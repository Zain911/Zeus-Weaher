package com.iti.aurora.usersystem.signup.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.iti.aurora.MainActivity;
import com.iti.aurora.R;
import com.iti.aurora.editmeds.view.EditMedsActivity;
import com.iti.aurora.model.User;
import com.iti.aurora.usersystem.signup.presenter.SignUpScreenPresenter;
import com.iti.aurora.usersystem.signup.presenter.SignUpScreenPresenterInterface;
import com.iti.aurora.utils.dialogs.TwoButtonsDialog;


public class SignUpActivity extends AppCompatActivity implements SignUpViewInterface {

    private static final int RC_SIGN_IN = 10;
    EditText userNameEditText, emailEditText, passwordEditText;
    Button signUpButton;
    SignInButton signInButton;
    private SignUpScreenPresenterInterface signUpScreenPresenterInterface;
    public static final String USER_KEY = "USER";
    public static final String TAG = "TAG";
    User user;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
        initUI();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    protected void onStart() {
        super.onStart();

        signUpScreenPresenterInterface.checkFirebaseUser();
    }

    void initUI() {
        userNameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signUpScreenPresenterInterface = new SignUpScreenPresenter(SignUpActivity.this, SignUpActivity.this);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signUpButton.setOnClickListener(view -> {
            user = new User(userNameEditText.getText().toString().trim(),
                    emailEditText.getText().toString().trim());

            signUpScreenPresenterInterface.createNewUser(
                    user, passwordEditText.getText().toString().trim());
        });

        signInButton.setOnClickListener(view -> {
            signIn();
        });
    }


    @Override
    public void usernameIsEmpty() {
        userNameEditText.setError("Username is required");
    }

    @Override
    public void emailIsEmpty() {
        emailEditText.setError("Email is required");


    }

    @Override
    public void passwordIsEmpty() {
        passwordEditText.setError("Password can not be empty");

    }

    @Override
    public void passwordIsShort() {
        passwordEditText.setError("Password is less than 7");

    }

    @Override
    public void authFailed() {
        Toast.makeText(this, "Something went wrong please try again later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void authSucceeded() {
        Toast.makeText(this, "Yay you are registered", Toast.LENGTH_LONG).show();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void loginSucceededWithGoogle() {
        Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFailedWithGoogle() {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                signUpScreenPresenterInterface.firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
}