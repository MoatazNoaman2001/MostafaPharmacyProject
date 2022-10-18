package com.example.mostafapharmacyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.RequestManager;
import com.example.mostafapharmacyproject.Models.Pharmacist;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.adapter.PharmacyViewPagerAdapter;
import com.example.mostafapharmacyproject.databinding.FragmentPharmaciesBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.example.networkboundresource.Utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PharmaciesFragment extends Fragment {
    private static final String TAG = "PharmaciesFragment";

    private FragmentPharmaciesBinding binding;
    private NavController navController;
    private PharmacyMVVM pharmacyMVVM;
    private PharmacyViewPagerAdapter adapter;

    @Inject
    public RequestManager imageLoader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPharmaciesBinding.inflate(getLayoutInflater());
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireView());
        NavigationUI.setupWithNavController(binding.toolbar, navController);
        binding.toolbar.setNavigationIcon(
                ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_arrow_back_ios_new_24
                        , requireContext().getTheme())
        );
        binding.toolbar.setNavigationIconTint(
                ResourcesCompat.getColor(getResources(), R.color.teal_700, requireContext().getTheme())
        );

        pharmacyMVVM.getPharmacistsLiveData().observe(getViewLifecycleOwner(), listResource -> {
            List<Pharmacist> pharmacists = listResource.getData();
            Log.d(TAG, "onViewCreated: pharmacies: " + pharmacists);
//            binding.EmptyLayout.setVisibility(
//                    listResource instanceof Resource.Failed && (listResource.getData() == null || listResource.getData().isEmpty()) ?
//                            View.VISIBLE : View.GONE
//            );
//            binding.emptyTextView.setText(listResource.getError() != null ? listResource.getError().getMessage() : "No Error Gathered");
//            binding.ViewPager.setVisibility(
//                    listResource instanceof Resource.Success && (listResource.getData() != null || !listResource.getData().isEmpty()) ?
//                            View.VISIBLE : View.GONE
//            );

            adapter = new PharmacyViewPagerAdapter(pharmacists, imageLoader);
            binding.ViewPager.setAdapter(adapter);
            binding.ViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            binding.ViewPager.setOffscreenPageLimit(10);
            binding.ViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            binding.ViewPager.setClipToPadding(true);
            binding.ViewPager.setClipChildren(true);
            binding.ViewPager.setPageTransformer(new MarginPageTransformer(40));
            CompositePageTransformer transformer = new CompositePageTransformer();
            transformer.addTransformer((page, position) -> {
                page.setScaleY(0.85f + (1 - Math.abs(position)) * 0.15f);
            });
            binding.ViewPager.setPageTransformer(transformer);
        });
    }
}