package com.example.mostafapharmacyproject.dp.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.Prescription;

import java.io.Serializable;
import java.util.List;

public class PrescriptionAndMedicine implements Serializable {
    @Embedded
    public Prescription prescription;
    @Relation(
            parentColumn = "id",
            entityColumn = "PrescriptionID"
    )
    public List<Medicine> medicines;

    public PrescriptionAndMedicine() {}

    public PrescriptionAndMedicine(Prescription prescription, List<Medicine> medicines) {
        this.prescription = prescription;
        this.medicines = medicines;
    }
}
