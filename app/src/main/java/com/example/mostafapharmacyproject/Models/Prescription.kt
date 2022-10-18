package com.example.mostafapharmacyproject.Models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Prescription table",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["Email"],
            childColumns = ["Email"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pharmacist::class,
            parentColumns = ["Email"],
            childColumns = ["DoEmail"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Prescription(

    @SerializedName("DiagnosticSide")
    @Expose
    var DiagnosticSide: List<String>?,
    @SerializedName("patient_name")
    @Expose
    var PatientName: String,
    @SerializedName("patient_address")
    @Expose
    var PatientAddress: String,
    @SerializedName("patient_age")
    @Expose
    val PatientAge: String,
    @SerializedName("email")
    @Expose
    val Email: String,
    @SerializedName("do_email")
    @Expose
    val DoEmail: String,
    @SerializedName("date_detection")
    @Expose
    val DateDetection: String,
) : Serializable{
    @PrimaryKey(true)
    var id: Int = 0

}