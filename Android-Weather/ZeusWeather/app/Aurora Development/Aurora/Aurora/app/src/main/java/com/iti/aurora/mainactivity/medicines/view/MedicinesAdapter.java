package com.iti.aurora.mainactivity.medicines.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.aurora.R;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.MedicineForm;

import java.util.List;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.ViewHolder> {

    List<Medicine> medicineList;
    MedicineRecyclerItemClick medicineRecyclerItemClick;

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    public MedicinesAdapter(List<Medicine> medicineList, MedicineRecyclerItemClick medicineRecyclerItemClick) {
        this.medicineList = medicineList;
        this.medicineRecyclerItemClick = medicineRecyclerItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine_in_main_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MedicineForm form = MedicineForm.valueOf(medicineList.get(position).getMedicineForm());
        holder.medicineNameTextView.setText(medicineList.get(position).getName());
        holder.medicineStrengthTextView.setText(medicineList.get(position).getNumberOfUnits() + " " + medicineList.get(position).getStrengthUnit());
        holder.medicineInstructionsTextView.setText(medicineList.get(position).getInstruction());
        if (medicineList.get(position).getMedicineForm().equalsIgnoreCase("Pills"))
            holder.medicineFormImageView.setBackgroundResource(R.drawable.ic_medicine_pill);
        else if (medicineList.get(position).getMedicineForm().equalsIgnoreCase("Injection"))
            holder.medicineFormImageView.setBackgroundResource(R.drawable.icon_vaccine);
        else if (medicineList.get(position).getMedicineForm().equalsIgnoreCase("Powder"))
            holder.medicineFormImageView.setBackgroundResource(R.drawable.icon_powder);
        else if (medicineList.get(position).getMedicineForm().equalsIgnoreCase("Drops"))
            holder.medicineFormImageView.setBackgroundResource(R.drawable.ic_dropper);
        else if (medicineList.get(position).getMedicineForm().equalsIgnoreCase("Inhaler"))
            holder.medicineFormImageView.setBackgroundResource(R.drawable.ic_inhaler);
        else
            holder.medicineFormImageView.setBackgroundResource(R.drawable.ic_inhaler);


        holder.containerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicineRecyclerItemClick.medicineItemClick(medicineList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout containerConstraintLayout;
        ImageView medicineFormImageView;
        TextView medicineNameTextView;
        TextView medicineStrengthTextView;
        TextView medicineInstructionsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerConstraintLayout = itemView.findViewById(R.id.containerConstraintLayout);
            medicineFormImageView = itemView.findViewById(R.id.medicineFormImageView);
            medicineNameTextView = itemView.findViewById(R.id.medicineNameTextView);
            medicineStrengthTextView = itemView.findViewById(R.id.medicineStrengthTextView);
            medicineInstructionsTextView = itemView.findViewById(R.id.medicineInstructionsTextView);
        }
    }

}