package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.model.utils.UtilsProject;
import com.tfg_project.model.services.ServicePartits;
import com.tfg_project.model.services.ServicePartitsJugadors;
import com.tfg_project.model.services.ServicePuntuacions;

import java.util.HashMap;
import java.util.Map;

public class AdminPage extends AppCompatActivity {

    private static final String TAG = "ADMIN_PAGE_TAG";
    private AlertDialog alertDialogTancarApp;

    @Override
    public void onBackPressed(){
        alertDialogTancarApp.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UtilsProject utilsProject = new UtilsProject(this);

        ImageButton ibBackButton = findViewById(R.id.ibBackButtonAdminPage);
        ibBackButton.setOnClickListener(view -> alertDialogTancarApp.show());

        AlertDialog.Builder builderBack = new AlertDialog.Builder(this);
        builderBack.setMessage("Vols tancar l'aplicació?")
                .setPositiveButton("Si", (dialog, id) -> {
                    alertDialogTancarApp.dismiss();
                    finishAffinity();
                })
                .setNegativeButton("No", (dialog, id) -> alertDialogTancarApp.dismiss());
        // Create the AlertDialog object and return it
        alertDialogTancarApp = builderBack.create();

        Button bTancarSessio = findViewById(R.id.bAdminPageTancarSessio);
        bTancarSessio.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            finish();
            utilsProject.goToAnotherActivity(LoginPage.class, null);
        });

        Button bNousJugadors = findViewById(R.id.bAdminJugadorsNous);
        Button bNousPartits = findViewById(R.id.bAdminPartitsNous);
        Button bPuntuarJornades = findViewById(R.id.bAdminPuntuarJornades);
        Button bPuntuarPlantilles = findViewById(R.id.bAdminPuntuarPlantilles);
        Button bCanviarPuntuacions = findViewById(R.id.bAdminCanviarPuntuacions);

        bNousPartits.setOnClickListener(view -> {
            ServicePartits servicePartits = new ServicePartits();
            utilsProject.makeToast(getString(R.string.bolcant_nous_partits));
        });

        bNousJugadors.setOnClickListener(view -> {
            ServicePartitsJugadors servicePartitsJugadors = new ServicePartitsJugadors();
            utilsProject.makeToast(getString(R.string.bolcant_nous_jugadors));
        });

        bPuntuarJornades.setOnClickListener(view -> {
            ServicePuntuacions servicePuntuacions = new ServicePuntuacions();
            utilsProject.makeToast(getString(R.string.bolcant_puntuacions_jornades));
            servicePuntuacions.puntuarJornades();
        });

        bPuntuarPlantilles.setOnClickListener(view -> {
            ServicePuntuacions servicePuntuacions = new ServicePuntuacions();
            utilsProject.makeToast(getString(R.string.bolcant_puntuacions_plantilles));
            servicePuntuacions.puntuacionsTotals();
        });

        bCanviarPuntuacions.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Canviar puntuacions del joc");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = inflater.inflate(R.layout.alert_dialog_puntuacions_del_joc, null);
            builder.setView(alertDialogView);
            builder.setPositiveButton("SI", (dialogInterface, i) -> {
                // canviar puntuacions
                EditText golsMarcats = alertDialogView.findViewById(R.id.etGolsMarcatsPuntuacionsJoc);
                EditText golsMarcatsPropia = alertDialogView.findViewById(R.id.etGolsMarcatsPropiaPuntuacionsJoc);
                EditText golsMarcatsPenal = alertDialogView.findViewById(R.id.etGolsMarcatsPenalPuntuacionsJoc);
                EditText golsRebutsDefensa = alertDialogView.findViewById(R.id.etDefGolRebutPuntuacionsJoc);
                EditText golsRebutsPorter = alertDialogView.findViewById(R.id.etPorGolRebutPuntuacionsJoc);
                EditText porteriaA0Defensa = alertDialogView.findViewById(R.id.etDefPorteriaPuntuacionsJoc);
                EditText porteriaA0Porter = alertDialogView.findViewById(R.id.etPorPorteria0PuntuacionsJoc);
                EditText targetesGrogues = alertDialogView.findViewById(R.id.etTarjetaAmarillaPuntuacionsJoc);
                EditText targetesVermelles = alertDialogView.findViewById(R.id.etTarjetaRojaPuntuacionsJoc);
                EditText minutJugat = alertDialogView.findViewById(R.id.etMinutsJugatsPuntuacionsJoc);
                Map<String, Object> mapUpdate = new HashMap<>();
                mapUpdate.put("golMarcat", golsMarcats.getText().toString());
                mapUpdate.put("golMarcatPropia", golsMarcatsPropia.getText().toString());
                mapUpdate.put("golMarcatPenalti", golsMarcatsPenal.getText().toString());
                mapUpdate.put("golRebutDefensa", golsRebutsDefensa.getText().toString());
                mapUpdate.put("golRebutPorter", golsRebutsPorter.getText().toString());
                mapUpdate.put("porteria0Defensa", porteriaA0Defensa.getText().toString());
                mapUpdate.put("porteria0Porter", porteriaA0Porter.getText().toString());
                mapUpdate.put("tarjetaAmarilla", targetesGrogues.getText().toString());
                mapUpdate.put("tarjetaRoja", targetesVermelles.getText().toString());
                mapUpdate.put("minutJugat", minutJugat.getText().toString());

                if ( comprovarMap(mapUpdate)){
                    FirebaseFirestore.getInstance().collection("PuntuacionsJoc").document("PuntuacionsJoc").update(mapUpdate).addOnCompleteListener(task -> {
                        Log.d(TAG, "Update Correctly");
                        utilsProject.makeToast( "Actualització correcta.");
                    });
                } else {
                    utilsProject.makeToast("Algún dels números introduits no es vàlid. Recorda que s'ha d'utilitzar un punt en compte d'una coma si es el número és decimal." );
                }
            });
        });
    }

    private boolean comprovarMap(Map<String, Object> mapUpdate) {
        for ( Map.Entry<String, Object> entry : mapUpdate.entrySet()){
            try {
                Double.parseDouble((String) entry.getValue());
            } catch (NumberFormatException e ){
                return false;
            }
        }
        return true;
    }
}