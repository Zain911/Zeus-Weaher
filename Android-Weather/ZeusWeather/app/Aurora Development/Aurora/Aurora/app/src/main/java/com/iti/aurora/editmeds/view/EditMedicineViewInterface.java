package com.iti.aurora.editmeds.view;

import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.RecurrencyModel;
import com.iti.aurora.model.medicine.Treatment;
import com.iti.aurora.utils.selectdays.DaysOfWeek;

import org.joda.time.DateTime;

import java.util.List;

public interface EditMedicineViewInterface {
    void editMedicine(Medicine medicine, DateTime startDate, DateTime endDate, RecurrencyModel recurrencyModel, List<DaysOfWeek> daysSelected);
    void showMedicine(Medicine medicine,List<Treatment> treatments);


}
