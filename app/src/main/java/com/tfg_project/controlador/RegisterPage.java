package com.tfg_project.controlador;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tfg_project.R;
import com.tfg_project.model.firestore.FirebaseOperationsRegisterPage;
import com.tfg_project.model.utils.UtilsProject;

public class RegisterPage extends AppCompatActivity {

    private static final String TAG = "REGISTER_PAGE_TAG";

    private ImageButton backButton;
    private TextView tvHaveAccount;
    private EditText etUsername, etMail, etPassword1, etPassword2;
    private Button bRegister;
    private FirebaseAuth mAuth;
    private Context context;
    private UtilsProject utilsProject;
    private FirebaseOperationsRegisterPage firebaseOperations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

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
        context = this;
        utilsProject = new UtilsProject(context);
        firebaseOperations = new FirebaseOperationsRegisterPage(context);

        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        etUsername = findViewById(R.id.etRegisterUsername);
        etMail = findViewById(R.id.etRegisterMail);
        etPassword1 = findViewById(R.id.etRegisterPassword);
        etPassword2 = findViewById(R.id.etRegisterPassword2);
        backButton = findViewById(R.id.backButton);
        bRegister = findViewById(R.id.bRegister);
        mAuth = FirebaseAuth.getInstance();
    }
}