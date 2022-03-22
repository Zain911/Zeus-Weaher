package com.iti.aurora.utils.dialogs.dosedetails;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.iti.aurora.R;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.notification.NotificationCenter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.MessageFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DoseDetailsDialog extends Dialog {

    Dose dose;
    Medicine medicine;

    DoseDialogClickListener listener;

    TextView medicineNameTextView;
    TextView doseTimeTextView;
    TextView medicineStrengthAndDosageTextView;
    TextView medicineInstructionsTextView;

    ImageButton dismissDialogImageButton;
    ImageButton takeDosageImageButton;

    ImageView deleteDoseImageView;

    RepositoryInterface repositoryInterface;

    public DoseDetailsDialog(@NonNull Context context, Dose dose, DoseDialogClickListener clickListener, RepositoryInterface repositoryInterface) {
        super(context);
        listener = clickListener;
        this.dose = dose;
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_dose_details);

        medicineNameTextView = findViewById(R.id.medicineNameTextView);
        doseTimeTextView = findViewById(R.id.doseTimeTextView);
        medicineStrengthAndDosageTextView = findViewById(R.id.strengthAndDosageTextView);
        medicineInstructionsTextView = findViewById(R.id.medicineInstructionsTextView);

        deleteDoseImageView = findViewById(R.id.deleteDoseImageView);

        dismissDialogImageButton = findViewById(R.id.dismissDialogImageButton);
        takeDosageImageButton = findViewById(R.id.takeDoseImageButton);

        dismissDialogImageButton.setOnClickListener(view -> this.dismiss());

        takeDosageImageButton.setOnClickListener(view -> {
            listener.markDoseAsTaken(dose, medicine);
            if (medicine.getDosagesLeft() <= medicine.getRemindMeOn())
                NotificationCenter.showRefill(getContext(), medicine);
            this.dismiss();
        });

        deleteDoseImageView.setOnClickListener(view -> {
            listener.deleteDose(dose);
            this.dismiss();
        });

        if (dose.getTaken() == null || !dose.getTaken()) {
            takeDosageImageButton.setEnabled(true);
            takeDosageImageButton.setAlpha(1.0f);
        } else {
            takeDosageImageButton.setEnabled(false);
            takeDosageImageButton.setAlpha(0.1f);
        }

        repositoryInterface.getSpecificMedicine(dose.getMedId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Medicine>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Medicine medicine) {
                        DoseDetailsDialog.this.medicine = medicine;
                        medicineNameTextView.setText(medicine.getName());
                        medicineStrengthAndDosageTextView.setText(MessageFormat.format("{0} {1} of {2}", medicine.getNumberOfUnits(), medicine.getStrengthUnit().toString(), medicine.getMedicineForm()));
                        medicineInstructionsTextView.setText(medicine.getInstruction());
                        doseTimeTextView.setText(MessageFormat.format("{0}{1}", getContext().getResources().getString(R.string.scheduledFor), new DateTime(dose.getTimeToTake()).toString(DateTimeFormat.forPattern("hh:mm a"))));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}