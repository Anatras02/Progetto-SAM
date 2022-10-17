package it.unipi.sam.abalderi.database.dao

import androidx.room.*
import androidx.room.Dao
import it.unipi.sam.abalderi.database.entities.Equipment

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment")
    suspend fun getAll(): List<Equipment>

    @Query("SELECT * FROM equipment WHERE id=:id")
    suspend fun getFromId(id: Int): Equipment

    @Insert
    suspend fun insert(equipment: Equipment)

    @Update
    suspend fun update(equipment: Equipment)

    @Delete
    suspend fun delete(equipment: Equipment)
}