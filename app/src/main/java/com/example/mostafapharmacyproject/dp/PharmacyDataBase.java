package com.example.mostafapharmacyproject.dp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.Models.Pharmacist;
import com.example.mostafapharmacyproject.Models.Prescription;
import com.example.mostafapharmacyproject.Models.category;
import com.example.mostafapharmacyproject.dp.Converters.DateConverter;
import com.example.mostafapharmacyproject.dp.Converters.StringListConverter;
import com.example.mostafapharmacyproject.dp.Dao.CategoryDao;
import com.example.mostafapharmacyproject.dp.Dao.CustomerDao;
import com.example.mostafapharmacyproject.dp.Dao.MedicineDao;
import com.example.mostafapharmacyproject.dp.Dao.PharmacyDao;
import com.example.mostafapharmacyproject.dp.Dao.PrescriptionDao;
import com.example.mostafapharmacyproject.dp.Dao.SimpleDao;

@Database(entities = {Customer.class, Medicine.class, Prescription.class, Pharmacist.class, category.class}, version = 3 , exportSchema = false)
@TypeConverters({DateConverter.class , StringListConverter.class})
public abstract class PharmacyDataBase extends RoomDatabase {
    public abstract SimpleDao SimpleDao();
    public abstract CustomerDao customerDao();
    public abstract PharmacyDao pharmacyDao();
    public abstract MedicineDao medicineDao();
    public abstract PrescriptionDao prescriptionDao();
    public abstract CategoryDao categoryDao();
}
