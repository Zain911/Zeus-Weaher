package com.iti.aurora.mainactivity.medicines.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.aurora.R;
import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.medicinedetails.view.MedicineDetailsActivity;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MedicinesFragment extends Fragment {
    Context context;

    RecyclerView medicinesRecyclerView;
    RepositoryInterface repositoryInterface;
    MedicinesAdapter adapter;
    List<Medicine> medsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        context = getContext();

        medsList = new ArrayList<>();
        medicinesRecyclerView = view.findViewById(R.id.medicinesRecyclerView);
        repositoryInterface = Repository.getInstance(ConcreteLocalSource.getInstance(context), context);

        adapter = new MedicinesAdapter(medsList, medicine -> {
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra(Constants.MEDICINE_PASSING_FLAG, medicine);
            startActivity(intent);
        });

        medicinesRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        medicinesRecyclerView.setAdapter(adapter);
        LiveData<List<Medicine>> meds = repositoryInterface.getAllStoredMedicines();
        meds.observe(getActivity(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                adapter.setMedicineList(medicines);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}