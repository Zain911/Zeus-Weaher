package com.iti.aurora.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iti.aurora.database.LocalSource;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class Repository implements RepositoryInterface {
    private static Repository repository = null;
    private final Context context;
    LocalSource localSource;

    public static Repository getInstance(LocalSource localSource, Context context) {
        if (repository == null) {
            repository = new Repository(localSource, context);
        }
        return repository;
    }

    private Repository(LocalSource localSource, Context context) {
        this.localSource = localSource;
        this.context = context;
    }

    @Override
    public Single<Long> insertMedicine(Medicine medicine) {
        return localSource.insertMedicine(medicine);
    }

    @Override
    public void deleteMedicine(Medicine medicine) {
        localSource.deleteMedicine(medicine);

    }

    @Override
    public void updateMedicine(Medicine medicine) {
        localSource.updateMedicine(medicine);

    }

    @Override
    public Maybe<Medicine> getSpecificMedicine(long medId) {
        return localSource.getSpecificMedicine(medId);
    }

    @Override
    public LiveData<List<Medicine>> getAllStoredMedicines() {
        return localSource.getAllStoredMedicines();
    }

    @Override
    public Single<Long> insetTreatment(Treatment treatment) {
        return localSource.insetTreatment(treatment);
    }

    @Override
    public void deleteTreatment(Treatment treatment) {
        localSource.deleteTreatment(treatment);
    }

    @Override
    public void updateTreatment(Treatment treatment) {
        localSource.updateTreatment(treatment);
    }

    @Override
    public Maybe<Treatment> getSpecificTreatment(long treatmentId) {
        return localSource.getSpecificTreatment(treatmentId);
    }

    @Override
    public LiveData<List<Treatment>> getAllStoredTreatments() {
        return localSource.getAllStoredTreatments();
    }

    @Override
    public Single<List<Treatment>> getTreatmentsMedId(long medId) {
        return localSource.getTreatmentsMedId(medId);
    }

    @Override
    public Single<Long> insertDose(Dose dose) {
        return localSource.insertDose(dose);
    }

    @Override
    public void deleteDose(Dose dose) {
        localSource.deleteDose(dose);
    }

    @Override
    public void updateDose(Dose dose) {
        localSource.updateDose(dose);
    }

    @Override
    public Maybe<Dose> getSpecificDose(int doseId) {
        return localSource.getSpecificDose(doseId);
    }

    @Override
    public LiveData<List<Dose>> getAllStoredDoses() {
        return localSource.getAllStoredDoses();
    }

    @Override
    public void insertDoses(List<Dose> doses) {
        localSource.insertDoses(doses);
    }

    @Override
    public LiveData<List<Dose>> getDosesByDay(long start, long end) {
        return localSource.getDosesByDay(start, end);
    }

    @Override
    public Single<List<Dose>> getDosesByDayOverLoad(long start, long end) {
        return localSource.getDosesByDayOverLoad(start, end);
    }

    @Override
    public Single<List<Dose>> getDosesByMedId(long medId) {
        return localSource.getDosesByMedId(medId);
    }
}