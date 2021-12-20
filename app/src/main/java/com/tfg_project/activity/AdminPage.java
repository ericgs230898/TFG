package com.tfg_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.tfg_project.R;
import com.tfg_project.clases.UtilsProject;
import com.tfg_project.services.ServicePartits;
import com.tfg_project.services.ServicePartitsJugadors;
import com.tfg_project.services.ServicePuntuacions;

public class AdminPage extends AppCompatActivity {

    Button bPuntuarJornades, bPuntuarPlantilles, bNousPartits, bNousJugadors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        bNousJugadors = findViewById(R.id.bAdminJugadorsNous);
        bNousPartits = findViewById(R.id.bAdminPartitsNous);
        bPuntuarJornades = findViewById(R.id.bAdminPuntuarJornades);
        bPuntuarPlantilles = findViewById(R.id.bAdminPuntuarPlantilles);

        UtilsProject utilsProject = new UtilsProject();
        Context context = this;

        bNousPartits.setOnClickListener(view -> {
            ServicePartits servicePartits = new ServicePartits();
            //servicePartits.ejecutar();
            utilsProject.makeToast(context, "Bolcant els partits nous");
        });

        bNousJugadors.setOnClickListener(view -> {
            ServicePartitsJugadors servicePartitsJugadors = new ServicePartitsJugadors();
            utilsProject.makeToast(context, "Bolcant els jugadors dels partits nous");
            //servicePartitsJugadors.ejecutar("","","","");
        });

        bPuntuarJornades.setOnClickListener(view -> {
            ServicePuntuacions servicePuntuacions = new ServicePuntuacions();
            utilsProject.makeToast(context, "Bolcant les puntuacions de les jornades");
            servicePuntuacions.puntuarJornades();
        });

        bPuntuarPlantilles.setOnClickListener(view -> {
            ServicePuntuacions servicePuntuacions = new ServicePuntuacions();
            utilsProject.makeToast(context, "Bolcant les puntuacions de les plantilles");
            servicePuntuacions.puntuacionsTotals();
        });
    }
}