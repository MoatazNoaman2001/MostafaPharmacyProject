package com.example.mostafapharmacyproject.dp

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mostafapharmacyproject.Models.*
import com.example.mostafapharmacyproject.Network.NetworkApi
import com.example.mostafapharmacyproject.dp.Relations.CategoryAndMedicineRelation
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine
import com.example.networkboundresource.Utils.networkBoundResource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import kotlinx.coroutines.flow.asFlow
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PharmacyRepository @Inject constructor(
    val pharmacyDataBase: PharmacyDataBase,
    val networkApi: NetworkApi,
) {
    private val TAG = "PharmacyRepository"

    fun getPharmaciesData(): Observable<List<Pharmacist>> {
        return networkApi.getAllPharmacies()
    }

    fun CreateNewUser(customer: Customer): Call<Customer> {
        return networkApi.postNewCustomer(customer)
    }

    fun IsValidEmail(email: String): Observable<String> {
        return networkApi.IsValidEmail(email)
    }

    fun LoginUser(email: String, password: String): Call<Customer> {
        return networkApi.LoginCustomer(LoginBody(email, password))
    }

    fun MyPrescritions(email: String) = networkBoundResource(
        query = {
            pharmacyDataBase.prescriptionDao().allPrescriptions
        }, fetch = {
            networkApi.GetContentData(email).prescriptions
        }, saveFetchResult = {
            pharmacyDataBase.inTransaction().apply {
                pharmacyDataBase.prescriptionDao().DeletePrescriptions()
                pharmacyDataBase.prescriptionDao()
                    .InsertPrescriptions(it)

//                pharmacyDataBase.medicineDao()
//                    .InsertMedicines(it.map { o -> o.medicines }.flatten())
            }
        }
    )


    val category = networkBoundResource(
        query = {
            pharmacyDataBase.categoryDao().getCategory()
        }, fetch = {
            networkApi.getAllCategory()
        }, saveFetchResult = {
            Log.d(TAG, "category list: $it")
            pharmacyDataBase.inTransaction().run {
                pharmacyDataBase.categoryDao().DeleteAllCategories()
                pharmacyDataBase.categoryDao().InsertCategories(it)
            }
        }

    )

    fun medicine(text: String) = networkBoundResource(
        query = {
            if (text == "ALL")
                pharmacyDataBase.medicineDao().allCategoryAndMedicine()
            else
                pharmacyDataBase.medicineDao().allCategoryAndMedicine(text)
        },
        fetch = {
            networkApi.getAllMedicines()
        },
        saveFetchResult = {
            Log.d(TAG, "medicine list: $it")
            Log.d(TAG, "category and medicine list : $it")
            pharmacyDataBase.medicineDao().InsertMedicines(it)
        }
    )

    fun pharmacistsEmails(): Observable<List<CustomerPharmacyEmailRequest>> =
        networkApi.getPharmaciesEmail()

    fun sendPrescription(prescriptionAndMedicine: PrescriptionAndMedicine): Call<Object> {
        return networkApi.AddPrescription(prescriptionAndMedicine)
    }

    val pharmacists = networkBoundResource(
        query = {
            pharmacyDataBase.pharmacyDao().allPharmacists
        }, fetch = {
            networkApi.getPharmaciesData()
        }, saveFetchResult = {
            Log.d("TAG", " fetched pharmacies data: $it")
            pharmacyDataBase.pharmacyDao().InsertPharmacists(it)
        }
    )

}