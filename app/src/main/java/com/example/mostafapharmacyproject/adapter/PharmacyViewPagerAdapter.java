package com.example.mostafapharmacyproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.mostafapharmacyproject.Models.Pharmacist;
import com.example.mostafapharmacyproject.databinding.ViewPagerPrescriptionItemBinding;

import java.util.List;

public class PharmacyViewPagerAdapter extends RecyclerView.Adapter<PharmacyViewPagerAdapter.ViewHolder> {
    List<Pharmacist> pharmacists;

    public PharmacyViewPagerAdapter(List<Pharmacist> pharmacists, RequestManager imageLoader) {
        this.pharmacists = pharmacists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewPagerPrescriptionItemBinding.inflate(
                LayoutInflater.from(parent.getContext()) , parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewPagerPrescriptionItemBinding binding;
        public ViewHolder(ViewPagerPrescriptionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Pharmacist pharmacist){

        }
    }
}
