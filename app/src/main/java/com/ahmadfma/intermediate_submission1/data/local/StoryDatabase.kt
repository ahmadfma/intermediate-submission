package com.ahmadfma.intermediate_submission1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmadfma.intermediate_submission1.data.model.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase: RoomDatabase()  {

    abstract fun storyDao(): StoryDao

    companion object {
        private const val DATABASE_NAME = "quote_database"

        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}