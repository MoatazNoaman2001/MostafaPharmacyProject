package com.example.mostafapharmacyproject.dp.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.Prescription;
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface PrescriptionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertPrescription(Prescription prescription);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertPrescriptions(List<Prescription> prescriptions);

    @Delete
    void DeletePrescriptions(List<Prescription> prescriptions);

    @Query("DELETE FROM 'Prescription table'")
    void DeletePrescriptions();

    @Transaction
    @Query("SELECT * FROM 'Prescription table' order by DateDetection ASC")
    Flow<List<PrescriptionAndMedicine>> getAllPrescriptions();


    @Transaction
    @Query("SELECT * FROM 'Prescription table' WHERE id =:ID")
    Observable<PrescriptionAndMedicine> getPrescription(String ID);


}
