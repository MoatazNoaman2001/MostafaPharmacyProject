package com.example.mostafapharmacyproject.Models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "customer Table", indices = [
    Index(value = ["Email"], unique = true)
])
open class Customer(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("email")
    @Expose
    var Email: String,

    @SerializedName("password")
    @Expose
    var password: String,

    @SerializedName("username")
    @Expose
    var name: String,

    @SerializedName("address")
    @Expose
    var address: String,

    @SerializedName("phone")
    @Expose
    var PhoneNumber: String,

    @SerializedName("age")
    @Expose
    var age: Int = 0,

    @SerializedName("DateOfBirth")
    @Expose
    var DateOfBirth: Date,

    @SerializedName("date_register")
    @Expose(serialize = false)
    var DateRegistration: Date,
) {
    override fun toString(): String {
        return Gson().toJson(this, Customer::class.java)
    }
}
