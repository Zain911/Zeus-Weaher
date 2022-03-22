package com.iti.aurora.mainactivity.home.presenter;

import com.iti.aurora.mainactivity.home.view.HomeFragmentViewInterface;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

import java.util.Date;

public class HomeFragmentPresenter implements HomeFragmentPresenterInterface {
    HomeFragmentViewInterface _view;
    RepositoryInterface _repo;

    public HomeFragmentPresenter(HomeFragmentViewInterface _view, RepositoryInterface _repo) {
        this._view = _view;
        this._repo = _repo;
    }

    @Override
    public void getLocalDoses() {
        _view.showLocalData(_repo.getAllStoredDoses());
    }

    @Override
    public void getDosesByDay(long start, long end) {
        _view.showLocalDataByDay(_repo.getDosesByDay(start, end));
    }

    @Override
    public void deleteDose(Dose dose) {
        _repo.deleteDose(dose);
    }

    @Override
    public void markDoseAsTaken(Dose dose, Medicine medicine) {
        dose.setTaken(true);
        dose.setTimeTaken(new Date(System.currentTimeMillis()));
        medicine.setDosagesLeft(medicine.getDosagesLeft() - 1);
        _repo.updateDose(dose);
        _repo.updateMedicine(medicine);
    }
}