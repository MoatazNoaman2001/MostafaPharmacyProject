package com.example.mostafapharmacyproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.databinding.FragmentInitaiteBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class InitaiteFragment extends Fragment {
    private static final String TAG = "InitaiteFragment";

    private FragmentInitaiteBinding binding;
    private PharmacyMVVM pharmacyMVVM;
    private SharedPreferences preferences;
    private NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInitaiteBinding.inflate(inflater, container, false);
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);
        preferences = requireActivity().getSharedPreferences("Customers", Context.MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(requireView());
        String email = preferences.getString("cur_email", null);
        if (email == null) {
            binding.enterLogo.postDelayed(() -> controller.navigate(R.id.action_initaiteFragment_to_loginFragment), 2000);
        } else {
            Log.d(TAG, "onViewCreated:  " + email);
            pharmacyMVVM.isValidEmail(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull String o) {
                            Log.d(TAG, "onNext: " + o);
                            if (o.trim().equalsIgnoreCase("valid"))
                                pharmacyMVVM.IsCustomerExist(email).subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                                        try {
                                            if (aBoolean)
                                                controller.navigate(R.id.action_initaiteFragment_to_mainActivity);
                                            else
                                                controller.navigate(R.id.action_initaiteFragment_to_loginFragment);
                                        }catch (Exception e){

                                        }
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                        Log.d(TAG, "onError: " + e.getMessage());
                                        controller.navigate(R.id.action_initaiteFragment_to_loginFragment);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: email exist check completed" );
                                    }
                                });
                            else {
                                Toast.makeText(requireContext(), "your email deleted or disabled", Toast.LENGTH_SHORT).show();
                                controller.navigate(R.id.action_initaiteFragment_to_loginFragment);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "your email deleted or disabled", Toast.LENGTH_SHORT).show();
                            controller.navigate(R.id.action_initaiteFragment_to_loginFragment);
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: completed");
                        }
                    });

        }

    }
}