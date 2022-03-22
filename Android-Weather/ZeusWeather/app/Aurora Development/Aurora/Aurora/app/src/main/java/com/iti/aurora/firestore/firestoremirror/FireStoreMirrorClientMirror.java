package com.iti.aurora.firestore.firestoremirror;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.aurora.model.User;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;
import com.iti.aurora.utils.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreMirrorClientMirror implements RemoteSourceFireStoreMirror {

    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FireStoreClient";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private CollectionReference userRef;
    private String medId;
    private static FireStoreMirrorClientMirror fireStoreClientMirror = null;
    private List<Medicine> medicineList;
    private List<Treatment> treatmentList;
    private List<Dose> doseList;
    private Medicine medicineToGet;
    private Treatment treatmentToGet;
    private Dose doseToGet;

    private FireStoreMirrorClientMirror() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userRef = firebaseFirestore.collection(Constants.FirestoreConstants.USERS);
        medicineList = new ArrayList<>();
        treatmentList = new ArrayList<>();
        doseList = new ArrayList<>();

    }

    public static FireStoreMirrorClientMirror getInstance() {
        if (fireStoreClientMirror == null) {
            fireStoreClientMirror = new FireStoreMirrorClientMirror();

        }
        return fireStoreClientMirror;
    }


    @Override
    public void putUser(User user) {
        firebaseFirestore.collection(Constants.FirestoreConstants.USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    //TODO Do something
                }).addOnFailureListener(e -> Log.d(TAG, "onFailure: Adding user failed"));
    }

    public void putMedicine(Medicine medicine) {
        medId = String.valueOf(medicine.getMedId());
        userRef.document(firebaseUser.getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(String.valueOf(medicine.getMedId()))
                .set(medicine)
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: Adding medicine failed");
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void putTreatment(Treatment treatment) {

        DocumentReference document = firebaseFirestore.collection(Constants.FirestoreConstants.USERS).document(firebaseAuth.getCurrentUser().getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE).document(medId);

        document.collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(medId)
                .set(treatment);
    }

    public void putDoses(List<Dose> doseList) {
        Map<String, Object> doseData = new HashMap<>();
        doseData.put("dose", doseList);
        DocumentReference document = firebaseFirestore.collection(Constants.FirestoreConstants.USERS).document(firebaseAuth.getCurrentUser().getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(medId);
        document.collection(Constants.FirestoreConstants.DOSE_FIRESTORE)
                .document()
                .set(doseData);
    }

    @Override
    public void putDose(Dose dose) {
        DocumentReference document = firebaseFirestore.collection(Constants.FirestoreConstants.USERS).document(firebaseAuth.getCurrentUser().getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(medId);
        document.collection(Constants.FirestoreConstants.DOSE_FIRESTORE)
                .document(String.valueOf(dose.getDoseId()))
                .set(dose);
    }


    @Override
    public void getUser() {
        firebaseFirestore.collection(Constants.FirestoreConstants.USERS)
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void getMedicine(String medId) {
        DocumentReference docRef = userRef.document(firebaseUser.getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        medicineToGet = document.toObject(Medicine.class);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void getTreatment(String treatmentId) {
        DocumentReference docRef = userRef.document(firebaseUser.getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(treatmentId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    treatmentToGet = document.toObject(Treatment.class);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void getDoseById(String doseId) {
        DocumentReference documentRef = firebaseFirestore.collection(Constants.FirestoreConstants.USERS).document(firebaseAuth.getCurrentUser().getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.DOSE_FIRESTORE)
                .document(doseId);

        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    doseToGet = document.toObject(Dose.class);
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public void getDoseByDay(Date start, Date end) {
        CollectionReference collectionReference = firebaseFirestore.collection(Constants.FirestoreConstants.USERS).document(firebaseAuth.getCurrentUser().getUid())
                .collection(Constants.FirestoreConstants.MEDICINE_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.TREATMENT_FIRESTORE)
                .document(medId)
                .collection(Constants.FirestoreConstants.DOSE_FIRESTORE);
        collectionReference.whereGreaterThanOrEqualTo("timeToTake", start)
                .whereLessThanOrEqualTo("timeToTake", end).orderBy("timeToTake");


    }

    @Override
    public void updateUserData(User user) {

    }

    @Override
    public void updateMedicine(Medicine medicine) {

    }

    @Override
    public void updateTreatment(Treatment treatment) {

    }

    @Override
    public void updateDoses(List<Dose> doseList) {

    }

    @Override
    public void updateDose(Dose dose) {

    }

    @Override
    public void removeUser(User user) {

    }

    @Override
    public void removeMedicine(Medicine medicine) {

    }

    @Override
    public void removeTreatment(Treatment treatment) {

    }

    @Override
    public void removeDoses(List<Dose> doseLists) {

    }

    public Medicine getMedicineToGet() {
        return medicineToGet;
    }

    public Treatment getTreatmentToGet() {
        return treatmentToGet;
    }

    public Dose getDoseToGet() {
        return doseToGet;
    }
}
