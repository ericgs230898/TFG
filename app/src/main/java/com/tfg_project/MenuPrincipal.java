package com.tfg_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuPrincipal extends AppCompatActivity {

    private static final List<String> listCategoriaGrup = Arrays.asList("primera-catalana1", "primera-catalana2", "primera-catalana3",
            "segona-catalana2", "segona-catalana3", "segona-catalana4", "segona-catalana5", "segona-catalana6",
            "tercera-catalana1", "tercera-catalana2","tercera-catalana3","tercera-catalana4","tercera-catalana5", "tercera-catalana6","tercera-catalana7",
            "tercera-catalana8","tercera-catalana9","tercera-catalana10","tercera-catalana11","tercera-catalana12","tercera-catalana13","tercera-catalana14",
            "tercera-catalana15","tercera-catalana16","tercera-catalana17","tercera-catalana18","tercera-catalana19",
            "quarta-catalana1", "quarta-catalana2", "quarta-catalana3", "quarta-catalana4","quarta-catalana5","quarta-catalana6","quarta-catalana7","quarta-catalana8",
            "quarta-catalana9","quarta-catalana10","quarta-catalana11","quarta-catalana12","quarta-catalana13","quarta-catalana14","quarta-catalana15","quarta-catalana16",
            "quarta-catalana17", "quarta-catalana20","quarta-catalana21","quarta-catalana23","quarta-catalana24","quarta-catalana27","quarta-catalana28","quarta-catalana29",
            "quarta-catalana30");

    private static final List<Integer> listGrupsCompeticions = Arrays.asList
            (R.array.grupsPrimeraCatalana, R.array.grupsSegonaCatalana,
                    R.array.grupsTerceraCatalana, R.array.grupsQuartaCatalana);

    private ImageButton accountButton;
    private Button crearLligaVirtualButton;
    private Button crearLligaVirtualOK;
    private FirebaseFirestore firebaseFirestore;
    private String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initializeVariables();

        accountButton.setOnClickListener(view -> {
            goToAccountPage();
        });

        crearLligaVirtualButton.setOnClickListener(view -> {
            createAlertDialogCrearLligaVirtual();
        });


    }

    private void createAlertDialogCrearLligaVirtual() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context context = this;

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertDialogView = inflater.inflate(R.layout.linear_layout_crear_lliga_virtual, null);

        EditText password = alertDialogView.findViewById(R.id.etPassword_CrearLligaVirtual);
        EditText nomLligaVirtual = alertDialogView.findViewById(R.id.etUsername_CrearLligaVirtual);

        Spinner spinnerMaxParticipants = alertDialogView.findViewById(R.id.spinnerMaxParticipants_CrearLligaVirtual);

        Spinner spinnerGrups = alertDialogView.findViewById(R.id.spinnerGrup_CrearLligaVirtual);

        Spinner spinnerCompeticio = alertDialogView.findViewById(R.id.spinnerCompeticio_CrearLligaVirtual);
        spinnerCompeticio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, listGrupsCompeticions.get(i), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGrups.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        crearLligaVirtualOK = alertDialogView.findViewById(R.id.bCrearLligaOK);
        crearLligaVirtualOK.setOnClickListener(view -> {
            Map<String,String> map = new HashMap<>();
            map.put("nomLligaVirtual", nomLligaVirtual.getText().toString());
            map.put("password", password.getText().toString());
            map.put("competicio", spinnerCompeticio.getSelectedItem().toString());
            map.put("grup", spinnerGrups.getSelectedItem().toString());
            map.put("maxParticipants", spinnerMaxParticipants.getSelectedItem().toString());
            firebaseFirestore.collection("LligaVirtual").document(nomLligaVirtual.getText().toString()).set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("TAG", "TASK COMPLETED SUCCESSFULLY");
                }
            });
            Map<String,String> mapUsuari = new HashMap<>();
            mapUsuari.put(emailUser, nomLligaVirtual.getText().toString());
            firebaseFirestore.collection("UsuariLligaVirtual").document(nomLligaVirtual.getText().toString()).set(mapUsuari).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("TAG", "TASK COMPLETED SUCCESSFULLY");
                }
            });
        });

        builder.setView(alertDialogView);
        builder.setTitle("Crear Lliga Virtual");
        builder.create().show();
    }

    private void goToAccountPage() {
        Intent intent = new Intent(this, PerfilPage.class);
        startActivity(intent);
    }

    private void initializeVariables() {
        accountButton = findViewById(R.id.accountButton);
        crearLligaVirtualButton = findViewById(R.id.bCrearLligaVirtual);
        firebaseFirestore = FirebaseFirestore.getInstance();
        emailUser = getIntent().getExtras().getString("email");
    }



    // ServiceEquipsFCF serviceEquipsFCF = new ServiceEquipsFCF();
    // serviceEquipsFCF.getEquipsParticipants();
/*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (String catGrup : listCategoriaGrup ) {

            db.collection("Equips").document(catGrup).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    System.out.println(catGrup);
                    System.out.println(documentSnapshot.getData().size());
                    List<Equip> equips = new ArrayList<>();
                    Map<String,Object> map = documentSnapshot.getData();
                    for ( Map.Entry<String, Object> entry : map.entrySet()){
                        equips.add(new Equip(entry.getKey(), (String) entry.getValue()));
                    }
                    addPlayerToCategory(map, catGrup);
                }
            });
        }
        /*ServiceJugadorsFCF serviceJugadorsFCF = new ServiceJugadorsFCF();
        List<Jugador> jugadors = serviceJugadorsFCF.getJugadorsEquip(listEquips);

        Log.e("SIZE", String.valueOf(jugadors.size()));
        for (Jugador jug : jugadors){
            System.out.println(jug.getIdEquip() + "  " + jug.getNomJugador());
        }*/

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> equips = new HashMap<String,String>();
        for ( Equip equip : listEquips ) {
            equips.put(equip.getNomEquip(), equip.getLinkEquip());
        }

        db.collection("Equips").add(equips).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DB","DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DB", "Error adding document", e);
            }
        });
    }

    private void addPlayerToCategory(Map<String, Object> map, String catGrup) {
        ServiceJugadorsFCF serviceJugadorsFCF = new ServiceJugadorsFCF();
        List<Equip> equips = new ArrayList<>();
        for ( Map.Entry<String, Object> entry : map.entrySet()){
            equips.add(new Equip(entry.getKey(), (String) entry.getValue()));
        }
        serviceJugadorsFCF.getJugadorsEquip(equips, catGrup);
    }*/
}