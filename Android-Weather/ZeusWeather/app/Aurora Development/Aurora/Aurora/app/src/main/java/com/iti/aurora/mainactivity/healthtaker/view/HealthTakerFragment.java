package com.iti.aurora.mainactivity.healthtaker.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.iti.aurora.R;
import com.iti.aurora.mainactivity.healthtaker.UserMedsActivity;
import com.iti.aurora.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealthTakerFragment extends Fragment {


    RecyclerView userRecyclerView;
    FirebaseFirestore firebaseFirestore;
    List<User> usersList = new ArrayList<>();
    UserAdapter adapter;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taker, container, false);
        userRecyclerView = view.findViewById(R.id.usersRecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        adapter = new UserAdapter(usersList, new UserClickHandler() {
            @Override
            public void userItemClickHandler(User user) {
                getContext().startActivity(new Intent(getContext(), UserMedsActivity.class).putExtra("UserDocRef", user.getDocumentReference().getPath()));
            }
        });
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        userRecyclerView.setAdapter(adapter);

        firebaseFirestore.collection("users2").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> users = value.getDocuments();
                for (DocumentSnapshot user : users) {
                    usersList.add(user.toObject(User.class));
                }
                DocumentReference userRef = firebaseFirestore.collection("users2").document(firebaseAuth.getCurrentUser().getUid());
                usersList = usersList.stream().filter(user -> !user.getDocumentReference().equals(userRef))
                        .collect(Collectors.toList());
                adapter.setUserList(usersList);
                adapter.notifyDataSetChanged();

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}