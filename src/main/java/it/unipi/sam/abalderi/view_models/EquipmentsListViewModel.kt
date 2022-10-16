package it.unipi.sam.abalderi.view_models

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unipi.sam.abalderi.network.Equipment
import it.unipi.sam.abalderi.network.EquipmentsApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException


class EquipmentsListViewModel : ViewModel() {
    private val _equipments = MutableStateFlow<List<Equipment>?>(null)
    private val _shownEquipments = MutableStateFlow<List<Equipment>?>(null)
    val equipments: StateFlow<List<Equipment>?> = _shownEquipments

    private val _filterText = MutableLiveData<String>()
    val filterText: LiveData<String> = _filterText


    init {
        getEquipments()
    }

    /**
     * Equipments
     */
    private fun setShownEquipments(equipments: List<Equipment>?) {
        _shownEquipments.update { equipments?.filter { it.isVisible() } }
    }

    private fun getEquipments() {
        viewModelScope.launch {
            var apiEquipments = listOf<Equipment>()

            try {
                apiEquipments = EquipmentsApi.retrofitService.getEquipments()
            } catch (e: HttpException) {
                // TODO: Mostrare servizio temporaneamente non disponibile
            }

            _equipments.emit(apiEquipments)
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
