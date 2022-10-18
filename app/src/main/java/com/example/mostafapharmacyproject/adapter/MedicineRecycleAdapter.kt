package com.example.mostafapharmacyproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.mostafapharmacyproject.Fragments.PrescriptionFragment
import com.example.mostafapharmacyproject.Models.Medicine
import com.example.mostafapharmacyproject.R
import com.example.mostafapharmacyproject.databinding.MedicineRecycleItemBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.lang.Exception

class MedicineRecycleAdapter(context: Context) :
    ListAdapter<Medicine, MedicineRecycleAdapter.ViewHolder>(MedicineDiffItem()) {
    val imageLoader:RequestManager = Glide.with(context)

    class ViewHolder(private val binding: MedicineRecycleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        public fun bindView(medicine: Medicine , imageLoader:RequestManager) {
            binding.MedicineName.text = medicine.name
            binding.concentration.text = medicine.Concentration
            binding.price.text = medicine.Price.toString()
            imageLoader.asDrawable()
                .load(R.drawable.medicine_recycle_item_img)
                .transform(RoundedCornersTransformation(15 , 8))
                .into(binding.appCompatImageView)
            binding.root.setOnClickListener {
                try {
                    Navigation.findNavController(view = it)
                        .navigate(resId = R.id.action_FirstFragment_to_SecondFragment,
                            args = PrescriptionFragment.newInstance(medicine = medicine).arguments)
                }catch (e:Exception){

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MedicineRecycleItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItem(position) != null)
            holder.bindView(getItem(position) , imageLoader)
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