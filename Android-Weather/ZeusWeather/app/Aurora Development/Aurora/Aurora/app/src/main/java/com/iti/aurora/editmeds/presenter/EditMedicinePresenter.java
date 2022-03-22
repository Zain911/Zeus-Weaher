package com.iti.aurora.editmeds.presenter;

import android.content.Context;
import com.iti.aurora.editmeds.view.EditMedicineViewInterface;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.model.medicine.Treatment;
import com.iti.aurora.utils.selectdays.DaysOfWeek;
import com.iti.aurora.utils.selectdays.SelectDaysAlertDialog;
import com.iti.aurora.utils.workmanager.DoseAlarmManager;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditMedicinePresenter implements EditMedicinePresenterInterface {
    EditMedicineViewInterface _view;
    RepositoryInterface _repo;
    SelectDaysAlertDialog selectDaysAlertDialog;
    private List<Treatment> treatmentList;
    private List<Dose> dosesToBeDeleted;
    Medicine medicineReference;

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public EditMedicinePresenter(EditMedicineViewInterface _view, RepositoryInterface _repo) {
        this._view = _view;
        this._repo = _repo;
    }



    @Override
    public void editMedicineToDB(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected) {
        Treatment treatment = treatmentList.get(0);
        treatment.setStartDate(startDate.toDate());
        treatment.setEndDate(endDate.toDate());
        treatment.setDaysList(daysSelected);
        treatment.setRecurrency(recurrencyModel.name());
        _repo.updateTreatment(treatment);
        for(Dose dose: dosesToBeDeleted) {
            _repo.deleteDose(dose);
        }


        updateDoses(treatment,medicine.getMedId(),startDate,endDate,recurrencyModel,daysSelected);

        _repo.updateMedicine(medicine);
    }


    @Override
    public void getSelectedDaysAlertdialog(SelectDaysAlertDialog selectDaysAlertDialog) {
        this.selectDaysAlertDialog = selectDaysAlertDialog;
    }

    @Override
    public void getListOfTreatmentsFromRepo(Medicine medicine) {
        _repo.getTreatmentsMedId(medicine.getMedId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Treatment>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Treatment> treatments) {
                        treatmentList = treatments;
                        getDosesByMedId(medicine.getMedId());
                        medicineReference = medicine;
                        _view.showMedicine(medicine,treatments);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void updateDoses(Treatment treatment, long medicineId, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel,List<DaysOfWeek> daysSelected) {
        List<Dose> dosesList;
        if (recurrencyModel == RecurrencyModel.Every_day) {
            dosesList = generatePeriodicDoses(medicineId, treatment.getTreatmentId(), startDate, endDate, 24);
        } else if (recurrencyModel == RecurrencyModel.Every_2_days) {
            dosesList = generatePeriodicDoses(medicineId, treatment.getTreatmentId(), startDate, endDate, 24 * 2);
        } else if (recurrencyModel == RecurrencyModel.Every_3_days) {
            dosesList = generatePeriodicDoses(medicineId, treatment.getTreatmentId(), startDate, endDate, 24 * 3);
        } else if (recurrencyModel == RecurrencyModel.Every_28_days) {
            dosesList = generatePeriodicDoses(medicineId, treatment.getTreatmentId(), startDate, endDate, 24 * 28);
        } else if (recurrencyModel == RecurrencyModel.Two_days_a_week || recurrencyModel == RecurrencyModel.Three_days_a_week || recurrencyModel == RecurrencyModel.Five_days_a_week || recurrencyModel == RecurrencyModel.Once_a_week) {
            dosesList = generateNonPeriodicDoses(medicineId, treatment.getTreatmentId(), startDate, endDate, selectDaysAlertDialog.getSelectedDaysFromDialog());
        } else {
            dosesList = new ArrayList<>();
        }
        _repo.insertDoses(dosesList);
    }

    @Override
    public void getDosesByMedId(long medId) {
        _repo.getDosesByMedId(medId)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Dose>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Dose> doses) {
                        dosesToBeDeleted = doses;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }




    private List<Dose> generatePeriodicDoses(long medID, long treatmentId, DateTime startDate, DateTime endDate, int noOfHours) {
        List<Dose> doseList = new ArrayList<>();

        DateTime currentWhole = new DateTime(System.currentTimeMillis());
        DateTime nextDayAt12Am = new DateTime(currentWhole.getYear(), currentWhole.getMonthOfYear(), currentWhole.getDayOfMonth(), 0, 0).plusDays(1);
        if (startDate.isBefore(nextDayAt12Am)) {
            DoseAlarmManager alarmManager = new DoseAlarmManager(this.context, new Dose(medID, treatmentId, startDate.toDate()),medicineReference);
        }

        while (startDate.isBefore(endDate)) {

            Dose dose = new Dose(medID, treatmentId, startDate.toDate());
            doseList.add(dose);
            startDate = startDate.plusHours(noOfHours);
        }
        return doseList;
    }

    @androidx.annotation.NonNull
    private List<Dose> generateNonPeriodicDoses(long medID, long treatmentId, DateTime startDate, DateTime endDate, @androidx.annotation.NonNull List<DaysOfWeek> days) {
        List<Dose> doseList = new ArrayList<>();
        for (DaysOfWeek day : days) {
            DateTime nextDayDateTime = findDateOfNextDayOfWeek(startDate, day);
            doseList.addAll(generatePeriodicDoses(medID, treatmentId, nextDayDateTime, endDate, 24 * 7));
        }
        return doseList;
    }

    private DateTime findDateOfNextDayOfWeek(@androidx.annotation.NonNull DateTime startDate, @androidx.annotation.NonNull DaysOfWeek day) {
        while (!startDate.dayOfWeek().getAsText().equalsIgnoreCase(day.toString())) {
            startDate = startDate.plusHours(24);
        }
        return startDate;
    }

}
