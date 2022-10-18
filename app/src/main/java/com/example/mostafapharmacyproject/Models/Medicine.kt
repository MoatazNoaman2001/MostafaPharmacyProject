package com.example.mostafapharmacyproject.Models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "medicine table",
    foreignKeys = [ForeignKey(entity = category::class,
        parentColumns = arrayOf("Type"),
        childColumns = arrayOf("Type"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
)
data class Medicine(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("type")
    val Type: String,

    val PrescriptionID:String,
    @SerializedName("manufacturer")
    @Expose
    val Manufacturer: String,
    @SerializedName("indications")
    @Expose
    val Indications: String,
    @SerializedName("price")
    @Expose
    val Price: Float,
    @SerializedName("categoryid")
    @Expose
    var CategoryID: Int,
    @SerializedName("quantity")
    @Expose
    val Quantity: Int,
    @SerializedName("pro_date")
    @Expose
    val ProDate: Date,
    @SerializedName("ex_date")
    @Expose
    val ExDate: Date,
    @SerializedName("concentration")
    @Expose
    val Concentration: String,

    @SerializedName("imagePath")
    @Expose
    val imagePath:String,
)  :Serializable{
    @PrimaryKey(true)
    var id: Int = 0

    override fun toString(): String {
        return Gson().toJson(this , Medicine::class.java);
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Medicine) return false
        val medicine2: Medicine = other
        return Objects.equals(id , medicine2.id) &&
                Objects.equals(name , medicine2.name) &&
                Objects.equals(Type , medicine2.Type) &&
                Objects.equals(PrescriptionID , medicine2.PrescriptionID) &&
                Objects.equals(Price , medicine2.Price) &&
                Objects.equals(Quantity , medicine2.Quantity)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Type.hashCode()
        result = 31 * result + PrescriptionID.hashCode()
        result = 31 * result + Manufacturer.hashCode()
        result = 31 * result + Indications.hashCode()
        result = 31 * result + Price.hashCode()
        result = 31 * result + CategoryID
        result = 31 * result + Quantity
        result = 31 * result + ProDate.hashCode()
        result = 31 * result + ExDate.hashCode()
        result = 31 * result + Concentration.hashCode()
        result = 31 * result + imagePath.hashCode()
        result = 31 * result + id
        return result
    }
}