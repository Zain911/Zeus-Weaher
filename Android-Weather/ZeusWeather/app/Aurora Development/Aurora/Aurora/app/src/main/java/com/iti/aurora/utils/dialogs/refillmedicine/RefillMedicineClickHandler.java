package com.iti.aurora.utils.dialogs.refillmedicine;

import com.iti.aurora.model.medicine.Medicine;

public interface RefillMedicineClickHandler {

    void refillMedicine(Medicine medicineId, int NoOfDosagesToAdd);

}