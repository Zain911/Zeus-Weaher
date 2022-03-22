package com.iti.aurora.editmeds.presenter;

import android.content.Context;

import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.model.medicine.Treatment;
import com.iti.aurora.utils.selectdays.DaysOfWeek;
import com.iti.aurora.utils.selectdays.SelectDaysAlertDialog;

import org.joda.time.DateTime;

import java.util.List;

public interface EditMedicinePresenterInterface {
    void editMedicineToDB(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected);

    void getSelectedDaysAlertdialog(SelectDaysAlertDialog selectDaysAlertDialog);
    void getListOfTreatmentsFromRepo(Medicine medicine);
    void getDosesByMedId(long medId);
    //for using alarm manager only
    void setContext(Context context);
}
