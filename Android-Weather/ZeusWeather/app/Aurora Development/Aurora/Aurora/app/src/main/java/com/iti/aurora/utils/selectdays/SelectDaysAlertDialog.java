package com.iti.aurora.utils.selectdays;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectDaysAlertDialog {

    Context context;
    IUpdateText iUpdateText;

    int daysCountMax;
    int currentCount = 0;

    public SelectDaysAlertDialog(Context context, IUpdateText iUpdateText) {
        this.context = context;
        this.iUpdateText = iUpdateText;
    }

    public SelectDaysAlertDialog(Context context, IUpdateText iUpdateText, int daysCount) {
        this.context = context;
        this.iUpdateText = iUpdateText;
        this.daysCountMax = daysCount;
    }

    private final static String[] days = {"Saturday",
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday"};

    private boolean[] selectedDays = new boolean[days.length];
    private ArrayList<Integer> dayList = new ArrayList<>();

    public void showSelectDaysDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Days");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(days, selectedDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    dayList.add(which);
                    Collections.sort(dayList);
                    currentCount++;
                    if (currentCount >= daysCountMax)
                        dialog.dismiss();
                } else {
                    currentCount--;
                    ArrayList<Integer> dayListDub = new ArrayList<>(dayList);
                    for (Integer day : dayList) {
                        if (day == which) {
                            dayListDub.remove(day);
                        }
                    }
                    dayList = new ArrayList<>(dayListDub);
                }
                setSelectedDaysTextView();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int x = 0; x < dayList.size(); x++) {
                    stringBuilder.append(days[dayList.get(x)]);

                    if (x != dayList.size() - 1) {
                        stringBuilder.append(" ,");
                    }
                    //selectedDaysTextView.setText(stringBuilder.toString());
                }
            }
        });


        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < selectedDays.length; j++) {
                    selectedDays[j] = false;
                }
                dayList.clear();
                setSelectedDaysTextView();
            }
        });
        builder.show();
    }

    private void setSelectedDaysTextView() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int x = 0; x < dayList.size(); x++) {
            stringBuilder.append(days[dayList.get(x)]);
            if (x != dayList.size() - 1) {
                stringBuilder.append(" ,");
            }
        }
        iUpdateText.updateText(stringBuilder.toString());
    }

    public List<DaysOfWeek> getSelectedDaysFromDialog() {
        List<DaysOfWeek> selectedDaysList = new ArrayList<>();
        for (int i = 0; i < dayList.size(); i++) {
            selectedDaysList.add(DaysOfWeek.valueOf(days[dayList.get(i)]));
        }
        return selectedDaysList;
    }

    public void setSelectedDays(List<DaysOfWeek> selectedDaysList) {
        for (DaysOfWeek day : selectedDaysList) {
            for (int i = 0; i < days.length; i++) {
                if (day.toString().equalsIgnoreCase(days[i])) {
                    selectedDays[i] = true;
                    dayList.add(i);
                }
            }
        }
    }

}