package com.example.mostafapharmacyproject.Fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mostafapharmacyproject.Models.Medicine
import com.example.mostafapharmacyproject.Models.category
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.adapter.MedicineRecycleAdapter
import com.example.mostafapharmacyproject.databinding.FragmentFirstBinding
import com.example.mostafapharmacyproject.dp.PharmacyDataBase
import com.example.mostafapharmacyproject.dp.PharmacyMVVM
import com.example.networkboundresource.Utils.Resource
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.List

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MainMedicineListFragment : Fragment() {
    private val TAG = "MainMedicineListFragmen"

    private var _binding: FragmentFirstBinding? = null

    @Inject
    lateinit var pharmacyDataBase: PharmacyDataBase
    val pharmacyMVVM: PharmacyMVVM by viewModels()
    lateinit var controller: NavController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var medicineRecycleAdapter: MedicineRecycleAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(requireView())
        NavigationUI.setupWithNavController(binding.toolbar, controller)

        binding.toolbar.navigationIcon =
            tintIcon(ResourcesCompat.getDrawable(resources,
                R.drawable.ic_baseline_menu_open_24,
                context?.theme), R.color.teal_200)
        binding.toolbar.menu.findItem(R.id.PrescriptionFragment).icon =
            tintIcon(binding.toolbar.menu.findItem(R.id.PrescriptionFragment).icon,
                R.color.teal_200)

//        GlobalScope.launch(Dispatchers.IO) {
//            pharmacyDataBase.SimpleDao().InsertCategory(category(
//                5 , "Cold"
//            ))
//            pharmacyDataBase.SimpleDao().InsertMedicine(Medicine(
//                "2" , "mid" , "Cold" , "1" , "sd" , "sdf" , 150f, 87 , 50 , Calendar.getInstance().time , Calendar.getInstance().let {
//                    it.add(Calendar.MONTH ,6)
//                    it.time
//                }, "500"
//            ))
//            pharmacyDataBase.SimpleDao().InsertMedicine(Medicine(
//                "4" , "mid" , "Cold" , "1" , "sd" , "sdf" , 150f, 87 , 50 , Calendar.getInstance().time , Calendar.getInstance().let {
//                    it.add(Calendar.MONTH ,6)
//                    it.time
//                }, "500"
//            ))
//            pharmacyDataBase.SimpleDao().InsertMedicine(Medicine(
//                "6" , "mid" , "Cold" , "1" , "sd" , "sdf" , 150f, 87 , 50 , Calendar.getInstance().time , Calendar.getInstance().let {
//                    it.add(Calendar.MONTH ,6)
//                    it.time
//                }, "500"
//            ))
//        }

        binding.toolbar.setOnMenuItemClickListener {
            NavigationUI.onNavDestinationSelected(it, controller)
            true
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout).open()
        }



        pharmacyMVVM.category.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: category: ${it.data}")
            Log.d(TAG, "onViewCreated: error  ${it.error}")
            if (it.error != null)
                it.error.printStackTrace()
            else {
                binding.tabLayout.removeAllTabs()
                it.data?.forEach {
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText(it.Type))
                }
                binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
//                        GlobalScope.launch(Dispatchers.IO) {
//                            val medicine:List<Medicine> = pharmacyMVVM.pharmacyRepository.networkApi.getAllMedicines()
//                            activity?.runOnUiThread {
//                                medicineRecycleAdapter.submitList(medicine)
//                            }
//                        }
                        pharmacyMVVM.CategoryAndMedicine(tab?.text.toString())
                            .observe(viewLifecycleOwner) {
//                                binding.medicineRecycleItem.isVisible =
//                                    it.data!!.isNotEmpty() && it is Resource.Success
                                binding.EmptyLayout.isVisible =
                                    it.data.isNullOrEmpty() && it !is Resource.Success
                                binding.emptyTextView.text = it.error?.message
                                Log.d(TAG, "onViewCreated: " + it.data.toString())
//                                medicineRecycleAdapter.submitList(it.data?.map { it.medicines }
//                                    ?.flatten())
                            }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                })
            }
        }


        binding.medicineRecycleItem.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))
        medicineRecycleAdapter = MedicineRecycleAdapter(requireContext())
        binding.medicineRecycleItem.adapter = medicineRecycleAdapter


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