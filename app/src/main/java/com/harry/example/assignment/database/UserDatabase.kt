package com.harry.example.assignment.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import com.harry.example.assignment.database.dao.UserDao
import com.harry.example.assignment.database.entities.User

@Database(entities = arrayOf(User::class), version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}