package com.example.mostafapharmacyproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.mostafapharmacyproject.Models.Prescription
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.databinding.FragmentSecondBinding
import com.example.mostafapharmacyproject.databinding.MyPrescriptionRecycleItemLayoutBinding
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine

class MyPrescriptionsReycleAdapter(val context: Context) :
    ListAdapter<PrescriptionAndMedicine, MyPrescriptionsReycleAdapter.ViewHolder>(PrescriptionItemDiff()) {
    class ViewHolder(val binding: MyPrescriptionRecycleItemLayoutBinding  ) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: PrescriptionAndMedicine? , context:Context) {
            binding.PatientName.text = "name" +item?.prescription?.PatientName
            binding.Age.text = item?.prescription?.PatientAge
            binding.DoName.text = "D/"+item?.prescription?.PatientName
            binding.DoEmail.text = item?.prescription?.DoEmail
            binding.configurationDate.text = "Date: "+item?.prescription?.DateDetection
            binding.medicineAmount.text = "medicine amount: " + item?.medicines?.size

            binding.imageView.setOnClickListener {
                if (!binding.medicineRecycleView.isVisible){
                    binding.imageView.setImageDrawable(
                        ResourcesCompat.getDrawable(context.resources  , R.drawable.ic_baseline_keyboard_arrow_up_24 , context.theme)
                    )
                    binding.medicineRecycleView.visibility = View.VISIBLE

                    val medicineRecycleAdapter = MedicineRecycleAdapter(context)
                    binding.medicineRecycleView.adapter= medicineRecycleAdapter
                    medicineRecycleAdapter.submitList(item?.medicines)

                    TransitionManager.beginDelayedTransition(binding.root , AutoTransition())
                }else{
                    binding.imageView.setImageDrawable(
                        ResourcesCompat.getDrawable(context.resources  , R.drawable.ic_arrow_down_24 , context.theme)
                    )
                    binding.medicineRecycleView.visibility = View.GONE
                    TransitionManager.beginDelayedTransition(binding.root , AutoTransition())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyPrescriptionRecycleItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context) , parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItem(position) != null)
            holder.bindItem(getItem(position) , context = context)
    }

    class PrescriptionItemDiff : DiffUtil.ItemCallback<PrescriptionAndMedicine>() {
        override fun areItemsTheSame(oldItem: PrescriptionAndMedicine, newItem: PrescriptionAndMedicine): Boolean {
            return oldItem.prescription.id == newItem.prescription.id
        }

        override fun areContentsTheSame(oldItem: PrescriptionAndMedicine, newItem: PrescriptionAndMedicine): Boolean {
            TODO("Not yet implemented")
        }

    }
}