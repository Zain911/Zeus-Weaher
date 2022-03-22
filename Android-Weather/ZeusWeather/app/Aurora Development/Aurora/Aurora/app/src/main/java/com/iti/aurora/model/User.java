package com.iti.aurora.model;



import com.google.firebase.firestore.DocumentReference;
import com.iti.aurora.model.medicine.Treatment;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    //patient
    private String username;
    private String email;
    //health takers
    private List<User> users;

    private DocumentReference documentReference;

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    //todo remove treatment list if u don't need it
    private List<Treatment> treatmentList;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, List<User> users, List<Treatment> treatmentList) {
        this.username = username;
        this.email = email;
        this.users = users;
        this.treatmentList = treatmentList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Treatment> getTreatmentList() {
        return treatmentList;
    }

    public void setTreatmentList(List<Treatment> treatmentList) {
        this.treatmentList = treatmentList;
    }
}