package com.iti.aurora.mainactivity.log.view;

import androidx.lifecycle.LiveData;

import com.iti.aurora.model.medicine.Dose;

import java.util.List;

public interface LogFragmentViewInterface {
    void showDosesListByDay(LiveData<List<Dose>> doseListLiveData);
}
