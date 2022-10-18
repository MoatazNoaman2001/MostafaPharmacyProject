package com.example.mostafapharmacyproject.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mostafapharmacyproject.Models.Prescription;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.adapter.MyPrescriptionsReycleAdapter;
import com.example.mostafapharmacyproject.databinding.FragmentMyPrecritpionsBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.example.mostafapharmacyproject.dp.Relations.PrescriptionAndMedicine;
import com.example.networkboundresource.Utils.Resource;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MyPrecritpionsFragment extends Fragment {
    private FragmentMyPrecritpionsBinding binding;
    private PharmacyMVVM pharmacyMVVM;
    private NavController controller;
    private SharedPreferences preferences;
    private MyPrescriptionsReycleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPrecritpionsBinding.inflate(getLayoutInflater());
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);
        preferences = requireContext().getSharedPreferences("Customers", MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(requireView());
        NavigationUI.setupWithNavController(binding.toolbar , controller);
        binding.toolbar.setNavigationIcon(
                ResourcesCompat.getDrawable(getResources() , R.drawable.ic_baseline_arrow_back_ios_new_24
                        , requireContext().getTheme())
        );
        binding.toolbar.setNavigationIconTint(
                ResourcesCompat.getColor(getResources() , R.color.teal_700 , requireContext().getTheme())
        );


        pharmacyMVVM.getAllPrescription(preferences.getString("cur_email" , null)).observe(getViewLifecycleOwner() , listResource -> {
            List<PrescriptionAndMedicine> prescriptions = listResource.getData();
            binding.EmptyLayout.setVisibility(
                    listResource instanceof Resource.Failed && (listResource.getData() == null ||listResource.getData().isEmpty())?
                            View.VISIBLE : View.GONE
            );
            binding.emptyTextView.setText(listResource.getError() != null ? listResource.getError().getMessage() : "No Error Gathered");
            binding.PrescriptionRecycleView.setVisibility(
                    listResource instanceof Resource.Success && (listResource.getData() != null || !listResource.getData().isEmpty())?
                            View.VISIBLE : View.GONE
            );

            adapter = new MyPrescriptionsReycleAdapter(requireContext());
            adapter.submitList(prescriptions);
            binding.PrescriptionRecycleView.setAdapter(adapter);

        });

    }
}