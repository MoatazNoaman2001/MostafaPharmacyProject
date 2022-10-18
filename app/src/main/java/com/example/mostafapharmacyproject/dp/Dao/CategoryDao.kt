package com.example.mostafapharmacyproject.dp.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mostafapharmacyproject.Models.category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertCategory(category: category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertCategories(category: List<category>)

    @Query("DELETE from 'category'")
    suspend fun DeleteAllCategories()

    @Query("SELECT * FROM 'category' order by Type ASC")
    fun getCategory(): Flow<List<category>>

    @Query("SELECT * FROM 'category' order by Type ASC")
    suspend fun getSignalCategory(): List<category>
}