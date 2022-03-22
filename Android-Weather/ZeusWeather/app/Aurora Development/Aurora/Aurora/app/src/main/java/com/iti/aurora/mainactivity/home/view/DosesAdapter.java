package com.iti.aurora.mainactivity.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.aurora.R;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.MessageFormat;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DosesAdapter extends RecyclerView.Adapter<DosesAdapter.ViewHolder> {

    List<Dose> doseList;
    Context context;
    RepositoryInterface repositoryInterface;
    DosesRecyclerItemClick dosesRecyclerItemClick;

    public void setDoseList(List<Dose> doseList) {
        this.doseList = doseList;
    }

    public DosesAdapter(List<Dose> medList, Context context, RepositoryInterface repositoryInterface, DosesRecyclerItemClick dosesRecyclerItemClick) {
        this.doseList = medList;
        this.context = context;
        this.repositoryInterface = repositoryInterface;
        this.dosesRecyclerItemClick = dosesRecyclerItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dose_in_main_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        repositoryInterface.getSpecificMedicine(doseList.get(position).getMedId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Medicine>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Medicine medicine) {
                        holder.medicationNameTextView.setText(medicine.getName());
                        holder.medicationDosageTextView.setText(MessageFormat.format("{0} {1} of {2}", medicine.getNumberOfUnits(), medicine.getMedicineForm(), medicine.getStrengthUnit()));
                        if (medicine.getMedicineForm().equalsIgnoreCase("Pills"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.ic_medicine_pill);
                        else if (medicine.getMedicineForm().equalsIgnoreCase("Injection"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.icon_vaccine);
                        else if (medicine.getMedicineForm().equalsIgnoreCase("Powder"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.icon_powder);
                        else if (medicine.getMedicineForm().equalsIgnoreCase("Drops"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.ic_dropper);
                        else if (medicine.getMedicineForm().equalsIgnoreCase("Inhaler"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.ic_inhaler);
                        else if (medicine.getMedicineForm().equalsIgnoreCase("Solution"))
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.ic_solution);
                        else
                            holder.notificationIconImageView.setBackgroundResource(R.drawable.ic_medicine_pill);


                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        DateTime date = new DateTime(doseList.get(position).getTimeToTake());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM");
        DateTimeFormatter fmt2 = DateTimeFormat.forPattern("hh:mm a");
        String dateString = date.toString(fmt);
        String timeString = date.toString(fmt2);
        holder.medicationTimeTextView.setText(MessageFormat.format("{0} ", timeString));
        holder.containerRelativeLayout.setOnClickListener(view -> dosesRecyclerItemClick.showDoseDialog(doseList.get(position)));
    }

    @Override
    public int getItemCount() {
        return doseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout containerRelativeLayout;
        ImageView notificationIconImageView;
        TextView medicationTimeTextView;
        TextView medicationNameTextView;
        TextView medicationDosageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerRelativeLayout = itemView.findViewById(R.id.containerRelativeLayout);
            notificationIconImageView = itemView.findViewById(R.id.notificationIconImageView);
            medicationTimeTextView = itemView.findViewById(R.id.medicationTimeTextView);
            medicationNameTextView = itemView.findViewById(R.id.medicationNameTextView);
            medicationDosageTextView = itemView.findViewById(R.id.medicationDosageTextView);
        }
    }
}