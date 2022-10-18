package com.example.mostafapharmacyproject.dp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.Prescription;
import com.example.mostafapharmacyproject.Models.category;

import java.util.Calendar;
import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface SimpleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertCustomer(Customer customer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertPrescription(Prescription prescription);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertMedicine(Medicine medicine);


    @Query("SELECT * FROM 'medicine table' order by name ASC")
    Flow<List<Medicine>> getMedicines();
}
