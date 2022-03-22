package com.iti.aurora.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.iti.aurora.model.User;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

import java.util.List;

public interface RemoteSourceFirestore {

    void putUser(User user);

    void putMedicine(Medicine medicine, List<Dose> doseList);

    User getUser(String userId);

    Medicine getMedicine(DocumentReference medReference);

    Dose getSpecificDose(DocumentReference doseReference);



}
