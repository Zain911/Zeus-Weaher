package com.iti.aurora.mainactivity.home.presenter;

import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

public interface HomeFragmentPresenterInterface {
    void getLocalDoses();

    void getDosesByDay(long start, long end);

    void deleteDose(Dose dose);

    void markDoseAsTaken(Dose dose , Medicine medicine);
}