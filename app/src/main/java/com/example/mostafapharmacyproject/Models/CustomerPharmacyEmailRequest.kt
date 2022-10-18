package com.example.mostafapharmacyproject.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerPharmacyEmailRequest(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("do_email")
    @Expose
    val Email: String,
)