package com.example.mostafapharmacyproject.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginBody(
    @SerializedName("email")
    @Expose
    val cus_email: String,
    @SerializedName("password")
    @Expose
    val password: String,
) : Serializable {
}