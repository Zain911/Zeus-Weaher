package com.iti.aurora.mainactivity.log.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.aurora.R;
import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.mainactivity.log.presenter.LogFragmentPresenter;
import com.iti.aurora.mainactivity.log.presenter.LogFragmentPresenterInterface;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.usersystem.signup.view.SignUpActivity;
import com.iti.aurora.utils.Constants;
import com.iti.aurora.utils.spfactory.SharedPreferencesFactory;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class LogFragment extends Fragment implements LogFragmentViewInterface {
    CalendarView logCalendarView;
    LogFragmentPresenterInterface logFragmentPresenterInterface;
    RecyclerView recyclerView;
    LogDosesAdapter logDosesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        recyclerView = view.findViewById(R.id.dosesLogRecyclerView);
        logDosesAdapter = new LogDosesAdapter(getContext(), new ArrayList<>(),
                Repository.getInstance(ConcreteLocalSource.getInstance(getContext()), getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(logDosesAdapter);
        logFragmentPresenterInterface = new LogFragmentPresenter(this,
                Repository.getInstance(ConcreteLocalSource.getInstance(getContext()), getContext()));


        logCalendarView = view.findViewById(R.id.logCalendarView);
        logCalendarView.setMaxDate(System.currentTimeMillis());
        DateTime now = new DateTime(System.currentTimeMillis());

        DateTime startDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0);
        DateTime endDate = new DateTime(startDate.plusDays(1));

        logFragmentPresenterInterface.getDosesByDay(startDate.getMillis(), endDate.getMillis());
        logCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                logFragmentPresenterInterface.getDosesByDay(new DateTime(year, month + 1, day, 0, 0).getMillis()
                        , new DateTime(year, month + 1, day, 0, 0).plusDays(1).getMillis());
            }
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showDosesListByDay(LiveData<List<Dose>> doseListLiveData) {
        doseListLiveData.observe(this, doses -> {
            logDosesAdapter.setDoseList(doses);
            logDosesAdapter.notifyDataSetChanged();
        });
    }
}