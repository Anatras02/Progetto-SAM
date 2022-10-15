package it.unipi.sam.abalderi.network


data class Equipment(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    var distance: Float? = null
)