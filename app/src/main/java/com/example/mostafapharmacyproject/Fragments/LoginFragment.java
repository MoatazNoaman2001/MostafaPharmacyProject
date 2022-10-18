package com.example.mostafapharmacyproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
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

import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.Models.Pharmacist;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.databinding.FragmentLoginBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private PharmacyMVVM pharmacyMVVM;
    private NavController controller;
    private SharedPreferences preferences;
    private Long DateLong;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        preferences = requireActivity().getSharedPreferences("Customers", Context.MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(requireView());
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);

        pharmacyMVVM.getPharmacists()
                .observeOn(Schedulers.io())
                .subscribe(new Observer<List<Pharmacist>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Pharmacist> pharmacists) {
                        Log.d(TAG, "onViewCreated: " + pharmacists);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: error happened in retrieve pharmacists list: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: retrieve data completed");
                    }
                });

        binding.LoginBtn.setOnClickListener(view1 -> {
            pharmacyMVVM.LoginUser(
                    binding.EmailInputEditLayout.getEditText().getText().toString(),
                    binding.passwordInputayout.getEditText().getText().toString()
            ).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    Log.d(TAG, "onResponse: " + response.body());
                    Log.d(TAG, "onResponse: " + response.message());
                    if (response.body() != null)
                        Log.d(TAG, "onResponse: " + response.body());
                    if (response.errorBody() != null) {
                        try {
                            Log.d(TAG, "onResponse: " + response.errorBody().string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    if (response.code() == 200) {
                        Customer customer = response.body();
                        customer.setDateRegistration(Calendar.getInstance().getTime());
                        customer.setDateOfBirth(Calendar.getInstance().getTime());
                        if (customer.getAge() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                                    .setTitle("OOPS Auth Error")
                                    .setMessage("Looks Like Auth Schema changed, You Did not enter your age before \n please click ok to enter age")
                                    .setPositiveButton("OK", (dialogInterface, i) -> {
                                        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                                                .setTitleText("Select Birth Date")
                                                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                                                .build();
                                        datePicker.show(getChildFragmentManager(), "tag");
                                        datePicker.addOnPositiveButtonClickListener(selection -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                if (Duration.between(new Date(selection).toInstant(), Calendar.getInstance().toInstant()).toDays() / (360 * 18) < 0) {
                                                    new AlertDialog.Builder(requireContext())
                                                            .setTitle("Age Restricted")
                                                            .setMessage("you cant enter with age under 18")
                                                            .setPositiveButton("OK", (dialogInterface1, i2) -> {
                                                            })
                                                            .create().show();
                                                } else {
                                                    DateLong = selection;
                                                    customer.setDateOfBirth(new Date(DateLong));
                                                    pharmacyMVVM.InsertCustomer(customer);
                                                    preferences.edit().putString("cur_email", response.body().getEmail()).apply();
                                                    controller.navigate(R.id.action_loginFragment_to_mainActivity);

                                                }
                                            }

                                        });
                                    })
                                    .setOnCancelListener(dialogInterface -> new AlertDialog.Builder(requireContext())
                                            .setTitle("Age Unknown")
                                            .setMessage("you cant enter with no age")
                                            .setPositiveButton("OK", (dialogInterface1, i) -> {
                                            })
                                            .create().show());
                            builder.create().show();
                        } else {
                            pharmacyMVVM.InsertCustomer(customer);
                            preferences.edit().putString("cur_email", response.body().getEmail()).apply();
                            controller.navigate(R.id.action_loginFragment_to_mainActivity);
                        }


                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Log.d(TAG, "onFailure: " + call.request().headers().toMultimap().entrySet().toString());
                    Log.d(TAG, "onFailure: " + call.request().body().toString());
                    Toast.makeText(requireContext(), "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.signOut.setOnClickListener(view1 -> {
            controller.navigate(R.id.action_loginFragment_to_signUpFragment);
        });
    }
}