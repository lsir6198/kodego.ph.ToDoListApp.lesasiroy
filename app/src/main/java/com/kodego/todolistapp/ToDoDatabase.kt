package com.kodego.todolistapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WorkList::class],
    version = 1
)

abstract class ToDoDatabase: RoomDatabase() {

        abstract fun getWorklist(): WorkListDao


        companion object {
            @Volatile
            private var instance: ToDoDatabase? = null
            private val LOCK = Any()


            operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

            private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "toDoList"
            ).build()
        }
}
