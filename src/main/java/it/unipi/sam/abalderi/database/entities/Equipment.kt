package it.unipi.sam.abalderi.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import it.unipi.sam.abalderi.network.Equipment as NetworkEquipment

@Entity
data class Equipment(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "note") val note: String? = null,
) {
    fun toNetworkEquipment(): NetworkEquipment {
        return NetworkEquipment(id, name, description, latitude, longitude, note)
    }
}