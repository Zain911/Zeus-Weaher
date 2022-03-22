package com.iti.aurora.medicinedetails.presenter;

import com.iti.aurora.medicinedetails.view.MedicineDetailsViewInterface;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Medicine;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MedicineDetailsPresenter implements MedicineDetailsPresenterInterface {
    MedicineDetailsViewInterface iView;
    RepositoryInterface repositoryInterface;

    public MedicineDetailsPresenter(MedicineDetailsViewInterface iView, RepositoryInterface repositoryInterface) {
        this.iView = iView;
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public void deleteMedicine(Medicine medicine) {
        repositoryInterface.deleteMedicine(medicine);
    }

    @Override
    public void getMedicine(long medId) {
        repositoryInterface.getSpecificMedicine(medId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Medicine>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Medicine medicine) {
                iView.showMedicine(medicine);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void addNumberOfDosagesToMedicine(Medicine medicine, int noOfDosagesToAdd) {
        medicine.setDosagesLeft(medicine.getDosagesLeft() + noOfDosagesToAdd);
        repositoryInterface.updateMedicine(medicine);
    }

    @Override
    public void activateMedicine(Medicine medicine) {
        medicine.setActive(true);
        repositoryInterface.updateMedicine(medicine);
    }

    @Override
    public void suspendMedicine(Medicine medicine) {
        medicine.setActive(false);
        repositoryInterface.updateMedicine(medicine);
    }
}