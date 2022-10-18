package com.example.mostafapharmacyproject.Models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class GetFullData extends Customer{
    private List<Prescription> prescriptions;
    public GetFullData(@NonNull String Email, @NonNull String password, @NonNull String name, @NonNull String address, @NonNull String PhoneNumber, int age, @NonNull Date DateOfBirth, @NonNull Date DateRegistration, List<Prescription> prescriptions) {
        super(Email, password, name, address, PhoneNumber, age, DateOfBirth, DateRegistration);
        this.prescriptions = prescriptions;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
}
