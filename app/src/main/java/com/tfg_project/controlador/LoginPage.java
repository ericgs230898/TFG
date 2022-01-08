package com.tfg_project.controlador;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tfg_project.R;
import com.tfg_project.model.firestore.FirebaseOperationsLoginPage;
import com.tfg_project.model.beans.LoadingDialog;
import com.tfg_project.model.utils.UtilsProject;

public class LoginPage extends AppCompatActivity {

    private TextView tvRegister;
    private EditText etLoginMail;
    private EditText etLoginPassword;
    private FirebaseAuth mAuth;
    private Button bIniciaSessio;
    private TextView forgotPassword;
    private AlertDialog alertDialogForgotPassword;
    private UtilsProject utilsProject;

    private static final String NO = "NO";
    private static final String SI = "SI";
    private FirebaseOperationsLoginPage firebaseOperations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializePrivateVariables();

        forgotPassword.setOnClickListener(view -> showAlertDialog());

        Activity activity = this;

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            if ( "eric.gonzalez.sole@estudiantat.upc.edu".equals(user.getEmail())){
                utilsProject.goToAnotherActivity(AdminPage.class, null);
            } else {
                final LoadingDialog loadingDialog = new LoadingDialog(activity);
                loadingDialog.startLoadingDialog();
                firebaseOperations.getUsername();
            }
        }

        tvRegister.setOnClickListener(view -> utilsProject.goToAnotherActivity(RegisterPage.class, null));
        bIniciaSessio.setOnClickListener(view -> {
            if (utilsProject.checkParameters(etLoginMail.getText().toString(), etLoginPassword.getText().toString())){
                final LoadingDialog loadingDialog = new LoadingDialog(activity);
                loadingDialog.startLoadingDialog();
                firebaseOperations.signIn(activity, etLoginMail.getText().toString(), etLoginPassword.getText().toString());
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.forgot_password));
        builder.setMessage(getString(R.string.recuperar_password));
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertDialogView = inflater.inflate(R.layout.linear_layout_alert_dialog_forgot_password, null);
        builder.setView(alertDialogView);
        builder.setPositiveButton(SI, (dialogInterface, i) -> {
            String mailForgotPassword = ((EditText) alertDialogView.findViewById(R.id.etMailForgotPassword)).getText().toString();
            if (UtilsProject.isValidMail(mailForgotPassword)){
                firebaseOperations.sendPasswordResetEmail(mailForgotPassword);
                alertDialogForgotPassword.dismiss();
            } else {
                utilsProject.makeToast(getString(R.string.mail_incorrect));
            }
        });
        builder.setNegativeButton(NO, (dialogInterface, i) -> alertDialogForgotPassword.dismiss());
        alertDialogForgotPassword = builder.create();
        alertDialogForgotPassword.show();
    }

    private void initializePrivateVariables() {
        Context context = this;
        utilsProject = new UtilsProject(context);
        mAuth = FirebaseAuth.getInstance();
        etLoginMail = findViewById(R.id.etLoginMail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        tvRegister = findViewById(R.id.tvRegisterNoAccount);
        bIniciaSessio = findViewById(R.id.bLoginIniciaSesio);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        firebaseOperations = new FirebaseOperationsLoginPage(context);
    }

}