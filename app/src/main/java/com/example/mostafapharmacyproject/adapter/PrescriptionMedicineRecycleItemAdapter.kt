package com.example.mostafapharmacyproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mostafapharmacyproject.Models.Medicine
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.databinding.AddPrescriptionBinding
import com.example.mostafapharmacyproject.databinding.MedicineRecycleItemBinding

class PrescriptionMedicineRecycleItemAdapter() :
    ListAdapter<Medicine, PrescriptionMedicineRecycleItemAdapter.ViewHolder>(MedicineDiffItem()) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var medicineRecycleItemBinding: MedicineRecycleItemBinding
        lateinit var addPrescriptionBinding: AddPrescriptionBinding

        constructor(binding: MedicineRecycleItemBinding) : this(binding.root) {
            this.medicineRecycleItemBinding = binding
        }

        constructor(binding: AddPrescriptionBinding) : this(binding.root) {
            this.addPrescriptionBinding = binding
        }

        fun bindMedicineView(medicine: Medicine) {
            medicineRecycleItemBinding.MedicineName.text = medicine.name
            medicineRecycleItemBinding.concentration.text = medicine.Concentration
            medicineRecycleItemBinding.price.text = medicine.Price.toString()
            medicineRecycleItemBinding.root.setOnLongClickListener {
                val medicines:ArrayList<Medicine> = ArrayList(currentList).clone() as ArrayList<Medicine> /* = java.util.ArrayList<com.example.mostafapharmacyproject.Models.Medicine> */
                medicines.remove(medicine)
                submitList(medicines)
                true
            }
        }

        fun bindLastView() {
            addPrescriptionBinding.root.setOnClickListener {
                Navigation.findNavController(view = it)
                    .navigate(R.id.action_PrescriptionFragment_to_searchMedicineFragment)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id == -1) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            ViewHolder(AddPrescriptionBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        } else {
            ViewHolder(MedicineRecycleItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ${holder.itemViewType}" )
        if (getItem(position) != null)
            if (holder.itemViewType == 0) {
                holder.bindMedicineView(getItem(position))
            }else{
                holder.bindLastView()
            }
    }

    class MedicineDiffItem : DiffUtil.ItemCallback<Medicine>() {
        override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
            return oldItem == newItem
        }

    }
}