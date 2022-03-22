package com.iti.aurora.utils.dialogs.dosedetails;

import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

public interface DoseDialogClickListener {

    void deleteDose(Dose dose);

    void markDoseAsTaken(Dose dose, Medicine medicine);
}