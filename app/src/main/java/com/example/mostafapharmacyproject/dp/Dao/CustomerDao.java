package com.example.mostafapharmacyproject.dp.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mostafapharmacyproject.Models.Customer;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@Dao
public interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertCustomer(Customer customer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertCustomers(List<Customer> customer);

    @Delete
    void DeleteCustomers(List<Customer> customer);

    @Query("SELECT * FROM 'customer Table' ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM 'customer table' WHERE Email =:email")
    Observable<Customer> getCustomer(String email);

    @Query("SELECT EXISTS(SELECT * FROM 'customer Table' WHERE Email=:email)")
    Observable<Boolean> IsCustomerExist(String email);

}
