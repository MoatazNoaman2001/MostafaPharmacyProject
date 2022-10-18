package com.example.mostafapharmacyproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mostafapharmacyproject.Models.Customer;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.databinding.FragmentSignUpBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";

    private FragmentSignUpBinding binding;
    private PharmacyMVVM pharmacyMVVM;
    private NavController controller;
    private Long Date;
    private SharedPreferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(getLayoutInflater());
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);
        preferences = requireActivity().getSharedPreferences("Customers", Context.MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(requireView());

        binding.DateBithInputEditTextLayout.setEndIconOnClickListener(view1 -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Birth Date")
                    .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                    .build();
            datePicker.show(getChildFragmentManager() , "tag");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Duration.between(new Date(selection).toInstant() , Calendar.getInstance().toInstant()).toDays() / (360 * 18) < 0){
                        binding.DateBithInputEditTextLayout.setErrorEnabled(true);
                        binding.DateBithInputEditTextLayout.setError("age Restricted");
                    }else{
                        Date = selection;
                    }
                }
                binding.DateBithInputEditTextLayout.getEditText().setText(
                        new SimpleDateFormat("dd/MM/yyyy").format(new Date(selection))
                );
            });
        });

        binding.SignUp.setOnClickListener(view1 -> {
            if (
                    binding.EmailInputEditTextLayout.getEditText().getText().toString().isEmpty() ||
                            binding.PasswordInputEditTextLayout.getEditText().getText().toString().isEmpty() ||
                            binding.NameInputEditTextLayout.getEditText().getText().toString().isEmpty() ||
                            binding.AddressInputEditTextLayout.getEditText().getText().toString().isEmpty() ||
                            binding.PhoneNumberInputEditTextLayout.getEditText().getText().toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "some Fields empty", Toast.LENGTH_SHORT).show();
            } else {
                if (Date == null){
                    Toast.makeText(requireContext(), "you cant sign up", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "onViewCreated: customer age : " +  Period.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(Date) , ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears());
                pharmacyMVVM.CreateNewUser(new Customer(
                        binding.EmailInputEditTextLayout.getEditText().getText().toString(),
                        binding.PasswordInputEditTextLayout.getEditText().getText().toString(),
                        binding.NameInputEditTextLayout.getEditText().getText().toString(),
                        binding.AddressInputEditTextLayout.getEditText().getText().toString(),
                        binding.PhoneNumberInputEditTextLayout.getEditText().getText().toString(),
                        Period.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(Date) , ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears(),
                        new Date(Date),
                        Calendar.getInstance().getTime()
                )).enqueue(new Callback<Customer>() {
                    @Override
                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                        Log.d(TAG, "onResponse: " + response.code());
                        Log.d(TAG, "onResponse: " + response.body());
                        Log.d(TAG, "onResponse: " + response.message());
                        Log.d(TAG, "onResponse: " + response);
                        if(response.body() != null)
                        Log.d(TAG, "onResponse: " + response.body());

                        if (response.errorBody() != null) {
                            try {
                                Log.d(TAG, "onResponse: "  + response.errorBody().string().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (response.code() == 200) {
                            Customer customer = response.body();
                            customer.setDateRegistration(Calendar.getInstance().getTime());
                            customer.setDateOfBirth(new Date(Date));

                            pharmacyMVVM.InsertCustomer(customer);
                            preferences.edit().putString("cur_email", customer.getEmail()).apply();
                            controller.navigate(R.id.action_signUpFragment_to_mainActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<Customer> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(requireContext(), "Failed to Login", Toast.LENGTH_SHORT).show();
                    }
                });
                Pattern pattern = Pattern.compile(
                        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\n"
                );
                Matcher matcher = pattern.matcher(binding.EmailInputEditTextLayout.getEditText().getText().toString());
                if (matcher.find()) {
                    Log.d(TAG, "onViewCreated: " + "passed");
                } else {
                    Log.d(TAG, "onViewCreated: " + "failed");
                }

            }

        });

    }
}