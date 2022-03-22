package com.iti.aurora.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iti.aurora.database.dao.DoseDAO;
import com.iti.aurora.database.dao.MedicineDAO;
import com.iti.aurora.database.dao.TreatmentDAO;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class ConcreteLocalSource implements LocalSource {

    private MedicineDAO medicineDAO;
    private DoseDAO doseDAO;
    private TreatmentDAO treatmentDAO;
    private static ConcreteLocalSource concreteLocalSource = null;
    private LiveData<List<Medicine>> storedMedicines;
    private LiveData<List<Dose>> storedDoses;
    private LiveData<List<Treatment>> storedTreatments;

    private ConcreteLocalSource(Context context) {
        AppDataBase db = AppDataBase.getInstance(context.getApplicationContext());

        medicineDAO = db.medicineDAO();
        storedMedicines = medicineDAO.getAllMedicines();

        doseDAO = db.doseDAO();
        storedDoses = doseDAO.getAllDoses();

        treatmentDAO = db.treatmentDAO();
        storedTreatments = treatmentDAO.getAllTreatments();
    }

    public static ConcreteLocalSource getInstance(Context context) {
        if(concreteLocalSource == null) {
           concreteLocalSource = new ConcreteLocalSource(context);
        }
        return concreteLocalSource;
    }


    @Override
    public @NonNull Single<Long> insertMedicine(Medicine medicine) {
        return Single.fromCallable(() -> medicineDAO.insertMedicine(medicine));
    }


    @Override
    public void deleteMedicine(Medicine medicine) {
        new Thread(() -> medicineDAO.deleteMedicine(medicine)).start();
    }

    @Override
    public void updateMedicine(Medicine medicine) {
        new Thread(() -> medicineDAO.updateMedicine(medicine)).start();
    }

    @Override
    public Maybe<Medicine> getSpecificMedicine(long medId) {
        return Maybe.fromCallable(() -> medicineDAO.getSpecificMedicine(medId));
    }

    @Override
    public LiveData<List<Medicine>> getAllStoredMedicines() {
        return storedMedicines;
    }

    @Override
    public @NonNull Single<Long> insetTreatment(Treatment treatment) {
        return Single.fromCallable(() -> treatmentDAO.insertTreatment(treatment));
    }

    @Override
    public void deleteTreatment(Treatment treatment) {
        new Thread(() -> treatmentDAO.deleteTreatment(treatment)).start();
    }

    @Override
    public void updateTreatment(Treatment treatment) {
        new Thread(() -> treatmentDAO.updateTreatment(treatment)).start();
    }

    @Override
    public Maybe<Treatment> getSpecificTreatment(long treatmentId) {
        return Maybe.fromCallable (() -> treatmentDAO.getSpecificTreatment(treatmentId));
    }

    @Override
    public LiveData<List<Treatment>> getAllStoredTreatments() {
        return storedTreatments;
    }

    @Override
    public Single<List<Treatment>> getTreatmentsMedId(long medId) {
        return Single.fromCallable(()->treatmentDAO.getTreatmentsMedId(medId));
    }

    @Override
    public Single<Long> insertDose(Dose dose) {
        return Single.fromCallable(()-> doseDAO.insertDose(dose));
    }

    @Override
    public void deleteDose(Dose dose) {
        new Thread(() -> doseDAO.deleteDose(dose)).start();
    }

    @Override
    public void updateDose(Dose dose) {
        new Thread(() -> doseDAO.updateDose(dose)).start();

    }

    @Override
    public Maybe<Dose> getSpecificDose(int doseId) {
        return Maybe.fromCallable (() -> doseDAO.getSpecificDose(doseId));
    }

    @Override
    public LiveData<List<Dose>> getAllStoredDoses() {
        return storedDoses;
    }

    @Override
    public void insertDoses(List<Dose> doses) {
        new Thread(() -> doseDAO.insertDoses(doses)).start();
    }

    @Override
    public LiveData<List<Dose>> getDosesByDay(long start, long end) {
        return doseDAO.getDosesByDay(start,end);
    }

    @Override
    public Single<List<Dose>> getDosesByDayOverLoad(long start, long end) {
        return Single.fromCallable (() -> doseDAO.getDosesByDayOverLoad(start,end));
    }

    @Override
    public Single<List<Dose>> getDosesByMedId(long medId) {
        return Single.fromCallable(() -> doseDAO.getDosesByMedId(medId));
    }
}