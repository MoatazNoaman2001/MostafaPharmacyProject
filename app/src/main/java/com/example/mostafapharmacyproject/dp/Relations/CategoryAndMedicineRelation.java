package com.example.mostafapharmacyproject.dp.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.category;

import java.util.List;

public class CategoryAndMedicineRelation {
    @Embedded
    public category category;
    @Relation(
            parentColumn = "Type",
            entityColumn = "Type"
    )
    public List<Medicine> medicines;

    public CategoryAndMedicineRelation() {
    }

    public CategoryAndMedicineRelation(com.example.mostafapharmacyproject.Models.category category, List<Medicine> medicines) {
        this.category = category;
        this.medicines = medicines;
    }

    @Override
    public String toString() {
        return "CategoryAndMedicineRelation{" +
                "category=" + category +
                ", medicines=" + medicines +
                '}';
    }
}
