package com.kodego.todolistapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkList ( var workList: String){

    @PrimaryKey(autoGenerate = true)
    var number : Int = 0
}