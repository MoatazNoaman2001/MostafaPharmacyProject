package com.example.mostafapharmacyproject.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "pharmacist Table")
class Pharmacist(
    @PrimaryKey(false)
    @SerializedName("pharmacist_email")
    @Expose
    val Email: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("address")
    @Expose
    val address: String,
    @SerializedName("phone")
    @Expose
    val PhoneNumber: String,
    @SerializedName("date_register")
    @Expose(deserialize = false)
    val DateRegistration: Date,
    @SerializedName("cardid")
    val CardId: String,
    @SerializedName("date_birth")
    @Expose(deserialize = false)
    val DateBirth: Date,
    @SerializedName("date_graduation")
    @Expose(deserialize = false)
    val DateGraduation: Date,
    @SerializedName("years_experience")
    @Expose(deserialize = false)
    val YearsEXP: Int,
){
    override fun toString(): String {
        return Gson().toJson(this , Pharmacist::class.java)
    }
}