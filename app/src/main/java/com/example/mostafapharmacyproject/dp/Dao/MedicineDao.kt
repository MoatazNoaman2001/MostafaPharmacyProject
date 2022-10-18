package com.example.mostafapharmacyproject.dp.Dao

import androidx.room.*
import com.example.mostafapharmacyproject.Models.Medicine
import com.example.mostafapharmacyproject.dp.Relations.CategoryAndMedicineRelation
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun InsertMedicine(medicine: Medicine) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertMedicines(medicines: List<Medicine>)

    @Delete
    suspend fun DeleteMedicines(medicines: List<Medicine>)

    @Query("DELETE FROM 'medicine table'")
    suspend fun DeleteMedicines()

    @Transaction
    @Query("SELECT * FROM category WHERE Type =:type")
    fun allCategoryAndMedicine(type:String): Flow<List<CategoryAndMedicineRelation>>

    @Transaction
    @Query("SELECT * FROM category ORDER BY Type ASC")
    fun allCategoryAndMedicine(): Flow<List<CategoryAndMedicineRelation>>

    @Query("SELECT * FROM 'medicine table' WHERE id =:ID ORDER BY name ASC")
    suspend fun getMedicine(ID: String?): Medicine?
}