package it.unipi.sam.abalderi.view_models

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.squareup.moshi.JsonDataException
import it.unipi.sam.abalderi.database.AppDatabase
import it.unipi.sam.abalderi.network.Equipment
import it.unipi.sam.abalderi.network.EquipmentsApi
import it.unipi.sam.abalderi.network.isInternetAvailable
import it.unipi.sam.abalderi.database.entities.Equipment as EquipmentDB
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException


class EquipmentsListViewModel(app: Application) : AndroidViewModel(app) {
    private val _equipments = MutableStateFlow<List<Equipment>?>(null)
    private val _shownEquipments = MutableStateFlow<List<Equipment>?>(null)
    val equipments: StateFlow<List<Equipment>?> = _shownEquipments

    private val _filterText = MutableLiveData<String>()
    val filterText: LiveData<String> = _filterText

    private val db = Room.databaseBuilder(
        getContext(),
        AppDatabase::class.java, "equipments-database"
    ).build()
    private val equipmentDao = db.equipmentDao()


    init {
        getEquipments()
    }

    /**
     * Context
     */
    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }


    /**
     * Database
     */
    private suspend fun handleEquipmentsDatabase(equipments: List<Equipment>) {
        val dbEquipments = equipmentDao.getAll()

        var insertEquipments = equipments.filter { it.toDatabaseEquipment() !in dbEquipments }
        var deleteEquipments = dbEquipments.filter { it.toNetworkEquipment() !in equipments }

        insertEquipments.forEach { equipmentDao.insert(it.toDatabaseEquipment()) }
        deleteEquipments.forEach { equipmentDao.delete(it) }
        equipments.forEach { equipmentDao.update(it.toDatabaseEquipment()) }
    }

    /**
     * Equipments
     */
    private fun setShownEquipments(equipments: List<Equipment>?) {
        _shownEquipments.update {
            equipments?.filter { it.isVisible() }?.sortedBy { null }
        }
    }


    private fun getEquipments() {
        viewModelScope.launch {
            if (isInternetAvailable(getContext())) {
                try {
                    val apiEquipments = EquipmentsApi.retrofitService.getEquipments()
                    handleEquipmentsDatabase(apiEquipments)

                    _shownEquipments.emit(apiEquipments)
                    _equipments.emit(apiEquipments)

                    return@launch
                } catch (e: HttpException) {
                    Log.e("Errore API", "HttpException")
                } catch (e: JsonDataException) {
                    Log.e("Errore API", "JsonDataException")
                }
            }

            val databaseEquipments = equipmentDao.getAll().map { it.toNetworkEquipment() }
            _shownEquipments.emit(databaseEquipments)
            _equipments.emit(databaseEquipments)
        }
    }

    fun filterEquipmentsByLocationAndSetDistance(userLocation: Location?) {
        fun getLocationFromCoordinates(latitude: Double, longitude: Double): Location {
            val location = Location("")
            location.latitude = latitude
            location.longitude = longitude

            return location
        }

        if (userLocation != null) {
            viewModelScope.launch {
                _equipments.buffer().collectLatest { localEquipments ->
                    if (localEquipments === null) {
                        return@collectLatest
                    } // initial case

                    _equipments.value?.forEach { equipment ->
                        val equipmentLocation =
                            getLocationFromCoordinates(equipment.latitude, equipment.longitude)

                        val distance = userLocation.distanceTo(equipmentLocation)

                        equipment.distance = distance
                        equipment.locationVisible = distance < 500
                    }

                    setShownEquipments(_equipments.value)
                }
            }
        }
    }

    private fun filterEquipmentsByName(name: String) {
        _equipments.value?.forEach { equipment ->
            equipment.textVisibile = equipment.name.lowercase().startsWith(name.lowercase())
        }

        setShownEquipments(_equipments.value)

    }

    /**
     * Filter
     */
    fun onFilterTextChange(value: String) {
        _filterText.value = value
        filterEquipmentsByName(value)
    }
}
