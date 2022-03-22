package com.iti.aurora.usersystem.signup.presenter;


import com.iti.aurora.model.User;

public interface SignUpScreenPresenterInterface {

    void createNewUser(User user, String password);

    void firebaseAuthWithGoogle(String idToken);

    void checkFirebaseUser();
}