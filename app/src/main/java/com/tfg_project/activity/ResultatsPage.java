package com.tfg_project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfg_project.R;
import com.tfg_project.clases.Jornada;
import com.tfg_project.clases.PartitJugador;
import com.tfg_project.clases.Resultat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultatsPage extends AppCompatActivity {

    private Spinner spinnerJornades;
    private FirebaseFirestore firebaseFirestore;
    private String competicio;
    private String grup;
    private LinearLayout linearLayoutResultats;
    private List<Jornada> jornades;
    private ImageButton backButton;
    private Context context;
    private String sizeJornades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_page);

        initializeVariables();
        setSpinnerJornades(Integer.valueOf(sizeJornades));
        initializeSpinner();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void goToAnotherActivity(Object pageClass, Map<String,String> map) {
        Intent intent = new Intent(this, (Class<?>) pageClass);
        if (map != null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivity(intent);
    }

    private void initializeSpinner() {
        spinnerJornades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornadaSpinner = (String) spinnerJornades.getSelectedItem();
                jornadaSpinner = jornadaSpinner.replaceAll("\\s","");
                jornadaSpinner = jornadaSpinner.replaceAll(" ", "");
                jornadaSpinner = jornadaSpinner.toLowerCase();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                for ( Jornada jorn : jornades ) {
                    if ( jorn.getJornada().equals(jornadaSpinner)){
                        for ( Resultat resultat : jorn.getResultats() ) {
                            View myView = inflater.inflate(R.layout.linear_layout_resultat, null);
                            TextView tvLocal = myView.findViewById(R.id.tvEquipLocal);
                            TextView tvResultat = myView.findViewById(R.id.tvResultats);
                            TextView tvVisitante = myView.findViewById(R.id.tvEquipVisitante);
                            String equipLocal = resultat.getEquipLocal();
                            int golesLocal = resultat.getGolesLocal();
                            int golesVisitant = resultat.getGolesVisitant();
                            if ( equipLocal.length() < 1 ||equipLocal.equals("")){
                                tvLocal.setText("Descansa");
                            } else {
                                tvLocal.setText(resultat.getEquipLocal());
                            }
                            String equipVisitant = resultat.getEquipVisitant();
                            if ( resultat.getEquipVisitant().equals("")){
                                tvVisitante.setText("Descansa");
                            } else {
                                tvVisitante.setText(resultat.getEquipVisitant());
                            }
                            tvLocal.setText(resultat.getEquipLocal());
                            tvVisitante.setText(resultat.getEquipVisitant());
                            String condicio = resultat.getCondicio();
                            if ( condicio.equals("NJ")) {
                                tvResultat.setText("NJ");
                            }
                            else if ( condicio.equals("-")) {
                                tvResultat.setText(new StringBuilder().append(String.valueOf(resultat.getGolesLocal())).append("-").append(String.valueOf(resultat.getGolesVisitant())).toString());
                            }
                            else if ( condicio.equals("D")){
                                tvResultat.setText("D");
                            } else if ( condicio.equals("R")){
                                tvResultat.setText("R");
                            } else {
                                tvResultat.setText("");
                            }
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 50, 0, 50);
                            myView.setLayoutParams(params);
                            String finalJornadaSpinner = jornadaSpinner;
                            myView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ( !condicio.equals("-")) {
                                        makeToast("No hi ha dades d'aquest partit");
                                    }
                                    else {
                                        System.out.println("CONDICION --> " + condicio);
                                        Map<String, String> map = new HashMap<>();
                                        map.put("grup", grup);
                                        map.put("competicio", competicio);
                                        map.put("jornada", finalJornadaSpinner);
                                        map.put("equipLocal", equipLocal);
                                        map.put("equipVisitant", equipVisitant);
                                        map.put("golesLocal", String.valueOf(golesLocal));
                                        map.put("golesVisitant", String.valueOf(golesVisitant));
                                        DocumentReference doc = firebaseFirestore
                                                .collection("PartitsJugats")
                                                .document(competicio).collection("Grups")
                                                .document("grup" + grup)
                                                .collection("Jornades")
                                                .document(finalJornadaSpinner)
                                                .collection("Partits")
                                                .document(equipLocal + "-" + equipVisitant);
                                        List<PartitJugador> jugadorsLocal = new ArrayList<>();
                                        List<PartitJugador> jugadorsVisitant = new ArrayList<>();
                                        final boolean[] jugLocals = {false};
                                        final boolean[] jugVisitant = {false};
                                        doc.collection("JugadorsLocals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                                    String nomJugador = documentSnapshot.getId();
                                                    String docEquipLocal = (String) documentSnapshot.get("EquipLocal");
                                                    String docEquipVisitant = (String) documentSnapshot.get("EquipVisitant");
                                                    int golesMarcados = ((Long) documentSnapshot.get("GolesMarcados")).intValue();
                                                    int golesMarcadosPenalti = ((Long) documentSnapshot.get("GolesMarcadosPenalti")).intValue();
                                                    int golesMarcadosPropiaPuerta = ((Long) documentSnapshot.get("GolesMarcadosPropiaPuerta")).intValue();
                                                    int golesEncajados = 0;
                                                    try {
                                                        golesEncajados = ((Long) documentSnapshot.get("GolesEncajados")).intValue();
                                                    } catch (Exception e) {
                                                        Log.e("TAG", e.getMessage());
                                                    }
                                                    //int minutosJugados = ((Long) documentSnapshot.get("MinutosJugados")).intValue();
                                                    int minutInici = ((Long) documentSnapshot.get("MinutInici")).intValue();
                                                    int minutFinal = ((Long) documentSnapshot.get("MinutFi")).intValue();
                                                    boolean porteriaA0 = (Boolean) documentSnapshot.get("PorteriaA0");
                                                    boolean portero = (Boolean) documentSnapshot.get("Portero");
                                                    boolean amarilla1 = (Boolean) documentSnapshot.get("TarjetaAmarilla1");
                                                    boolean amarilla2 = (Boolean) documentSnapshot.get("TarjetaAmarilla2");
                                                    boolean roja = (Boolean) documentSnapshot.get("TarjetaRoja");
                                                    jugadorsLocal.add(new PartitJugador(nomJugador, docEquipLocal, docEquipVisitant,
                                                            golesMarcados, golesMarcadosPropiaPuerta, golesMarcadosPenalti, golesEncajados,
                                                            amarilla1, amarilla2, roja, portero, porteriaA0, minutInici, minutFinal));
                                                }
                                                if (task.getResult().getDocuments().size() == jugadorsLocal.size())
                                                    jugLocals[0] = true;
                                                if (jugVisitant[0] && jugLocals[0]) {
                                                    String gsonLocals = new Gson().toJson(jugadorsLocal);
                                                    String gsonVisitants = new Gson().toJson(jugadorsVisitant);
                                                    map.put("loc", gsonLocals);
                                                    map.put("vis", gsonVisitants);
                                                    goToAnotherActivity(ResultatsPartitPage.class, map);
                                                }

                                            }
                                        });
                                        doc.collection("JugadorsVisitant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                                    String nomJugador = documentSnapshot.getId();
                                                    String docEquipLocal = (String) documentSnapshot.get("EquipLocal");
                                                    String docEquipVisitant = (String) documentSnapshot.get("EquipVisitant");
                                                    int golesMarcados = ((Long) documentSnapshot.get("GolesMarcados")).intValue();
                                                    int golesMarcadosPenalti = ((Long) documentSnapshot.get("GolesMarcadosPenalti")).intValue();
                                                    int golesMarcadosPropiaPuerta = ((Long) documentSnapshot.get("GolesMarcadosPropiaPuerta")).intValue();
                                                    int golesEncajados = 0;
                                                    try {
                                                        golesEncajados = ((Long) documentSnapshot.get("GolesEncajados")).intValue();
                                                    } catch (Exception e) {
                                                        Log.e("TAG", e.getMessage());
                                                    }
                                                    //int minutosJugados = ((Long) documentSnapshot.get("MinutosJugados")).intValue();
                                                    int minutInici = ((Long) documentSnapshot.get("MinutInici")).intValue();
                                                    int minutFinal = ((Long) documentSnapshot.get("MinutFi")).intValue();
                                                    boolean porteriaA0 = (Boolean) documentSnapshot.get("PorteriaA0");
                                                    boolean portero = (Boolean) documentSnapshot.get("Portero");
                                                    boolean amarilla1 = (Boolean) documentSnapshot.get("TarjetaAmarilla1");
                                                    boolean amarilla2 = (Boolean) documentSnapshot.get("TarjetaAmarilla2");
                                                    boolean roja = (Boolean) documentSnapshot.get("TarjetaRoja");
                                                    jugadorsVisitant.add(new PartitJugador(nomJugador, docEquipLocal, docEquipVisitant,
                                                            golesMarcados, golesMarcadosPropiaPuerta, golesMarcadosPenalti, golesEncajados,
                                                            amarilla1, amarilla2, roja, portero, porteriaA0, minutInici, minutFinal));
                                                }
                                                if (task.getResult().getDocuments().size() == jugadorsVisitant.size())
                                                    jugVisitant[0] = true;
                                                if (jugVisitant[0] && jugLocals[0]) {
                                                    String gsonLocals = new Gson().toJson(jugadorsLocal);
                                                    String gsonVisitants = new Gson().toJson(jugadorsVisitant);
                                                    map.put("loc", gsonLocals);
                                                    map.put("vis", gsonVisitants);
                                                    goToAnotherActivity(ResultatsPartitPage.class, map);
                                                }

                                            }
                                        });
                                    }

                                }
                            });
                            linearLayout.addView(myView);
                        }
                    }

                }
                linearLayoutResultats.removeAllViews();
                linearLayoutResultats.addView(linearLayout);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerJornades(int size) {
        List<String> listJornades = new ArrayList<>();
        for ( int i=1; i<=size; i++ ) {
            listJornades.add("Jornada " + i);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listJornades);
        spinnerJornades.setAdapter(spinnerArrayAdapter);
    }

    private void makeToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void initializeVariables() {
        context = this;
        linearLayoutResultats = findViewById(R.id.linearLayoutResultats);

        backButton = findViewById(R.id.resultatsBackButton);

        spinnerJornades = findViewById(R.id.spinnerJornades);
        firebaseFirestore = FirebaseFirestore.getInstance();

        competicio = this.getIntent().getStringExtra("competicio");
        grup = this.getIntent().getStringExtra("grup");
        sizeJornades = this.getIntent().getStringExtra("sizeJornades");
        String jornadesJson = this.getIntent().getStringExtra("jornades");
        Type listType = new TypeToken<ArrayList<Jornada>>(){}.getType();
        jornades = new Gson().fromJson(jornadesJson, listType);
        System.out.println(jornadesJson);
        System.out.println(sizeJornades);

    }
}