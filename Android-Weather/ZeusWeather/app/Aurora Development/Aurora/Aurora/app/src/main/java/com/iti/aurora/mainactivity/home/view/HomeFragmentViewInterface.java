package com.iti.aurora.mainactivity.home.view;

import androidx.lifecycle.LiveData;

import com.iti.aurora.model.medicine.Dose;

import java.util.List;

public interface HomeFragmentViewInterface {
    void showLocalData(LiveData<List<Dose>> doses);
    void showLocalDataByDay(LiveData<List<Dose>> doseList);
}