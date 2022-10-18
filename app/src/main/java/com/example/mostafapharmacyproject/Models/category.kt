package com.example.mostafapharmacyproject.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category", indices = [
    Index(value = ["Type"], unique = true)
])
class category(
    @PrimaryKey(true)
    @NonNull
    var id: Int,
    @SerializedName("type")
    val Type: String,
) : java.io.Serializable{
    override fun toString(): String {
        return Gson().toJson(this , category::class.java)
    }
}