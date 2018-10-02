package com.example.wc2015_0679.motoapp.Users;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Constants.Constant;
import com.example.wc2015_0679.motoapp.MainActivity;
import com.example.wc2015_0679.motoapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RC_SIGN_IN = 1;
    private static final int API_EXCEPTION_CODE = 12501;
    private EditText etUsername,etPassword;
    private ImageButton btnRegister;
    private Button btnLogin;
    private String username,password;
    private FirebaseAuth mAuth;
    private SignInButton btnGoogle;
    private RelativeLayout rlProgress;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        rlProgress = findViewById(R.id.rlProgress);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        btnGoogle.setSize(SignInButton.SIZE_STANDARD);

        Constant.showProgressBar(rlProgress,true);
        mAuth = FirebaseAuth.getInstance();
        verifyLogin(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("log")) getLogin();
        if (v.getTag().equals("reg")) getRegistry();
        if (v.getTag().equals("google")) getLoginWithGoogle();
    }

    // this method verify if there is a current user
    private void verifyLogin(boolean option){
        if (option){
            // if mainActivity sent me a value with key 'return' then log out
            boolean ret = getIntent().getBooleanExtra("return", false);
            if (ret){
                FirebaseAuth.getInstance().signOut();
            }else{
                verifyLogin(false);
            }
        } else {
            try {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null)
                    getMainScreen(mAuth.getCurrentUser());
                else
                    btnRegister.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Constant.showMessage("Exception", e.getMessage(), this);
            }
        }
        Constant.showProgressBar(rlProgress,false);
    }

    private void getMainScreen(FirebaseUser user){
        Constant.showProgressBar(rlProgress,true);
        startActivity(new Intent(this, MainActivity.class).putExtra("user", user));
        Constant.showProgressBar(rlProgress,false);
    }

    // this method configure login
    private void getLogin() {
        Constant.showProgressBar(rlProgress,true);
        if (validateCredentials()) {
            try {
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getMainScreen(mAuth.getCurrentUser());
                                } else {
                                    Constant.showMessage("Error","Authentication failed",LogInActivity.this);
                                }
                            }
                        });
            } catch (Exception ex) {
                Constant.showMessage("Exception", ex.getMessage(),this);
            }
        }
        Constant.showProgressBar(rlProgress,false);
    }

    // this method is username and password correctly writen
    private boolean validateCredentials(){
        boolean option = false;

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.trim().isEmpty())
            etUsername.setError("Username can not be empty!");
        else if (password.trim().isEmpty())
            etPassword.setError("Password can not be empty!");
        else
            option = true;

        return option;
    }

    // this method is to registry a new user
    private void getRegistry() {
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, RegistryActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            // Swap without transition
        }
    }

    // Login with google
    private void getLoginWithGoogle(){
        Constant.showProgressBar(rlProgress,true);
        if (isNetAvailable()) {
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else {
            Constant.showMessage("Warning", "the net is not available", this);
            Constant.showProgressBar(rlProgress,false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        Constant.showProgressBar(rlProgress,false);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            mAuth.signInWithCustomToken(Objects.requireNonNull(account.getIdToken()));
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
            verifyLogin(false);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            if (e.getStatusCode() != API_EXCEPTION_CODE)
                Constant.showMessage("Exception","Authentication failed ["+e.getStatusCode()+"]", this);
        }
    }

    private boolean isNetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }
}
