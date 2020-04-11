package com.ruczajsoftware.workoutrival.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ruczajsoftware.workoutrival.data.db.entity.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET token = null WHERE `index` = :index")
    fun nullifyToken(index: Int): Int

    @Query("SELECT * FROM auth_token WHERE `index` = :index")
    suspend fun searchByPk(index: Int): AuthToken?
}