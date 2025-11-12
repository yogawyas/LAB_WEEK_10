package com.example.lab_week_10.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Create a database with the @Database annotation
@Database(entities = [Total::class], version = 1)
abstract class TotalDatabase : RoomDatabase() {
    // Declare the Dao
    abstract fun totalDao(): TotalDao
}
