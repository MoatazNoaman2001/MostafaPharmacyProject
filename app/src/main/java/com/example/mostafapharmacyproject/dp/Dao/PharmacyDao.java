package com.example.mostafapharmacyproject.dp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.Pharmacist;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface PharmacyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertPharmacist(Pharmacist pharmacist);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertPharmacists(List<Pharmacist> pharmacists);

    @Delete
    void DeletePharmacists(List<Pharmacist> pharmacists);

    @Query("DELETE FROM 'pharmacist Table'")
    void DeletePharmacists();

    @Query("SELECT * FROM 'pharmacist Table' order by name ASC")
    Flow<List<Pharmacist>> getAllPharmacists();

    @Query("SELECT * FROM 'pharmacist Table' WHERE Email =:Email ORDER BY name ASC")
    Observable<Pharmacist> getPharmacist(String Email);
}
