package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tfg_project.R;
import com.tfg_project.model.firestore.FirebaseOperationsPerfilPage;
import com.tfg_project.model.utils.UtilsProject;

public class PerfilPage extends AppCompatActivity {

    private TextView tvMail;
    private EditText etUsername;
    private String mail;
    private ImageButton backButton;
    private Button bTancarSessio;
    private Button bEliminarCompte;
    private Button bModificarDades;
    private AlertDialog alertDialog;
    private UtilsProject utilsProject;
    private FirebaseOperationsPerfilPage firebaseOperationsPerfilPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        backButton.setOnClickListener(view -> finish());

        tvMail.setText(mail);
        Task<DocumentSnapshot> taskUser = firebaseOperationsPerfilPage.getUsername();
        Tasks.whenAllComplete(taskUser).addOnCompleteListener(task1 -> etUsername.setText(firebaseOperationsPerfilPage.getUsuari()));

        bTancarSessio.setOnClickListener(view -> {
            firebaseAuth.signOut();
            finishAffinity();
            utilsProject.goToAnotherActivity(LoginPage.class, null);
        });

        bEliminarCompte.setOnClickListener(view -> dialogAlert());

        bModificarDades.setOnClickListener(view -> {
            String newUsername = etUsername.getText().toString();
            if (UtilsProject.isValidUsername(newUsername)){
                firebaseOperationsPerfilPage.modificaDadesUsuari(newUsername);
            } else {
                utilsProject.makeToast("Nom d'usuari no vàlid");
            }
        });
    }

    private void dialogAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar compte");
        builder.setMessage("Estàs segur que vols eliminar el teu compte? \n Les teves dades i el teu progrès serà eliminat completament.");
        builder.setPositiveButton("Si", (dialogInterface, i) -> firebaseOperationsPerfilPage.eliminaUsuari());
        builder.setNegativeButton("No", (dialogInterface, i) -> alertDialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void initializeVariables() {
        Context context = this;
        utilsProject = new UtilsProject(context);
        tvMail = findViewById(R.id.tvAccountMail);
        mail = this.getIntent().getStringExtra("mail");
        etUsername = findViewById(R.id.etPerfilUsername);
        backButton = findViewById(R.id.backButtonAccountPage);
        bTancarSessio = findViewById(R.id.bTancarSessio);
        bEliminarCompte = findViewById(R.id.bEliminarCompte);
        bModificarDades = findViewById(R.id.bModificaDades);
        firebaseOperationsPerfilPage = new FirebaseOperationsPerfilPage(context);
    }
}
