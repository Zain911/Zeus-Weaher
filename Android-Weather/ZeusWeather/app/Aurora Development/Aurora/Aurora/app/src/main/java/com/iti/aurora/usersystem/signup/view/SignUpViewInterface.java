package com.iti.aurora.usersystem.signup.view;

public interface SignUpViewInterface {

    void usernameIsEmpty();

    void emailIsEmpty();

    void passwordIsEmpty();

    void passwordIsShort();

    void authFailed();

    void authSucceeded();

    void loginSucceededWithGoogle();

    void loginFailedWithGoogle();

}