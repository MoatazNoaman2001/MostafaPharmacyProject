package com.example.mostafapharmacyproject.dp.Relations;


import androidx.room.Relation;

import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.Models.Prescription;

import java.util.List;

public class CustomerAndPrescription {
    Customer customer;
    @Relation(
            parentColumn = "Email",
            entityColumn = "Email"
    )
    List<Prescription> prescriptions;

    public CustomerAndPrescription(Customer customer, List<Prescription> prescriptions) {
        this.customer = customer;
        this.prescriptions = prescriptions;
    }
}
