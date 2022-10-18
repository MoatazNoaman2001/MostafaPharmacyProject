package com.example.mostafapharmacyproject.Network

import com.example.mostafapharmacyproject.Models.*
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import okhttp3.Address
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface NetworkApi {
    companion object {
        val base_url = "http://mostafagamal2023-001-site1.itempurl.com/"
    }

    @Headers("Content-Type: application/json", "accept: */*")
    @GET("api/admin_/get_all_pharmacist")
    fun getAllPharmacies(): Observable<List<Pharmacist>>

    @Headers("Content-Type: application/json", "accept: */*")
    @GET("/api/admin_/get_all_category")
    suspend fun getAllCategory() : List<category>

    @Headers("Content-Type: application/json", "accept: */*")
    @GET("/api/admin_/get_mididcines")
    suspend fun getAllMedicines() : List<Medicine>


//
//    @Headers("Content-Type: application/json")
//    @GET("/api/admin_/get_all_customerrs")
//    fun getAllCustomers(): List<Customer>
//
//    @DELETE("/api/admin_/delete_pharmacist")
//    fun deletePharmacist(@Body pharmacist: Pharmacist)


    @Headers("Content-Type: application/json", "accept: */*")
    @POST("api/customer_/add_customer")
    fun postNewCustomer(@Body customer: Customer): Call<Customer>

    @Headers(
        "accept: */*"
    )
    @GET("/api/customer_/is_valid")
    fun IsValidEmail(@Query("email") email: String) : Observable<String>

    @Headers(
        "Content-Type: application/json",
        "accept: */*"
    )
    @POST("/api/customer_/login_customer")
    fun LoginCustomer(
        @Body loginBody: LoginBody,
    ): Call<Customer>;

    @Headers(
        "Content-Type: application/json",
        "accept: */*"
    )
    @GET("/api/customer_/get_pharmacist_email")
    fun getPharmaciesEmail():Observable<List<CustomerPharmacyEmailRequest>>

    @Headers(
        "Content-Type: application/json",
        "accept: */*"
    )
    @GET("/api/admin_/get_all_pharmacist")
    suspend fun getPharmaciesData():List<Pharmacist>

    @Headers("Content-Type: application/json")
    @DELETE("/api/customer_/delete_customer")
    fun DeleteCustomer(@Body customer: Customer);

    @Headers("Content-Type: application/json")
    @GET("/api/customer_/get_pharmacist_email")
    fun GetPharmancistEmail(): String;

    @Headers("Content-Type: application/json")
    @POST("/api/customer_/add-prescription")
    fun AddPrescription(@Body prescriptionAndMedicine: PrescriptionAndMedicine) : Call<Object>

    @GET("/api/customer_/Get_my_data")
    suspend fun GetContentData(@Query("email") email: String): GetFullData
}