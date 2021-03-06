package com.harry.example.assignment.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harry.example.assignment.database.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User?)

    @Query("SELECT * FROM users WHERE email =:email AND password=:password")
    suspend fun getUser(email: String?, password: String?): User?
}