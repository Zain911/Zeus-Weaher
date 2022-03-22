package com.iti.aurora.addmedicine.presenter;

import android.content.Context;

import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.utils.selectdays.DaysOfWeek;
import com.iti.aurora.utils.selectdays.SelectDaysAlertDialog;

import org.joda.time.DateTime;

import java.util.List;

public interface AddMedicinePresenterInterface {

    void addMedicineToDB(
            Medicine medicine,
            DateTime startDate, DateTime endDate,
            RecurrencyModel recurrencyModel,
            List<DaysOfWeek> daysSelected);

    void addMedicineToDbWithDosages(
            Medicine medicine,
            DateTime startDate, DateTime endDate,
            RecurrencyModel recurrencyModel,
            List<DaysOfWeek> days,
            int dosagesUserHave, int dosagesPerPack , int remindMeOn);

    void getSelectedDaysAlertdialog(SelectDaysAlertDialog selectDaysAlertDialog);

    void setDosagesForMedicine(long medicineId, int currentDosagesNumber, int dosagesPerPack);

    //for using alarm manager only
    void setContext(Context context);
}