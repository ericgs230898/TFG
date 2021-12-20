package com.tfg_project.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.clases.AlineacioJugadorPuntuacio;
import com.tfg_project.clases.Jornada;
import com.tfg_project.clases.JugadorPuntuacio;
import com.tfg_project.clases.LoadingDialog;
import com.tfg_project.clases.PuntuacionsJugadorsAdapter;
import com.tfg_project.clases.UtilsProject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puntuacions extends AppCompatActivity {
    private String competicio;
    private String grup;
    private List<Jornada> jornades;
    private String sizeJornades;
    private ImageButton buttonBack;
    private Spinner spinner;
    private RecyclerView recyclerViewPuntuacions;
    private TextView tvPuntuacio;
    private Map<String, List<JugadorPuntuacio>> mapListPuntuacionsJugadors;
    private List<JugadorPuntuacio> listPuntuacionsJugadors;
    private PuntuacionsJugadorsAdapter adapter;
    private TextView tvAlineacio;
    private UtilsProject utils;
    private Context context;
    private Map<String, AlineacioJugadorPuntuacio> mapPuntuacions;
    private List<String> jornadesPossibles;
    private int countJornades;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacions);
        countJornades = 0;
        initializeVariables();
        loadingDialog = new LoadingDialog(this);
        String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String nomLligaVirtual = this.getIntent().getStringExtra("nomLligaVirtual");
        mapPuntuacions = new HashMap<>();
        jornadesPossibles = new ArrayList<>();
        loadingDialog.startLoadingDialog();
        FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual)
                .collection("Jornada").get().addOnCompleteListener(task -> {
                    for ( DocumentSnapshot doc : task.getResult().getDocuments()){
                        int sizeJornades = task.getResult().getDocuments().size();
                        String jornada = doc.getId();
                        doc.getReference().collection(mail).document("Plantilla").get().addOnCompleteListener(task1 -> {
                           if ( (boolean) task1.getResult().get("puntuat")) {
                               jornadesPossibles.add(jornada);
                               String alineacio = (String) task1.getResult().get("alineacio");
                               List<JugadorPuntuacio> jugadorPuntuacioList = new ArrayList<>();
                               doc.getReference().collection(mail).document("Plantilla").collection("Jugadors").get().addOnCompleteListener(task2 -> {
                                   for ( DocumentSnapshot doc1 : task2.getResult().getDocuments()){
                                       String punts = (String) doc1.get("punts");
                                       String posicio = (String) doc1.get("posicio");
                                       String nomJugador = doc1.getId();
                                       jugadorPuntuacioList.add(new JugadorPuntuacio(posicio, nomJugador, punts));
                                   }
                                   AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = new AlineacioJugadorPuntuacio(alineacio, jugadorPuntuacioList);
                                   mapPuntuacions.put(jornada, alineacioJugadorPuntuacio);
                                   countJornades++;
                                   if ( countJornades == sizeJornades) {
                                       loadingDialog.dissmisDialog();
                                       setRecyclerView();
                                   }
                               });
                           } else {
                                countJornades++;
                               if ( countJornades == sizeJornades) {
                                   loadingDialog.dissmisDialog();
                                   setRecyclerView();
                               }

                           }
                        });
                    }
                });
        buttonBack.setOnClickListener(view -> {
            finish();
        });


    }

    private void setRecyclerView() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jornadesPossibles);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornada = jornadesPossibles.get(i);
                AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = mapPuntuacions.get(jornada);
                tvAlineacio.setText("Alineacio: " + alineacioJugadorPuntuacio.getAlineacio());
                String puntuacio = getPuntuacioTotal(alineacioJugadorPuntuacio.getJugadorPuntuacioList());
                tvPuntuacio.setText("Punts totals: " + puntuacio);
                adapter = new PuntuacionsJugadorsAdapter(alineacioJugadorPuntuacio.getJugadorPuntuacioList());
                recyclerViewPuntuacions.setAdapter(adapter);
                recyclerViewPuntuacions.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private String getPuntuacioTotal(List<JugadorPuntuacio> jugadorPuntuacioList) {
        double total = 0;
        for ( JugadorPuntuacio jugadorPuntuacio : jugadorPuntuacioList ) {
            double d = Double.valueOf(jugadorPuntuacio.getPunts());
            total = total + d;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(total);
    }

    private void getPuntuacioJugador(String competicio, String grup, String jornada, String jugador, String posicio){
        competicio = competicio.toLowerCase().replace(" ", "-");
        String finalJornada = jornada.toLowerCase().replaceAll("\\s","");
        FirebaseFirestore.getInstance().collection("Puntuacions").document(competicio)
                .collection("Grups").document("grup"+grup).collection("Jornades")
                .document(finalJornada).collection("Jugadors").document(jugador).get().addOnCompleteListener(task -> {
                    String punts = "0";
                    try {
                        punts = (String) task.getResult().get(posicio);
                    } catch ( Exception e ){
                        punts = "0";
                    }
                    if ( punts == null ) punts = "0";
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    if ( punts != null && !punts.equals("")){
                        List<JugadorPuntuacio> jugadorPuntuacioAux = mapListPuntuacionsJugadors.get(jornada);
                        jugadorPuntuacioAux.add(new JugadorPuntuacio(posicio, jugador, df.format(Double.valueOf(punts))));
                        mapListPuntuacionsJugadors.put(finalJornada, jugadorPuntuacioAux);
                        refreshAdapter(jornada);
                        /*listPuntuacionsJugadors.add(new JugadorPuntuacio(posicio, jugador, df.format(Double.valueOf(punts))));
                        adapter.notifyItemInserted(listPuntuacionsJugadors.size()-1);*/
                    } else {
                        List<JugadorPuntuacio> jugadorPuntuacioAux = mapListPuntuacionsJugadors.get(jornada);
                        jugadorPuntuacioAux.add(new JugadorPuntuacio(posicio, jugador, df.format(Double.valueOf(punts))));
                        mapListPuntuacionsJugadors.put(finalJornada, jugadorPuntuacioAux);
                        refreshAdapter(jornada);
                        /*listPuntuacionsJugadors.add(new JugadorPuntuacio(posicio, jugador, "0"));
                        adapter.notifyItemInserted(listPuntuacionsJugadors.size()-1);*/
                    }
                });
    }

    private void refreshAdapter(String jornada) {
        List<JugadorPuntuacio> jugadorPuntuacioListAux = mapListPuntuacionsJugadors.get(jornada);
        if ( spinner.getSelectedItem().equals(jornada)){
            adapter = new PuntuacionsJugadorsAdapter(jugadorPuntuacioListAux);
            recyclerViewPuntuacions.setAdapter(adapter);
        }
    }

    private void initializeVariables() {
        context = this;
        utils = new UtilsProject();
        competicio = this.getIntent().getStringExtra("competicio");
        grup = this.getIntent().getStringExtra("grup");
        buttonBack = findViewById(R.id.ibBackButtonPuntuacions);
        spinner = findViewById(R.id.spinnerJornadesPuntuacions);
        recyclerViewPuntuacions = findViewById(R.id.recyclerViewPuntuacions);
        tvPuntuacio = findViewById(R.id.tvPuntsJornada);

        tvAlineacio = findViewById(R.id.tvAlineacioPuntuacions);
        mapListPuntuacionsJugadors = new HashMap<>();
        listPuntuacionsJugadors = new ArrayList<>();
        adapter = new PuntuacionsJugadorsAdapter(listPuntuacionsJugadors);
        recyclerViewPuntuacions.setAdapter(adapter);
        recyclerViewPuntuacions.setLayoutManager(new LinearLayoutManager(this));
    }
}