package com.iti.aurora.addmedicine.presenter;

import android.content.Context;

import com.iti.aurora.addmedicine.view.AddMedicineViewInterface;

import com.iti.aurora.firestore.FirestoreClient;
import com.iti.aurora.firestore.RemoteSourceFirestore;
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
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddMedicinePresenter implements AddMedicinePresenterInterface {
    AddMedicineViewInterface _view;
    RepositoryInterface _repo;
    SelectDaysAlertDialog selectDaysAlertDialog;
    Medicine medicineReference;

    RemoteSourceFirestore remoteSourceFireStore;

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public AddMedicinePresenter(AddMedicineViewInterface _view, RepositoryInterface _repo) {
        this._view = _view;
        this._repo = _repo;
        remoteSourceFireStore = FirestoreClient.getInstance();
    }


    @Override
    public void addMedicineToDB(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected) {
        _repo.insertMedicine(medicine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        medicine.setMedId(aLong);
                        medicineReference = medicine;
                        Treatment treatment = new Treatment(medicine.getMedId(), startDate.toDate(), endDate.toDate());
                        treatment.setRecurrency(recurrencyModel.name());
                        treatment.setDaysList(daysSelected);
                        insertTreatment(treatment, aLong, startDate, endDate, recurrencyModel, daysSelected);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    @Override
    public void addMedicineToDbWithDosages(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> days, int dosagesUserHave, int dosagesPerPack, int remindMeOn) {
        medicine.setDosagesLeft(dosagesUserHave);
        medicine.setDosagesPerPack(dosagesPerPack);
        medicine.setRemindMeOn(remindMeOn);
        _repo.insertMedicine(medicine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        medicine.setMedId(aLong);
                        medicineReference = medicine;

                        Treatment treatment = new Treatment(medicine.getMedId(), startDate.toDate(), endDate.toDate());
                        treatment.setRecurrency(recurrencyModel.name());
                        treatment.setDaysList(days);

                        insertTreatment(treatment, aLong, startDate, endDate, recurrencyModel, days);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    @Override
    public void setDosagesForMedicine(long medicineId, int currentDosagesNumber, int dosagesPerPack) {
        _repo.getSpecificMedicine(medicineId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new MaybeObserver<Medicine>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Medicine medicine) {
                        medicine.setDosagesLeft(currentDosagesNumber);
                        medicine.setDosagesPerPack(dosagesPerPack);
                        _repo.updateMedicine(medicine);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void insertTreatment(Treatment treatment, long medicineId, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected) {
        _repo.insetTreatment(treatment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        long treatmentId = aLong;
                        List<Dose> doses;
                        if (recurrencyModel == RecurrencyModel.Every_day) {
                            doses = generatePeriodicDoses(medicineId, treatmentId, startDate, endDate, 24);
                        } else if (recurrencyModel == RecurrencyModel.Every_2_days) {
                            doses = generatePeriodicDoses(medicineId, treatmentId, startDate, endDate, 24 * 2);
                        } else if (recurrencyModel == RecurrencyModel.Every_3_days) {
                            doses = generatePeriodicDoses(medicineId, treatmentId, startDate, endDate, 24 * 3);
                        } else if (recurrencyModel == RecurrencyModel.Every_28_days) {
                            doses = generatePeriodicDoses(medicineId, treatmentId, startDate, endDate, 24 * 28);
                        } else if (recurrencyModel == RecurrencyModel.Two_days_a_week || recurrencyModel == RecurrencyModel.Three_days_a_week || recurrencyModel == RecurrencyModel.Five_days_a_week || recurrencyModel == RecurrencyModel.Once_a_week) {
                            doses = generateNonPeriodicDoses(medicineId, treatmentId, startDate, endDate, selectDaysAlertDialog.getSelectedDaysFromDialog());
                        } else {
                            doses = new ArrayList<>();
                        }
                        setDoseIdForDoseList(doses);
                        remoteSourceFireStore.putMedicine(medicineReference, doses);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void setDoseIdForDoseList(List<Dose> doseList) {
        for (Dose dose : doseList) {
            _repo.insertDose(dose)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Long>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Long aLong) {
                            dose.setDoseId(aLong);
                            DateTime currentWhole = new DateTime(System.currentTimeMillis());
                            DateTime nextDayAt12Am = new DateTime(currentWhole.getYear(), currentWhole.getMonthOfYear(), currentWhole.getDayOfMonth(), 0, 0).plusDays(1);
                            if (new DateTime(dose.getTimeToTake()).isBefore(nextDayAt12Am)) {
                                DoseAlarmManager alarmManager = new DoseAlarmManager(context, new Dose(aLong, dose.getMedId(), dose.getTreatmentId(), dose.getTimeToTake()), medicineReference);
                            }
                            _repo.insertDose(dose);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }
    }

    @Override
    public void getSelectedDaysAlertdialog(SelectDaysAlertDialog selectDaysAlertDialog) {
        this.selectDaysAlertDialog = selectDaysAlertDialog;
    }


    private List<Dose> generatePeriodicDoses(long medID, long treatmentId, DateTime startDate, DateTime endDate, int noOfHours) {
        List<Dose> doseList = new ArrayList<>();


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