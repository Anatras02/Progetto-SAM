package it.unipi.sam.abalderi.view_models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.squareup.moshi.JsonDataException
import it.unipi.sam.abalderi.database.AppDatabase
import it.unipi.sam.abalderi.network.Equipment
import it.unipi.sam.abalderi.network.EquipmentsApi
import it.unipi.sam.abalderi.network.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.HttpException


class SingleEquipmentViewModel(app: Application) : AndroidViewModel(app) {
    private val _equipment = MutableLiveData<Equipment?>(null)
    val equipment: LiveData<Equipment?> = _equipment

    private val db = Room.databaseBuilder(
        getContext(),
        AppDatabase::class.java, "equipments-database"
    ).build()
    private val equipmentDao = db.equipmentDao()

    /**
     * Context
     */
    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }


    fun getEquipment(id: Int) {
        viewModelScope.launch {
            if (isInternetAvailable(getContext())) {
                try {
                     _equipment.value = EquipmentsApi.retrofitService.getEquipment(id)

                    return@launch
                } catch (e: HttpException) {
                    Log.e("Errore API", "HttpException")
                } catch (e: JsonDataException) {
                    Log.e("Errore API", "JsonDataException")
                }
            }

            _equipment.value = equipmentDao.getFromId(id).toNetworkEquipment()
        }
    }
}
