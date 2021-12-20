package com.tfg_project.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PerfilPage extends AppCompatActivity {

    private TextView tvMail;
    private EditText etUsername;
    private String mail;
    private String username;
    private ImageButton backButton;
    private Button bTancarSessio;
    private Button bEliminarCompte;
    private Button bModificarDades;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_page);

        initializeVariables();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        backButton.setOnClickListener(view -> finish());

        tvMail.setText(mail);
        etUsername.setText(username);

        bTancarSessio.setOnClickListener(view -> {
            firebaseAuth.signOut();
            goToAnotherActivity(LoginPage.class, null);
        });

        bEliminarCompte.setOnClickListener(view -> dialogAlert());

        bModificarDades.setOnClickListener(view -> {
            String newUsername = etUsername.getText().toString();
            if (isValidUsername(newUsername)){
                FirebaseFirestore.getInstance().collection("Usuari").document(mail).update("usuari", newUsername).addOnCompleteListener(task -> {
                    makeToast("Dades modificades correctament.");
                });
            } else {
                makeToast("Nom d'usuari no vàlid");
            }
        });
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private static boolean isValidUsername(String s){
        Pattern USERNAME_PATTERN
                = Pattern.compile(
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
        return !TextUtils.isEmpty(s) && USERNAME_PATTERN.matcher(s).matches();
    }

    private void dialogAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar compte");
        builder.setMessage("Estàs segur que vols eliminar el teu compte? \n Les teves dades i el teu progrès serà eliminat completament.");
        builder.setPositiveButton("Si", (dialogInterface, i) -> {
            eliminaUsuari();
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void eliminaUsuari() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("UsuariLligaVirtual").document(mail);
        documentReference.get().addOnCompleteListener(task -> {
            List<String> lliguesUsuari = (List<String>) task.getResult().get("Lligues");
            int size = lliguesUsuari.size();
            final int[] count = {0};
            for ( String lligaUsuari : lliguesUsuari ) {
                FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(lligaUsuari).update("usuaris", FieldValue.arrayRemove(mail)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG","Usuari eliminat de les lligues virtuals");
                        count[0]++;
                        if ( count[0] == size ) {
                            documentReference.delete().addOnCompleteListener(task13 -> {
                                Log.d("TAG", "Usuari eliminat de les lligues virtuals2");
                                FirebaseFirestore.getInstance().collection("Usuari").document(mail).delete().addOnCompleteListener(task12 -> {
                                    Log.d("TAG", "Usuari eliminat");
                                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task1 -> {
                                        Log.d("TAG","Usuari eliminat FIREBASE AUTH");
                                        goToAnotherActivity(LoginPage.class, null);
                                    });
                                });
                            });
                        }
                    }
                });
            }
        });
    }

    private void initializeVariables() {
        tvMail = findViewById(R.id.tvAccountMail);
        mail = this.getIntent().getStringExtra("mail");
        username = this.getIntent().getStringExtra("username");
        etUsername = findViewById(R.id.etPerfilUsername);
        backButton = findViewById(R.id.backButtonAccountPage);
        bTancarSessio = findViewById(R.id.bTancarSessio);
        bEliminarCompte = findViewById(R.id.bEliminarCompte);
        bModificarDades = findViewById(R.id.bModificaDades);
    }

    private void goToAnotherActivity(Object pageClass, Map<String, String> map) {
        Intent intent = new Intent(this, (Class<?>) pageClass);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivity(intent);
    }
}
