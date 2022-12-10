package com.kodego.todolistapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class WorkList(var workList:String){

    @PrimaryKey(autoGenerate = true)
    var listNumber:Int = 0
}