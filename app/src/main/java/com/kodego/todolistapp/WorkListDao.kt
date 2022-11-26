package com.kodego.todolistapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkListDao {

    @Insert
    fun addWork(employee: WorkList)

    @Query("SELECT * FROM WorkList")
    fun getWorkList():MutableList<WorkList>

    @Query("UPDATE WorkList SET worklist = :workList WHERE number = :number")
    fun updateWorkList(workList:String,number:Int)

    @Query("DELETE FROM WorkList WHERE number = :number")
    fun deleteWorkList(number:Int)
}

