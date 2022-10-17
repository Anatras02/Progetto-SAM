package it.unipi.sam.abalderi.network

import it.unipi.sam.abalderi.database.entities.Equipment as DatabaseEquipment

data class Equipment(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val note: String? = null,
    var distance: Float? = null,
    var locationVisible: Boolean = true,
    var textVisibile: Boolean = true
) {
    fun isVisible(): Boolean {
        return locationVisible && textVisibile
    }

    fun toDatabaseEquipment(): DatabaseEquipment {
        return DatabaseEquipment(id, name, description, latitude, longitude, note)
    }
}
