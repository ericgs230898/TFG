package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tfg_project.R;
import com.tfg_project.model.firestore.FirebaseOperationsRegisterPage;
import com.tfg_project.model.utils.UtilsProject;

public class RegisterPage extends AppCompatActivity {

    private ImageButton backButton;
    private TextView tvHaveAccount;
    private EditText etUsername;
    private EditText etMail;
    private EditText etPassword1;
    private EditText etPassword2;
    private Button bRegister;
    private UtilsProject utilsProject;
    private FirebaseOperationsRegisterPage firebaseOperations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();

        tvHaveAccount.setOnClickListener(view -> utilsProject.goToAnotherActivity(LoginPage.class, null));
        backButton.setOnClickListener(view -> utilsProject.goToAnotherActivity(LoginPage.class, null));

        bRegister.setOnClickListener(view -> {
            if ( utilsProject.checkParameters(etMail.getText().toString(), etUsername.getText().toString(), etPassword1.getText().toString(), etPassword2.getText().toString()) ) {
                firebaseOperations.createUser(this, etMail.getText().toString(), etUsername.getText().toString(), etPassword1.getText().toString());
            }
        });
    }



    private void initializeVariables(){
        Context context = this;
        utilsProject = new UtilsProject(context);
        firebaseOperations = new FirebaseOperationsRegisterPage(context);

        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        etUsername = findViewById(R.id.etRegisterUsername);
        etMail = findViewById(R.id.etRegisterMail);
        etPassword1 = findViewById(R.id.etRegisterPassword);
        etPassword2 = findViewById(R.id.etRegisterPassword2);
        backButton = findViewById(R.id.backButton);
        bRegister = findViewById(R.id.bRegister);
    }
}