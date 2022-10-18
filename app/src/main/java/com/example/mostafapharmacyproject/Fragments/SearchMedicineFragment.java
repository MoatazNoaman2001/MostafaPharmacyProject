package com.example.mostafapharmacyproject.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.mostafapharmacyproject.Models.Medicine;
import com.example.mostafapharmacyproject.R;
import com.example.mostafapharmacyproject.adapter.MedicineRecycleAdapter;
import com.example.mostafapharmacyproject.databinding.FragmentSearchMedicineBinding;
import com.example.mostafapharmacyproject.dp.PharmacyMVVM;
import com.example.mostafapharmacyproject.dp.Relations.CategoryAndMedicineRelation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchMedicineFragment extends Fragment {
    private static final String TAG = "SearchMedicineFragment";

    private FragmentSearchMedicineBinding binding;
    private MedicineRecycleAdapter recycleAdapter;
    private NavController controller;

    private PharmacyMVVM pharmacyMVVM;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchMedicineBinding.inflate(inflater, container, false);
        pharmacyMVVM = new ViewModelProvider(this).get(PharmacyMVVM.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycleAdapter = new MedicineRecycleAdapter(requireContext());
        controller = Navigation.findNavController(requireView());
        NavigationUI.setupWithNavController(binding.toolbar, controller);

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        binding.toolbar.setNavigationIconTint(
                ResourcesCompat.getColor(getResources(), R.color.teal_700, getActivity().getTheme())
        );


        binding.MedicineList.setAdapter(recycleAdapter);
        binding.MedicineList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        new Thread() {
            @Override
            public void run() {
                super.run();

            }
        }.start();


        pharmacyMVVM.getCategory().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.getData() == null) {
                Log.d(TAG, "onViewCreated: error in retrieve category: " + listResource.getError());
            } else if (listResource.getError() != null) {
                Log.d(TAG, "onViewCreated: error in retrieve category:  " + listResource.getError());
            } else
                Log.d(TAG, "onViewCreated: category: " + listResource.getData().toString());
            pharmacyMVVM.CategoryAndMedicine("ALL").observe(getViewLifecycleOwner(), medicines -> {
                List<Medicine> list = new ArrayList<>();
                for (CategoryAndMedicineRelation cm : medicines.getData()) {
                    List<Medicine> medicineList = cm.medicines;
                    for (Medicine medicine : medicineList) {
                        list.add(medicine);
                    }
                }
                binding.autoCompleteEditText.setAdapter(new MedicineSearchAdapter(
                        requireContext(), R.layout.medicine_recycle_item, R.id.MedicineName, list
                ));
            });
        });

        recycleAdapter.submitList(new ArrayList<>());

        binding.toolbar.getMenu().findItem(R.id.Done).setIcon(
                tintDrawable(binding.toolbar.getMenu().findItem(R.id.Done).getIcon())
        );
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.Done: {
                    ArrayList<Medicine> medicines = (ArrayList<Medicine>) new ArrayList<>(recycleAdapter.getCurrentList()).clone();
                    controller.getPreviousBackStackEntry().getSavedStateHandle().set("list", medicines);
                    controller.popBackStack();
                }
            }

            return true;
        });

    }

    private Drawable tintDrawable(Drawable Drawable) {
        Drawable = DrawableCompat.wrap(Drawable);
        DrawableCompat.setTint(Drawable, ResourcesCompat.getColor(getResources(), R.color.teal_700, getActivity().getTheme()));
        return Drawable;
    }

    class MedicineSearchAdapter extends ArrayAdapter<Medicine> {
        Context context;
        int resource, textViewResourceId;
        List<Medicine> items, tempItems, suggestions;

        public MedicineSearchAdapter(Context context, int resource, int textViewResourceId, List<Medicine> items) {
            super(context, resource, textViewResourceId, items);
            this.context = context;
            this.resource = resource;
            this.textViewResourceId = textViewResourceId;
            this.items = items;
            tempItems = new ArrayList<>(items); // this makes the difference.
            suggestions = new ArrayList<>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(resource, parent, false);
            }
            Medicine medicine = items.get(position);
            if (medicine != null) {
                TextView lblName = view.findViewById(textViewResourceId);
                if (lblName != null) lblName.setText(medicine.getName());

                view.setOnClickListener(view1 -> {
                    if (recycleAdapter.getCurrentList().contains(medicine)) {
                        List<Medicine> medicines = (List<Medicine>) new ArrayList<>(recycleAdapter.getCurrentList()).clone();
                        medicines.remove(medicine);
                        recycleAdapter.submitList(medicines);
                    } else {
                        List<Medicine> medicines = (List<Medicine>) new ArrayList<>(recycleAdapter.getCurrentList()).clone();
                        medicines.add(medicine);
                        recycleAdapter.submitList(medicines);
                    }
                });

            }

            return view;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((Medicine) resultValue).getName();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    ArrayList<Medicine> medicines = new ArrayList<>();
                    for (Medicine people : tempItems) {
                        if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            medicines.add(people);
                        }
                    }
                    suggestions.addAll(medicines);
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Medicine> filterList = (ArrayList<Medicine>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Medicine medicine : filterList) {
                        add(medicine);
                        notifyDataSetChanged();
                    }
                }
            }
        };


    }

}