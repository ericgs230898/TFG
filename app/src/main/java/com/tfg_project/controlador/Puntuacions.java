package com.tfg_project.controlador;

import android.content.Context;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puntuacions extends AppCompatActivity {

    private static final String TAG = "PUNTUACIONS_PAGE_TAG";

    private ImageButton buttonBack;
    private Spinner spinner;
    private RecyclerView recyclerViewPuntuacions;
    private TextView tvPuntuacio;
    private Map<String, List<JugadorPuntuacio>> mapListPuntuacionsJugadors;
    private List<JugadorPuntuacio> listPuntuacionsJugadors;
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

        UtilsProject utilsProject = new UtilsProject(this);

        countJornades = 0;
        initializeVariables();
        LoadingDialog loadingDialog = LoadingDialog.getInstance(this);
        String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String nomLligaVirtual = this.getIntent().getStringExtra("nomLligaVirtual");

        Task<DocumentSnapshot> taskPuntuacionsJoc = firebaseOperationsPuntuacionsPage.getPuntuacionsJoc();
        Tasks.whenAllComplete(taskPuntuacionsJoc).addOnCompleteListener(task -> {
            mapPuntuacionsJoc = firebaseOperationsPuntuacionsPage.getMapPuntuacionsJoc();
        });

        mapPuntuacions = new HashMap<>();
        jornadesPossibles = new ArrayList<>();
        loadingDialog.startLoadingDialog();
        FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual)
                .collection("Jornada").get().addOnCompleteListener(task -> {
            if (task.getResult().getDocuments().isEmpty()) {
                loadingDialog.dissmisDialog();
                utilsProject.makeToast("No hi ha cap puntuació guardada");
            }
            else {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                int sizeJornades = task.getResult().getDocuments().size();
                String jornada = doc.getId();
                doc.getReference().collection(mail).document("Plantilla").get().addOnCompleteListener(task1 -> {
                    if ((boolean) task1.getResult().get("puntuat")) {
                        jornadesPossibles.add(jornada);
                        String alineacio = (String) task1.getResult().get("alineacio");
                        List<JugadorPuntuacio> jugadorPuntuacioList = new ArrayList<>();
                        doc.getReference().collection(mail).document("Plantilla").collection("Jugadors").get().addOnCompleteListener(task2 -> {
                            for (DocumentSnapshot doc1 : task2.getResult().getDocuments()) {
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

            TextView tvGolsMarcats, tvGolsMarcatsPropia, tvGolsMarcatsPenalti,
                    tvGolRebutDefensa, tvGolRebutPorter, tvMinutJugat, tvPorteria0Defensa,
                    tvPorteria0Porter, tvTarjetaAmarilla, tvTarjetaRoja;

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
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                builder.create().dismiss();
            });
            builder.create().show();
        });


    }

    private void setRecyclerView() {
        Collections.sort(jornadesPossibles, (s, t1) -> {
            int num1 = Integer.parseInt(s.substring(8));
            int num2 = Integer.parseInt(t1.substring(8));
            if ( num1 < num2 ) return -1;
            else if ( num1 == num2) return 0;
            else return 1;
        });
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jornadesPossibles);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornada = jornadesPossibles.get(i);
                AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = mapPuntuacions.get(jornada);
                tvAlineacio.setText(new StringBuilder().append("Alineacio: ").append(alineacioJugadorPuntuacio.getAlineacio()).toString());
                String puntuacio = getPuntuacioTotal(alineacioJugadorPuntuacio.getJugadorPuntuacioList());
                tvPuntuacio.setText(new StringBuilder().append("Punts totals: ").append(puntuacio.replace(",", ".")).toString());
                List<JugadorPuntuacio> listJugadors = alineacioJugadorPuntuacio.getJugadorPuntuacioList();
                Collections.sort(listJugadors, new Comparator<JugadorPuntuacio>() {
                    @Override
                    public int compare(JugadorPuntuacio jugadorPuntuacio, JugadorPuntuacio t1) {
                        if (jugadorPuntuacio.getPosicio().equals("POR")){
                            return -1;
                        } else if ( jugadorPuntuacio.getPosicio().equals("DFC")){
                            if (t1.getPosicio().equals("MC") || t1.getPosicio().equals("DC")) return -1;
                            else if ( t1.getPosicio().equals("DFC")) return 0;
                            else return 1;
                        }
                        else if ( jugadorPuntuacio.getPosicio().equals("MC")){
                            if (t1.getPosicio().equals("DC")) return -1;
                            else if ( t1.getPosicio().equals("MC")) return 0;
                            else return 1;
                        }
                        else if ( jugadorPuntuacio.getPosicio().equals("DC")){
                            if (t1.getPosicio().equals("DC")) return 0;
                            else return 1;
                        }
                        return 0;
                    }
                });
                adapter = new PuntuacionsJugadorsAdapter(listJugadors);
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
            double d = Double.parseDouble(jugadorPuntuacio.getPunts());
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
                    if (!punts.equals("")){
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

        bPuntuacionsJoc = findViewById(R.id.bPuntuacionsJoc);
        firebaseOperationsPuntuacionsPage = new FirebaseOperationsPuntuacionsPage(context);
    }
}