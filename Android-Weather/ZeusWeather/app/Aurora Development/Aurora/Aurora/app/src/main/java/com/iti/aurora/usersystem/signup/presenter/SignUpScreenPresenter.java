package com.iti.aurora.usersystem.signup.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.aurora.MainActivity;
import com.iti.aurora.R;
import com.iti.aurora.usersystem.signup.view.SignUpViewInterface;
import com.iti.aurora.model.User;
import com.iti.aurora.utils.Constants;
import com.iti.aurora.utils.spfactory.SharedPreferencesFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpScreenPresenter implements SignUpScreenPresenterInterface {

    SignUpViewInterface _view;
    Activity activity;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    private CollectionReference userRef;
    private static final String TAG = "SignUpScreenPresenter";

    public SignUpScreenPresenter(SignUpViewInterface _view, Activity activity) {
        this._view = _view;
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        userRef = fireStore.collection(Constants.FirestoreConstants.USERS2);
    }


    @Override
    public void createNewUser(User user, String password) {
        if (TextUtils.isEmpty(user.getEmail())) {
            _view.emailIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            _view.passwordIsEmpty();
            return;
        }

        if (password.length() < 6) {
            _view.passwordIsShort();
            return;
        }

        if (TextUtils.isEmpty(user.getUsername())) {
            _view.usernameIsEmpty();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            _view.authFailed();
                        } else {
                            Intent intent = new Intent(activity, MainActivity.class);
                            user.setDocumentReference(userRef.document(firebaseAuth.getCurrentUser().getUid()));
                            CollectionReference reference = fireStore.collection(Constants.FirestoreConstants.USERS2);
                            reference.document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        _view.authSucceeded();
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Register Error is :" + e.getMessage());
                                    // _view.authFailed();
                                }
                            });
                            activity.startActivity(intent);
                            (activity).finish();
                        }
                    }
                });

    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        _view.loginFailedWithGoogle();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                Intent intent = new Intent(activity, MainActivity.class);
                if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                    Log.d("TAG", "Account created");
                    assert firebaseUser != null;
                    User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
                    user.setDocumentReference(userRef.document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()));
                    userRef.document(firebaseAuth.getCurrentUser().getUid()).set(user)
                            .addOnSuccessListener(aVoid -> {
                                _view.loginSucceededWithGoogle();
                            }).addOnFailureListener(e -> {
                        Log.d("TAG", "Register Error is :" + e.getMessage());
                        _view.loginFailedWithGoogle();
                    });
                } else {
                    Log.i("TAG", "Already Existed");
                }
                activity.startActivity(intent);
                activity.finish();

            }
        });

    }


    @Override
    public void checkFirebaseUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }


}