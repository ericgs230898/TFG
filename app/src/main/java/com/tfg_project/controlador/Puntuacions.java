package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.model.beans.AlineacioJugadorPuntuacio;
import com.tfg_project.model.beans.JugadorPuntuacio;
import com.tfg_project.model.beans.LoadingDialog;
import com.tfg_project.model.adapters.PuntuacionsJugadorsAdapter;
import com.tfg_project.model.firestore.FirebaseOperationsPuntuacionsPage;
import com.tfg_project.model.utils.UtilsProject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Puntuacions extends AppCompatActivity {

    private ImageButton buttonBack;
    private Spinner spinner;
    private RecyclerView recyclerViewPuntuacions;
    private TextView tvPuntuacio;
    private PuntuacionsJugadorsAdapter adapter;
    private TextView tvAlineacio;
    private Context context;
    private Map<String, AlineacioJugadorPuntuacio> mapPuntuacions;
    private List<String> jornadesPossibles;
    private int countJornades;
    private Button bPuntuacionsJoc;
    private Map<String, String> mapPuntuacionsJoc;
    private FirebaseOperationsPuntuacionsPage firebaseOperationsPuntuacionsPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacions);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UtilsProject utilsProject = new UtilsProject(this);

        countJornades = 0;
        initializeVariables();
        LoadingDialog loadingDialog = LoadingDialog.getInstance(this);
        String mail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String nomLligaVirtual = this.getIntent().getStringExtra("nomLligaVirtual");

        Task<DocumentSnapshot> taskPuntuacionsJoc = firebaseOperationsPuntuacionsPage.getPuntuacionsJoc();
        Tasks.whenAllComplete(taskPuntuacionsJoc).addOnCompleteListener(task -> mapPuntuacionsJoc = firebaseOperationsPuntuacionsPage.getMapPuntuacionsJoc());

        mapPuntuacions = new HashMap<>();
        jornadesPossibles = new ArrayList<>();
        loadingDialog.startLoadingDialog();
        FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual)
                .collection("Jornada").get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).getDocuments().isEmpty()) {
                loadingDialog.dissmisDialog();
                utilsProject.makeToast("No hi ha cap puntuaci?? guardada");
            }
            else {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                int sizeJornades = task.getResult().getDocuments().size();
                String jornada = doc.getId();
                doc.getReference().collection(Objects.requireNonNull(mail)).document("Plantilla").get().addOnCompleteListener(task1 -> {
                    if ((boolean) Objects.requireNonNull(task1.getResult()).get("puntuat")) {
                        jornadesPossibles.add(jornada);
                        String alineacio = (String) task1.getResult().get("alineacio");
                        List<JugadorPuntuacio> jugadorPuntuacioList = new ArrayList<>();
                        doc.getReference().collection(mail).document("Plantilla").collection("Jugadors").get().addOnCompleteListener(task2 -> {
                            for (DocumentSnapshot doc1 : Objects.requireNonNull(task2.getResult()).getDocuments()) {
                                String punts = (String) doc1.get("punts");
                                String posicio = (String) doc1.get("posicio");
                                String nomJugador = doc1.getId();
                                jugadorPuntuacioList.add(new JugadorPuntuacio(posicio, nomJugador, punts));
                            }
                            AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = new AlineacioJugadorPuntuacio(alineacio, jugadorPuntuacioList);
                            mapPuntuacions.put(jornada, alineacioJugadorPuntuacio);
                            countJornades++;
                            if (countJornades == sizeJornades) {
                                loadingDialog.dissmisDialog();
                                setRecyclerView();
                            }
                        });
                    } else {
                        countJornades++;
                        if (countJornades == sizeJornades) {
                            loadingDialog.dissmisDialog();
                            setRecyclerView();
                        }

                        }
                    });
                }
            }
                });
        buttonBack.setOnClickListener(view -> finish());

        bPuntuacionsJoc.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Puntuacions del joc");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = inflater.inflate(R.layout.alert_dialog_puntuacions_del_joc, null);

            TextView tvGolsMarcats;
            TextView tvGolsMarcatsPropia;
            TextView tvGolsMarcatsPenalti;
            TextView tvGolRebutDefensa;
            TextView tvGolRebutPorter;
            TextView tvMinutJugat;
            TextView tvPorteria0Defensa;
            TextView tvPorteria0Porter;
            TextView tvTarjetaAmarilla;
            TextView tvTarjetaRoja;

            tvGolsMarcats = alertDialogView.findViewById(R.id.tvGolsMarcatsPuntuacionsJoc);
            tvGolsMarcatsPenalti = alertDialogView.findViewById(R.id.tvGolsMarcatsPenalPuntuacionsJoc);
            tvGolsMarcatsPropia = alertDialogView.findViewById(R.id.tvGolsMarcatsPropiaPuntuacionsJoc);
            tvGolRebutDefensa = alertDialogView.findViewById(R.id.tvDefGolRebutPuntuacionsJoc);
            tvGolRebutPorter = alertDialogView.findViewById(R.id.tvPorGolRebutPuntuacionsJoc);
            tvPorteria0Defensa = alertDialogView.findViewById(R.id.tvDefPorteriaPuntuacionsJoc);
            tvPorteria0Porter = alertDialogView.findViewById(R.id.tvPorPorteria0PuntuacionsJoc);
            tvTarjetaAmarilla = alertDialogView.findViewById(R.id.tvTarjetaAmarillaPuntuacionsJoc);
            tvTarjetaRoja = alertDialogView.findViewById(R.id.tvTarjetaRojaPuntuacionsJoc);
            tvMinutJugat = alertDialogView.findViewById(R.id.tvMinutsJugatsPuntuacionsJoc);
            tvGolsMarcats.setText(mapPuntuacionsJoc.get("golMarcat"));
            tvGolsMarcatsPenalti.setText(mapPuntuacionsJoc.get("golMarcatPenalti"));
            tvMinutJugat.setText(mapPuntuacionsJoc.get("minutJugat"));
            tvGolsMarcatsPropia.setText(mapPuntuacionsJoc.get("golMarcatPropia"));
            tvGolRebutDefensa.setText(mapPuntuacionsJoc.get("golRebutDefensa"));
            tvGolRebutPorter.setText(mapPuntuacionsJoc.get("golRebutPorter"));
            tvPorteria0Defensa.setText(mapPuntuacionsJoc.get("porteria0Defensa"));
            tvPorteria0Porter.setText(mapPuntuacionsJoc.get("porteria0Porter"));
            tvTarjetaAmarilla.setText(mapPuntuacionsJoc.get("tarjetaAmarilla"));
            tvTarjetaRoja.setText(mapPuntuacionsJoc.get("tarjetaRoja"));
            builder.setView(alertDialogView);
            builder.setPositiveButton("OK", (dialogInterface, i) -> builder.create().dismiss());
            builder.create().show();
        });
    }

    private void setRecyclerView() {
        Collections.sort(jornadesPossibles, (s, t1) -> {
            int num1 = Integer.parseInt(s.substring(8));
            int num2 = Integer.parseInt(t1.substring(8));
            return Integer.compare(num1, num2);
        });
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jornadesPossibles);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornada = jornadesPossibles.get(i);
                AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = mapPuntuacions.get(jornada);
                tvAlineacio.setText("Alineacio: " + Objects.requireNonNull(alineacioJugadorPuntuacio).getAlineacio());
                String puntuacio = getPuntuacioTotal(alineacioJugadorPuntuacio.getJugadorPuntuacioList());
                tvPuntuacio.setText("Punts totals: " + puntuacio.replace(",", "."));
                List<JugadorPuntuacio> listJugadors = alineacioJugadorPuntuacio.getJugadorPuntuacioList();
                Collections.sort(listJugadors, (jugadorPuntuacio, t1) -> {
                    switch (jugadorPuntuacio.getPosicio()) {
                        case "POR":
                            return -1;
                        case "DFC":
                            if (t1.getPosicio().equals("MC") || t1.getPosicio().equals("DC"))
                                return -1;
                            else if (t1.getPosicio().equals("DFC")) return 0;
                            else return 1;
                        case "MC":
                            if (t1.getPosicio().equals("DC")) return -1;
                            else if (t1.getPosicio().equals("MC")) return 0;
                            else return 1;
                        case "DC":
                            if (t1.getPosicio().equals("DC")) return 0;
                            else return 1;
                    }
                    return 0;
                });
                adapter = new PuntuacionsJugadorsAdapter(listJugadors);
                recyclerViewPuntuacions.setAdapter(adapter);
                recyclerViewPuntuacions.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // non implemented
            }
        });
    }

    private String getPuntuacioTotal(List<JugadorPuntuacio> jugadorPuntuacioList) {
        double total = 0;
        for ( JugadorPuntuacio jugadorPuntuacio : jugadorPuntuacioList ) {
            double d = Double.parseDouble(jugadorPuntuacio.getPunts());
            total = total + d;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(total);
    }

    private void initializeVariables() {
        context = this;
        buttonBack = findViewById(R.id.ibBackButtonPuntuacions);
        spinner = findViewById(R.id.spinnerJornadesPuntuacions);
        recyclerViewPuntuacions = findViewById(R.id.recyclerViewPuntuacions);
        tvPuntuacio = findViewById(R.id.tvPuntsJornada);

        tvAlineacio = findViewById(R.id.tvAlineacioPuntuacions);
        List<JugadorPuntuacio> listPuntuacionsJugadors = new ArrayList<>();
        adapter = new PuntuacionsJugadorsAdapter(listPuntuacionsJugadors);
        recyclerViewPuntuacions.setAdapter(adapter);
        recyclerViewPuntuacions.setLayoutManager(new LinearLayoutManager(this));

        bPuntuacionsJoc = findViewById(R.id.bPuntuacionsJoc);
        firebaseOperationsPuntuacionsPage = new FirebaseOperationsPuntuacionsPage(context);
    }
}