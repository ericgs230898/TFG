package com.tfg_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {

    private ImageButton backButton;
    private TextView tvHaveAccount;
    private EditText etUsername, etMail, etPassword1, etPassword2;
    private Button bRegister;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        initializeVariables();

        tvHaveAccount.setOnClickListener(view -> goToLoginPage());
        backButton.setOnClickListener(view -> goToLoginPage());

        bRegister.setOnClickListener(view -> {
            if ( checkParameters() ) {
                mAuth.createUserWithEmailAndPassword(etMail.getText().toString(),
                        etPassword1.getText().toString()).addOnCompleteListener(task -> {
                            if ( task.isSuccessful() ) {
                                task.getResult().getUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){
                                        Log.d("RGSR", "Email sent");
                                        registerUserInDataBase();
                                    }
                                });
                                Log.d("RGSR","Register Successful");
                                goToLoginPage();
                            }
                            else {
                                Log.w("RGSR", "CreateUserWithEmail:failure",task.getException());
                            }
                });
            }
        });
    }

    private void registerUserInDataBase() {
        String usuari = etUsername.getText().toString();
        String mail = etMail.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> userMap = new HashMap<>();
        userMap.put(usuari, mail);
        db.collection("Usuari").document().set(userMap).addOnCompleteListener(task -> Log.d("TAG", "completeInsertUsuari"));
    }

    private void initializeVariables(){
        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        etUsername = findViewById(R.id.etRegisterUsername);
        etMail = findViewById(R.id.etRegisterMail);
        etPassword1 = findViewById(R.id.etRegisterPassword);
        etPassword2 = findViewById(R.id.etRegisterPassword2);
        backButton = findViewById(R.id.backButton);
        bRegister = findViewById(R.id.bRegister);
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean checkParameters() {
        if (TextUtils.isEmpty(etUsername.getText().toString()) ||
                TextUtils.isEmpty(etMail.getText().toString()) ||
                TextUtils.isEmpty(etPassword1.getText().toString()) ||
                TextUtils.isEmpty(etPassword2.getText().toString())){
            makeToast("Alguno de los campos no está rellenado");
            return false;
        }
        else if ( !etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
            makeToast("Las contraseñas no coinciden");
            return false;
        } else if ( !isValidPassword(etPassword1.getText().toString())){
            makeToast("La contraseña no concuerda con el formato");
            return false;
        } else if ( !isValidMail(etMail.getText().toString())){
            makeToast("El mail no concuerda con el formato");
            return false;
        } else if (!isValidUsername(etUsername.getText().toString())){
            makeToast("El username no concuerda con el formato");
            return false;
        }
        return true;
    }

    public static boolean isValidUsername(String s){
        Pattern USERNAME_PATTERN
                = Pattern.compile(
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
        return !TextUtils.isEmpty(s) && USERNAME_PATTERN.matcher(s).matches();
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

    private void goToLoginPage(){
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(intent);
    }

    private void makeToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }
}