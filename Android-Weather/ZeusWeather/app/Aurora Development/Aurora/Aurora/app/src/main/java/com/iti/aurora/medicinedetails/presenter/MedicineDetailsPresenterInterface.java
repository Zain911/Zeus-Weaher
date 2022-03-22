package com.iti.aurora.medicinedetails.presenter;

import com.iti.aurora.model.medicine.Medicine;

public interface MedicineDetailsPresenterInterface {

    void deleteMedicine(Medicine medicine);

    void getMedicine(long medId);

    void addNumberOfDosagesToMedicine(Medicine medicine, int noOfDosagesToAdd);

    void activateMedicine(Medicine medicine);

    void suspendMedicine(Medicine medicine);

}