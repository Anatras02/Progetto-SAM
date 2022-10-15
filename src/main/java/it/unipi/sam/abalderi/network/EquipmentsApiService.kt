package it.unipi.sam.abalderi.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://backend-app-sam-vvyhv.ondigitalocean.app"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface EquipmentsApiService {
    @GET("equipments")
    suspend fun getEquipments(): List<Equipment>
}

object EquipmentsApi {
    val retrofitService: EquipmentsApiService by lazy { retrofit.create(EquipmentsApiService::class.java) }
}