package com.example.lab_week_10.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Create a database with the @Database annotation
// Now using version = 2 to match the latest schema (with @Embedded TotalObject)
@Database(entities = [Total::class], version = 2)
abstract class TotalDatabase : RoomDatabase() {

    // Declare the Dao
    abstract fun totalDao(): TotalDao

    companion object {
        // Singleton instance of the database
        @Volatile
        private var INSTANCE: TotalDatabase? = null

        fun getDatabase(context: Context): TotalDatabase {
            // Double-checked locking to ensure thread-safety
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TotalDatabase::class.java,
                    "total-database"
                )

                    .allowMainThreadQueries()

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
