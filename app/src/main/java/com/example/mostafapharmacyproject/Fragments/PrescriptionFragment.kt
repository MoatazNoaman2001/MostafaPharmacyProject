package com.example.mostafapharmacyproject.Fragments

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mostafapharmacyproject.Models.*
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.adapter.PrescriptionMedicineRecycleItemAdapter
import com.example.mostafapharmacyproject.databinding.FragmentSecondBinding
import com.example.mostafapharmacyproject.dp.PharmacyMVVM
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@AndroidEntryPoint
class PrescriptionFragment : Fragment() {
    private val TAG = "PrescriptionFragment"

    private var _binding: FragmentSecondBinding? = null
    private var medicine: Medicine? = null
    lateinit var controller: NavController
    lateinit var alertDialog: androidx.appcompat.app.AlertDialog
    lateinit var builder: androidx.appcompat.app.AlertDialog.Builder
    private var emptyItem: Medicine = Medicine("",
        "",
        "",
        "",
        "",
        0f,
        0,
        0,
        Calendar.getInstance().time,
        Calendar.getInstance().time,
        "500" , "")
    private var adapter: PrescriptionMedicineRecycleItemAdapter? = null
    lateinit var preferences: SharedPreferences
    val pharmacyMVVM: PharmacyMVVM by viewModels()

    companion object {

        @JvmStatic
        fun newInstance(medicine: Medicine): PrescriptionFragment {
            val args = Bundle()
            val fragment = PrescriptionFragment()
            args.putSerializable("medicine", medicine)
            fragment.arguments = args
            return fragment
        }

    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        if (arguments != null && requireArguments().getSerializable("medicine") != null) {
            medicine = requireArguments().getSerializable("medicine") as Medicine
        }
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view)
        preferences =
            requireContext().getSharedPreferences("Customers", AppCompatActivity.MODE_PRIVATE)

        binding.EmptyLayout.visibility = View.VISIBLE
        NavigationUI.setupWithNavController(binding.toolbar, controller)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
        binding.toolbar.setNavigationIconTint(
            ResourcesCompat.getColor(resources, R.color.teal_700, activity!!.theme)
        )


        if (adapter == null) {
            adapter = PrescriptionMedicineRecycleItemAdapter()
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
            if (medicine != null) {
                val medicines: ArrayList<Medicine> = ArrayList<Medicine>().apply {
                    add(medicine!!)
//                    add(emptyItem)
                }
                binding.EmptyLayout.visibility = View.GONE
                adapter?.submitList(medicines)
                binding.toolbar.inflateMenu(R.menu.search_menu_done)
            } else {
//                val medicines: ArrayList<Medicine> = ArrayList<Medicine>().apply {
//                    add(emptyItem)
//                }
                adapter?.submitList(ArrayList())
            }
        } else {
            binding.recyclerView.adapter = adapter
        }

        controller.currentBackStackEntry?.lifecycleScope?.launchWhenCreated {
            controller.currentBackStackEntry?.savedStateHandle?.getLiveData("list",
                ArrayList<Medicine>())?.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                val medicines: ArrayList<Medicine> = java.util.ArrayList(it).apply {
                    addAll(adapter?.currentList?.let { it1 -> ArrayList(it1).clone() } as ArrayList<Medicine>)
                };
                pharmacyMVVM.pharmacistsLiveData.observe(viewLifecycleOwner) {
                    val pharmacists : ArrayList<Pharmacist> = ArrayList(it.data)
                    Log.d(TAG, "onViewCreated resource error: ${it.error} pharmacists list: $pharmacists")
                }
                adapter?.submitList(medicines)
                binding.toolbar.inflateMenu(R.menu.search_menu_done)
                binding.EmptyLayout.visibility = View.GONE
                binding.toolbar.setOnMenuItemClickListener {
                    if (it.itemId == R.id.Done) {
                        pharmacyMVVM.getPharmaciesEmails()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe (object : Observer<List<CustomerPharmacyEmailRequest>>{
                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onNext(it: List<CustomerPharmacyEmailRequest>) {
                                    Log.d(TAG, "onViewCreated: $it")
                                    val pharmacy: CustomerPharmacyEmailRequest = it[0]
                                    preferences.getString("cur_email", null)?.let {
                                        pharmacyMVVM.getCustomer(email = it)!!
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .observeOn(Schedulers.io())
                                            .subscribe(object : Observer<Customer> {
                                                override fun onSubscribe(d: Disposable) {
                                                }

                                                override fun onNext(it: Customer) {
                                                    Log.d(TAG, "onViewCreated: $it")
                                                    val prescription = Prescription(
                                                        null,
                                                        it.name,
                                                        it.address,
                                                        "20",
                                                        it.Email,
                                                        pharmacy.Email,
                                                        Calendar.getInstance().time.toGMTString()
                                                    )
                                                    val prescriptionAndMedicine : PrescriptionAndMedicine
                                                            = PrescriptionAndMedicine(
                                                        prescription , adapter?.currentList
                                                    )

                                                    pharmacyMVVM.sendPrescription(prescriptionAndMedicine)
                                                        .enqueue(object : retrofit2.Callback<Object> {
                                                            override fun onResponse(
                                                                call: Call<Object>,
                                                                response: Response<Object>,
                                                            ) {
                                                                pharmacyMVVM.InsertPrescription(prescription)
                                                            }

                                                            override fun onFailure(
                                                                call: Call<Object>,
                                                                t: Throwable,
                                                            ) {
                                                                pharmacyMVVM.InsertPrescription(prescription)
                                                                TODO("Not yet implemented")
                                                            }

                                                        })

                                                    requireActivity().runOnUiThread {
                                                        builder = androidx.appcompat.app.AlertDialog.Builder(
                                                            requireContext())
                                                            .apply {
                                                                setTitle("prescription sent successfully")
                                                                setMessage("prescription has been sent and it will be in wait queue until " +
                                                                        "pharmacy agree to all of medicine list")
                                                                setPositiveButton("Ok") { _, _ ->
                                                                    controller.popBackStack()
                                                                    alertDialog.dismiss()
                                                                }
                                                            }
                                                        alertDialog = builder.create()
                                                        alertDialog.show()
                                                    }

                                                }

                                                override fun onError(e: Throwable) {
                                                    Log.d(TAG, "onError: " + e.message)
                                                }

                                                override fun onComplete() {
                                                    Log.d(TAG, "onComplete: " + "completed")
                                                }

                                            })
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    Log.d(TAG, "onError: " + e.message)
                                }

                                override fun onComplete() {
                                }


                            });
                    }
                    return@setOnMenuItemClickListener super.onContextItemSelected(it)
                }
            }
        }


        binding.addItemLayout.setOnClickListener{
            Navigation.findNavController(view = it)
                .navigate(R.id.action_PrescriptionFragment_to_searchMedicineFragment)
        }

    }

    private fun tintIcon(overflowIcon: Drawable?, res: Int): Drawable {
        overflowIcon?.let { DrawableCompat.wrap(it) }
        DrawableCompat.setTint(overflowIcon!!,
            ResourcesCompat.getColor(resources, res, activity?.theme))
        return overflowIcon
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}