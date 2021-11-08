package com.tfg_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ServiceEquipsFCF serviceEquipsFCF = new ServiceEquipsFCF();
        serviceEquipsFCF.getEquipsParticipants();
        List<Equip> listEquips = serviceEquipsFCF.getListEquips();

        ServiceJugadorsFCF serviceJugadorsFCF = new ServiceJugadorsFCF();
        List<Jugador> jugadors = serviceJugadorsFCF.getJugadorsEquip(listEquips);

        Log.e("SIZE", String.valueOf(jugadors.size()));
        for (Jugador jug : jugadors){
            System.out.println(jug.getIdEquip() + "  " + jug.getNomJugador());
        }
    }
}