package com.iti.aurora.mainactivity.log.presenter;

import androidx.lifecycle.LiveData;

import com.iti.aurora.mainactivity.log.view.LogFragmentViewInterface;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;

import java.util.List;

public class LogFragmentPresenter implements LogFragmentPresenterInterface {

    LogFragmentViewInterface logFragmentViewInterface;
    RepositoryInterface repositoryInterface;

    public LogFragmentPresenter(LogFragmentViewInterface logFragmentViewInterface, RepositoryInterface repositoryInterface) {
        this.logFragmentViewInterface = logFragmentViewInterface;
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public void getDosesByDay(long startDate, long endDate) {
        LiveData<List<Dose>> doses = repositoryInterface.getDosesByDay(startDate, endDate);
        logFragmentViewInterface.showDosesListByDay(doses);
    }

    @Override
    public void getMedicineById(long medicineId) {

    }
}
