package com.tfg_project.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.clases.LoadingDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {

    private static final String TAG = "LoginPage.java";

    private TextView tvRegister;
    private EditText etLoginMail, etLoginPassword;
    private FirebaseAuth mAuth;
    private Button bIniciaSessio;
    private TextView forgotPassword;
    private AlertDialog alertDialogForgotPassword;

    private static List<String> competicions = Arrays.asList("primera-catalana", "segona-catalana", "tercera-catalana", "quarta-catalana");
    private static List<String> jornades = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40");

    private static List<String> grups = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        initializePrivateVariables();

        forgotPassword.setOnClickListener(view -> showAlertDialog());

        Activity actualActivity = this;

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            if ( "eric.gonzalez.sole@estudiant.upc.edu".equals(user.getEmail())){
                goToAdminPage();
            } else {
                final LoadingDialog loadingDialog = new LoadingDialog(actualActivity);
                loadingDialog.startLoadingDialog();
                FirebaseFirestore.getInstance().collection("Usuari").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String usuari = (String) task.getResult().get("usuari");
                        loadingDialog.dissmisDialog();
                        goToMenuPrincipalPage(user.getEmail(), usuari);
                    }
                });
            }

        }

        tvRegister.setOnClickListener(view -> goToRegisterPage());
        bIniciaSessio.setOnClickListener(view -> {
            if (checkParameters()){
                final LoadingDialog loadingDialog = new LoadingDialog(actualActivity);
                loadingDialog.startLoadingDialog();
                mAuth.signInWithEmailAndPassword(etLoginMail.getText().toString(), etLoginPassword.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if ( Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).isEmailVerified() ) {
                            Log.d(TAG, "signImWithEmail:succes");
                            FirebaseFirestore.getInstance().collection("Usuari").document(etLoginMail.getText().toString()).get().addOnCompleteListener(task1 -> {
                                String usuari = (String) task1.getResult().get("usuari");
                                loadingDialog.dissmisDialog();
                                goToMenuPrincipalPage(etLoginMail.getText().toString(), usuari);
                            });
                        } else {
                            loadingDialog.dissmisDialog();
                            makeToast("Email not verified!");

                        }
                    } else {
                        loadingDialog.dissmisDialog();
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        makeToast("Authentication failed");
                    }
                });
            }
        });
    }

    private void goToAdminPage() {
        Intent intent = new Intent(this, AdminPage.class);
        startActivity(intent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contrasenya oblidada");
        builder.setMessage("Vols recuperar la teva contrasenya? \n S'enviarà un mail a l'adreça electrònica introduïda.");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertDialogView = inflater.inflate(R.layout.linear_layout_alert_dialog_forgot_password, null);
        builder.setView(alertDialogView);
        builder.setPositiveButton("SI", (dialogInterface, i) -> {
            String mailForgotPassword = ((EditText) alertDialogView.findViewById(R.id.etMailForgotPassword)).getText().toString();
            if ( isValidMail(mailForgotPassword)){
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.sendPasswordResetEmail(mailForgotPassword).addOnCompleteListener(task -> makeToast("S'ha enviat un correu electrònic a l'adreça electrònica per tal de recuperar la contrasenya."));
                alertDialogForgotPassword.dismiss();
            } else {
                makeToast("L'adreça electrònica no té un format valid.");
            }
        });
        builder.setNegativeButton("NO", (dialogInterface, i) -> alertDialogForgotPassword.dismiss());
        alertDialogForgotPassword = builder.create();
        alertDialogForgotPassword.show();
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
        forgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void goToRegisterPage() {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    private void goToMenuPrincipalPage(String email, String username) {
        Intent intent = new Intent(this, MenuPrincipal.class);
        mAuth.getCurrentUser().getEmail();
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}