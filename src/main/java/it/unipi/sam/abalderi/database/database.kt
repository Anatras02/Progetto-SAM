package it.unipi.sam.abalderi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.unipi.sam.abalderi.database.dao.EquipmentDao
import it.unipi.sam.abalderi.database.entities.Equipment

@Database(entities = [Equipment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun equipmentDao(): EquipmentDao
}