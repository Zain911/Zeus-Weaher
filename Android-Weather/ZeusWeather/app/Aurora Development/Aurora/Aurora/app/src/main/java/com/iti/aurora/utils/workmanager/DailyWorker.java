package com.iti.aurora.utils.workmanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DailyWorker extends Worker {

    RepositoryInterface repositoryInterface;

    public DailyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        repositoryInterface = Repository.getInstance(
                ConcreteLocalSource.getInstance(context.getApplicationContext()), context.getApplicationContext()
        );
        Log.d("WORK_MANAGER", "DailyWorker: Constructor");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Result doWork() {
        Log.d("WORK_MANAGER", "doWork: DailyWorkManger");
        Log.d("WORK_MANAGER", "doWork: in daily worker");
        DateTime startDate = new DateTime(System.currentTimeMillis());

        DateTime start2 = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), 0, 0);
        DateTime endDate = new DateTime(start2.plusDays(1));

        repositoryInterface.getDosesByDayOverLoad(start2.getMillis(), endDate.getMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Dose>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Dose> doseList) {
                        for (Dose dose : doseList) {
                            getSpecificMedicnie(dose.getMedId(), dose);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
        return Result.success();


    }

    private void getSpecificMedicnie(long medId, Dose dose) {
        repositoryInterface.getSpecificMedicine(medId)
                .subscribeOn(Schedulers.io())
                .subscribe(new MaybeObserver<Medicine>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Medicine medicine) {

                        DoseAlarmManager doseAlarmManager = new DoseAlarmManager(getApplicationContext(), dose, medicine);
                        //  Log.d("WORK_MANAGER", "doWork: intializing DoesAlarmManager for " + doseModelLoop.toString());


                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}