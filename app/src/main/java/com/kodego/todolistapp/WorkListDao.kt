package com.kodego.todolistapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkListDao {

    @Insert
    fun addWork(employee: WorkList)

    @Query("SELECT * FROM WorkList")
    fun getAllWorkList():MutableList<WorkList>

    @Query("UPDATE WorkList SET worklist = :workList WHERE listNumber = :listNumber")
    fun updateWorkList(workList:String,listNumber:Int)

    @Query("DELETE FROM WorkList WHERE listNumber = :listNumber")
    fun deleteWorkList(listNumber:Int)

    @Query("DELETE FROM WorkList")
    fun clearAllTask()
}

