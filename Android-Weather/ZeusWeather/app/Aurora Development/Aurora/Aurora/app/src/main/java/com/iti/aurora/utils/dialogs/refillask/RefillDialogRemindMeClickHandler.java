package com.iti.aurora.utils.dialogs.refillask;

public interface RefillDialogRemindMeClickHandler {

    void addRefillReminderToMedicine(long medicineId, int noOfDosages, int numberOfDosagesPerPack, int remindMeOnNumber);

    void addMedicineWithoutReminder();

}