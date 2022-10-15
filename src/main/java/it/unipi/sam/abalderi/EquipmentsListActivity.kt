package it.unipi.sam.abalderi

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import it.unipi.sam.abalderi.components.equipments_list_activity.EquipmentCard
import it.unipi.sam.abalderi.components.equipments_list_activity.Filter
import it.unipi.sam.abalderi.components.general.MainStructure
import it.unipi.sam.abalderi.network.Equipment
import it.unipi.sam.abalderi.network.EquipmentsApi
import kotlinx.coroutines.*
import retrofit2.HttpException

class EquipmentsListActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val equipments = MutableLiveData<List<Equipment>>()
    private lateinit var mCoroutineScope: CoroutineScope //https://medium.com/@pramahalqavi/several-types-of-kotlin-coroutine-scope-difference-coroutinescope-globalscope-etc-9f086cd40173

    /**
     * GPS and Coordinates
     */
    private fun handleGPSPermissions() {
        val requestMultiplePermissions = ActivityResultContracts.RequestMultiplePermissions()


        val gpsPermissions =
            registerForActivityResult(requestMultiplePermissions) { permissions ->
                if (permissions.all { it.value }) {
                    onGPSPermissionsAllowed()
                } else {
                    onGPSPermissionDenied()
                }
            }

        gpsPermissions.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun onGPSPermissionDenied() {
        val toast = Toast.makeText(
            applicationContext,
            this.getString(R.string.invalid_gps_permission),
            Toast.LENGTH_LONG
        )
        toast.show()
    }

    private fun onGPSPermissionsAllowed() {
        fun isLocationEnabled(): Boolean {
            val locationManager: LocationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }


        if (isLocationEnabled()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnCompleteListener(this) {
                val location = it.result

                setEquipments(location)
            }
        } else {
            val toast = Toast.makeText(
                applicationContext,
                this.getString(R.string.gps_turned_off),
                Toast.LENGTH_LONG
            )
            toast.show()

            setEquipments(null)
        }
    }

    /**
     * Equipments
     */
    private fun setEquipments(userLocation: Location?) {
        fun getLocationFromCoordinates(latitude: Double, longitude: Double): Location {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude

            return location
        }

        fun filterEquipments(equipments: List<Equipment>): List<Equipment> {
            if (userLocation != null) {
                return equipments.filter { equipment ->
                    return@filter userLocation.distanceTo(
                        getLocationFromCoordinates(
                            equipment.latitude,
                            equipment.longitude
                        )
                    ) < 500
                }
            }

            return equipments
        }

        mCoroutineScope = CoroutineScope(Dispatchers.Main)
        mCoroutineScope.launch {
            try {
                val results = EquipmentsApi.retrofitService.getEquipments()

                val filteredEquipments = filterEquipments(results)

                if (userLocation != null) {
                    filteredEquipments.forEach { equipment ->
                        equipment.distance = userLocation.distanceTo(
                            getLocationFromCoordinates(
                                equipment.latitude,
                                equipment.longitude
                            )
                        )
                    }
                }

                equipments.postValue(filteredEquipments)


            } catch (e: HttpException) {
                // TODO: Mostrare servizio temporaneamente non disponibile
            }
        }
    }


    /**
     * Android callback methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val equipments: List<Equipment> by this.equipments.observeAsState(listOf())

            MainStructure(topBarText = stringResource(R.string.equipments_list)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_space_between)),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.fab_margin))
                        .verticalScroll(rememberScrollState())
                ) {
                    Filter("Filtra attrezzature")
                    equipments.forEach { equipment ->
                        EquipmentCard(equipment.id, equipment.name, equipment.distance)
                    }
                }
            }
        }
    }

    override fun onStart() {
        handleGPSPermissions()

        super.onStart()
    }

    override fun onStop() {
        super.onStop()

        if (::mCoroutineScope.isInitialized && mCoroutineScope.isActive) {
            mCoroutineScope.cancel()
        }
    }
}

