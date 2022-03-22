package com.iti.aurora.model;

import androidx.lifecycle.LiveData;

import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface RepositoryInterface {

    @NonNull Single<Long> insertMedicine(Medicine medicine);
    void deleteMedicine(Medicine medicine);
    void updateMedicine(Medicine medicine);
    @NonNull Maybe<Medicine> getSpecificMedicine(long medId);
    LiveData<List<Medicine>> getAllStoredMedicines();

    @NonNull Single<Long> insetTreatment(Treatment treatment);
    void deleteTreatment(Treatment treatment);
    void updateTreatment(Treatment treatment);
    @NonNull Maybe<Treatment> getSpecificTreatment(long treatmentId);
    LiveData<List<Treatment>> getAllStoredTreatments();
    Single<List<Treatment>> getTreatmentsMedId(long medId);

    Single<Long> insertDose(Dose dose);
    void deleteDose(Dose dose);
    void updateDose(Dose dose);
    @NonNull Maybe<Dose> getSpecificDose(int doseId);
    LiveData<List<Dose>> getAllStoredDoses();
    void insertDoses(List<Dose> doses);
    LiveData<List<Dose>> getDosesByDay(long start, long end);
    Single<List<Dose>> getDosesByDayOverLoad(long start,long end);
    Single<List<Dose>> getDosesByMedId(long medId);
}