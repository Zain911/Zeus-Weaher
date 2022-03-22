package com.iti.aurora.firestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.iti.aurora.model.User;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FirestoreClient implements RemoteSourceFirestore {

    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FireStoreClient";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static FirestoreClient firestoreClient = null;


    private FirestoreClient() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public static FirestoreClient getInstance() {
        if (firestoreClient == null) {
            firestoreClient = new FirestoreClient();

        }
        return firestoreClient;
    }

    @Override
    public void putUser(User user) {
        firebaseFirestore.collection("users2")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnFailureListener(e -> Log.d(TAG, "onFailure: Adding user failed"));
    }

    @Override
    public void putMedicine(Medicine medicine, List<Dose> doseList) {
        DocumentReference userReference = firebaseFirestore.collection("users2")
                .document(firebaseUser.getUid());

        medicine.setUser(userReference.getPath());
        firebaseFirestore.collection("medicines")
                .add(medicine).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println(documentReference.getId());
                DocumentReference medicineReference = firebaseFirestore.collection("medicines")
                        .document(documentReference.getId());
                for (Dose dose : doseList) {
                    dose.setMedicine(medicineReference.getPath());
                    dose.setUser(userReference.getPath());
                    firebaseFirestore.collection("doses").add(dose).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            getSpecificDose(documentReference);
                        }
                    });
                }
                getMedicine(medicineReference);
            }
        });

    }

    @Override
    public User getUser(String userId) {
        final DocumentReference docRef = firebaseFirestore.collection("users2")
                .document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.toObject(User.class);
                        Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return new User();
    }

    @Override
    public Medicine getMedicine(DocumentReference medReference) {
        final DocumentReference docRef = firebaseFirestore.collection("medicines")
                .document(medReference.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.toObject(Medicine.class);
                        Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        return null;
    }

    @Override
    public Dose getSpecificDose(DocumentReference doseReference) {
        final DocumentReference docRef = firebaseFirestore.collection("doses")
                .document(doseReference.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.toObject(Dose.class);
                        Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return null;
    }


}