package com.tfg_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "LoginPage.java";

    private TextView tvRegister;
    private EditText etLoginMail, etLoginPassword;
    private FirebaseAuth mAuth;
    private Button bIniciaSessio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        initializePrivateVariables();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            goToMenuPrincipalPage(user.getEmail());
        }

        tvRegister.setOnClickListener(view -> goToRegisterPage());
        bIniciaSessio.setOnClickListener(view -> {
            if (checkParameters()){
                mAuth.signInWithEmailAndPassword(etLoginMail.getText().toString(), etLoginPassword.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if ( Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).isEmailVerified() ) {
                            Log.d(TAG, "signImWithEmail:succes");
                            goToMenuPrincipalPage(etLoginMail.getText().toString());
                        } else {
                            makeToast("Email not verified!");
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        makeToast("Authentication failed");
                    }
                });
            }
        });
    }

    private void makeToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    public static boolean isValidMail(String s){
        Pattern MAIL_PATTERN
                = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return !TextUtils.isEmpty(s) && MAIL_PATTERN.matcher(s).matches();
    }

    private boolean checkParameters() {
        if (!isValidMail(etLoginMail.getText().toString())){
            makeToast("El mail: " + etLoginMail.getText().toString() +  " no té un format correcte");
            return false;
        }
        if (!isValidPassword(etLoginPassword.getText().toString())){
            makeToast("El password no té un format correcte");
            return false;
        }
        return true;
    }

    private void initializePrivateVariables() {
        mAuth = FirebaseAuth.getInstance();
        etLoginMail = findViewById(R.id.etLoginMail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        tvRegister = findViewById(R.id.tvRegisterNoAccount);
        bIniciaSessio = findViewById(R.id.bLoginIniciaSesio);
    }

    private void goToRegisterPage() {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    private void goToMenuPrincipalPage(String email) {
        Intent intent = new Intent(this, MenuPrincipal.class);
        mAuth.getCurrentUser().getEmail();
        intent.putExtra("email", email);
        startActivity(intent);
    }
}