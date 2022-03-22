package com.iti.aurora.mainactivity.log.presenter;

import androidx.lifecycle.LiveData;

import com.iti.aurora.model.medicine.Medicine;

import java.util.List;

public interface LogFragmentPresenterInterface {
    void getDosesByDay(long startDate, long endDate);
    void getMedicineById(long medicineId);


}