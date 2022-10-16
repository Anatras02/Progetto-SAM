package it.unipi.sam.abalderi.network

import android.util.Log


data class Equipment(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    var distance: Float? = null,
    var locationVisible: Boolean = true,
    var textVisibile: Boolean = true
) {
    fun isVisible(): Boolean {
        return locationVisible && textVisibile
    }
}
