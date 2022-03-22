package com.iti.aurora.mainactivity.log.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.aurora.R;
import com.iti.aurora.mainactivity.home.view.DosesAdapter;
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

public class LogDosesAdapter extends RecyclerView.Adapter<LogDosesAdapter.ViewHolder> {
    Context context;
    List<Dose> doseList;
    RepositoryInterface repositoryInterface;

    public LogDosesAdapter(Context context, List<Dose> doseList, RepositoryInterface repositoryInterface) {
        this.context = context;
        this.doseList = doseList;
        this.repositoryInterface = repositoryInterface;
    }

    public void setDoseList(List<Dose> doseList) {
        this.doseList = doseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dose_log_fragment, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateTimeTitleTextView.setText(new DateTime(doseList.get(position).getTimeToTake()).toString(DateTimeFormat.forPattern("d MMMM hh:mm a")));
        if (doseList.get(position).getTaken() == null || !doseList.get(position).getTaken()) {
            holder.isTakenTextView.setText(context.getResources().getString(R.string.missed));
            holder.isTakenImageView.setImageResource(R.drawable.missed_icon);
        } else {
            holder.isTakenTextView.setText(context.getResources().getString(R.string.taken));
            holder.isTakenImageView.setImageResource(R.drawable.ic_taken_done);
        }
        repositoryInterface.getSpecificMedicine(doseList.get(position).getMedId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Medicine>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Medicine medicine) {
                        holder.nameMedicineTextView.setText(medicine.getName());
                        holder.reasonMedicnieTextView.setText(medicine.getReasonOfTaking());
                        holder.strenghtTextView.setText(MessageFormat.format("{0} {1}", medicine.getNumberOfUnits(), medicine.getStrengthUnit().toString()));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return doseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTitleTextView;
        TextView nameMedicineTextView;
        TextView isTakenTextView;
        ImageView isTakenImageView;
        TextView strenghtTextView;
        TextView reasonMedicnieTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeTitleTextView = itemView.findViewById(R.id.dateTimeTitleTextView);
            nameMedicineTextView = itemView.findViewById(R.id.nameMedicineTextView);
            isTakenTextView = itemView.findViewById(R.id.isTakenTextView);
            isTakenImageView = itemView.findViewById(R.id.isTakenImageView);
            strenghtTextView = itemView.findViewById(R.id.strenghtTextView);
            reasonMedicnieTextView = itemView.findViewById(R.id.reasonMedicnieTextView);

        }
    }
}
