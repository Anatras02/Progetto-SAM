package it.unipi.sam.abalderi

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import it.unipi.sam.abalderi.components.equipments_list_activity.EquipmentCard
import it.unipi.sam.abalderi.components.equipments_list_activity.FilterLayout
import it.unipi.sam.abalderi.components.general.MainStructure
import it.unipi.sam.abalderi.view_models.EquipmentsListViewModel

class EquipmentsListActivity : ComponentActivity() {
    private val equipmentsListViewModel: EquipmentsListViewModel by viewModels()

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
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation.addOnCompleteListener(this) {
                equipmentsListViewModel.filterEquipmentsByLocationAndSetDistance(it.result)
            }
        } else {
            val toast = Toast.makeText(
                applicationContext,
                this.getString(R.string.gps_turned_off),
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }


    /**
     * Android callback methods
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val equipments by equipmentsListViewModel.equipments.collectAsState(listOf())
            Log.v("equipments", equipments.toString())

            MainStructure(topBarText = stringResource(R.string.equipments_list)) {
                if (equipments !== null) {
                    FilterLayout(
                        viewModel = equipmentsListViewModel,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_space_between)),
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        equipments?.forEach { equipment ->
                            Log.v("equipment", equipment.name)
                            EquipmentCard(equipment.id, equipment.name, equipment.distance)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    override fun onStart() {
        handleGPSPermissions()

        super.onStart()
    }
}

