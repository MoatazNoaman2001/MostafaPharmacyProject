package com.example.mostafapharmacyproject.dp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mostafapharmacyproject.Models.Customer
import com.example.mostafapharmacyproject.Models.CustomerPharmacyEmailRequest
import com.example.mostafapharmacyproject.Models.Pharmacist
import com.example.mostafapharmacyproject.Models.Prescription
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PharmacyMVVM @Inject constructor(val pharmacyRepository: PharmacyRepository) : ViewModel() {
    //    public val medicines = pharmacyRepository.pharmacyDataBase.SimpleDao().medicines.asLiveData()
    public val customers: LiveData<MutableList<Customer>> =
        pharmacyRepository.pharmacyDataBase.customerDao().allCustomers
    public fun CategoryAndMedicine(type:String) = pharmacyRepository.medicine(type).asLiveData()
    val category = pharmacyRepository.category.asLiveData()
    public val pharmacistsLiveData = pharmacyRepository.pharmacists.asLiveData()


    fun InsertCustomer(customer: Customer) {
        GlobalScope.launch(Dispatchers.IO) {
            pharmacyRepository.pharmacyDataBase.customerDao().InsertCustomer(customer)
        }
    }

    fun InsertPrescription(prescription: Prescription) {
        GlobalScope.launch(Dispatchers.IO) {
            pharmacyRepository.pharmacyDataBase.prescriptionDao().InsertPrescription(prescription)
        }
    }

    fun InsertCustomers(customers: List<Customer>) {
        GlobalScope.launch(Dispatchers.IO) {
            pharmacyRepository.pharmacyDataBase.customerDao().InsertCustomers(customers)
        }
    }

    fun DeleteCustomer(customer: Customer) {
        GlobalScope.launch(Dispatchers.IO) {
            pharmacyRepository.pharmacyDataBase.customerDao().DeleteCustomers(listOf(customer))
        }
    }

    fun getCustomer(email: String): Observable<Customer>? {
        return pharmacyRepository.pharmacyDataBase.customerDao().getCustomer(email)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
    }

    fun IsCustomerExist(email: String):Observable<Boolean> =
        pharmacyRepository.pharmacyDataBase.customerDao().IsCustomerExist(email)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())


    fun getPharmaciesEmails(): Observable<List<CustomerPharmacyEmailRequest>> =
        pharmacyRepository.pharmacistsEmails()

    fun getAllPrescription(email: String) =
        pharmacyRepository.MyPrescritions(email = email).asLiveData()

    public fun isValidEmail(email: String): Observable<String> =
        pharmacyRepository.IsValidEmail(email)

    public val pharmacists: Observable<List<Pharmacist>> = pharmacyRepository.getPharmaciesData()

    public fun CreateNewUser(customer: Customer): Call<Customer> =
        pharmacyRepository.CreateNewUser(customer)

    public fun LoginUser(email: String, password: String): Call<Customer> =
        pharmacyRepository.LoginUser(email, password)


    public fun sendPrescription(prescriptionAndMedicine: PrescriptionAndMedicine): Call<Object> =
        pharmacyRepository.sendPrescription(prescriptionAndMedicine)

}