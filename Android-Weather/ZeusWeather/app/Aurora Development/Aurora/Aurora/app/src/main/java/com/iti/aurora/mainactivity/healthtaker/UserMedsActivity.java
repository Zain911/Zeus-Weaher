package com.iti.aurora.mainactivity.healthtaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iti.aurora.R;
import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.mainactivity.log.view.LogDosesAdapter;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserMedsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    List<Medicine> medicineList = new ArrayList<>();
    List<Dose> doseList = new ArrayList<>();

    UserMedsAdapter userMedsAdapter;
    RecyclerView userMedsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meds);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userMedsRecyclerView = findViewById(R.id.userMedsRecyclerview);
        String docRef = "/" + getIntent().getStringExtra("UserDocRef");
        userMedsAdapter = new UserMedsAdapter(UserMedsActivity.this, new ArrayList<>());
        userMedsRecyclerView.setLayoutManager(new LinearLayoutManager(UserMedsActivity.this, RecyclerView.VERTICAL, false));
        userMedsRecyclerView.setAdapter(userMedsAdapter);

        DocumentReference documentReference = firebaseFirestore.document(docRef);

        firebaseFirestore.collection("doses").whereEqualTo("user", "/" + documentReference.getPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        doseList.add(doc.toObject(Dose.class));
                    }
                    doseList = doseList.stream()
                            .filter(dose -> new DateTime(dose.getTimeToTake()).isAfter(System.currentTimeMillis()))
                            .sorted(Comparator.comparing(Dose::getTimeToTake))
                            .collect(Collectors.toList());

                    userMedsAdapter.setDoseList(doseList);
                    userMedsAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}