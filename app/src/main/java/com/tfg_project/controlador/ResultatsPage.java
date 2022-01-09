package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfg_project.R;
import com.tfg_project.model.beans.Jornada;
import com.tfg_project.model.beans.PartitJugador;
import com.tfg_project.model.beans.Resultat;
import com.tfg_project.model.firestore.FirebaseOperationsResultatsPage;
import com.tfg_project.model.utils.UtilsProject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ResultatsPage extends AppCompatActivity {

    private static final String RETIRAT = "R";
    private static final String DESCANS = "D";

    private Spinner spinnerJornades;
    private String competicio;
    private String grup;
    private LinearLayout linearLayoutResultats;
    private List<Jornada> jornades;
    private ImageButton backButton;
    private Context context;
    private String sizeJornades;
    private UtilsProject utilsProject;
    private FirebaseOperationsResultatsPage firebaseOperationsResultatsPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();
        setSpinnerJornades(Integer.parseInt(sizeJornades));
        initializeSpinner();

        backButton.setOnClickListener(view -> finish());

    }

    private void initializeSpinner() {
        spinnerJornades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornadaSpinner = (String) spinnerJornades.getSelectedItem();
                jornadaSpinner = jornadaSpinner.replace("\\s","");
                jornadaSpinner = jornadaSpinner.replace(" ", "");
                jornadaSpinner = jornadaSpinner.toLowerCase();
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
                            if (equipLocal.length() < 1){
                                tvLocal.setText(getString(R.string.descansa));
                            } else {
                                tvLocal.setText(resultat.getEquipLocal());
                            }
                            String equipVisitant = resultat.getEquipVisitant();
                            if ( resultat.getEquipVisitant().equals("")){
                                tvVisitante.setText(getString(R.string.descansa));
                            } else {
                                tvVisitante.setText(resultat.getEquipVisitant());
                            }
                            tvLocal.setText(resultat.getEquipLocal());
                            tvVisitante.setText(resultat.getEquipVisitant());
                            String condicio = resultat.getCondicio();
                            switch (condicio) {
                                case "NJ":
                                    tvResultat.setText(getString(R.string.no_jugat));
                                    break;
                                case "-":
                                    tvResultat.setText(resultat.getGolesLocal() + "-" + resultat.getGolesVisitant());
                                    break;
                                case DESCANS:
                                    tvResultat.setText(DESCANS);
                                    break;
                                case RETIRAT:
                                    tvResultat.setText(RETIRAT);
                                    break;
                                default:
                                    tvResultat.setText("");
                                    break;
                            }
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                            myView.setLayoutParams(params);
                            String finalJornadaSpinner = jornadaSpinner;
                            myView.setOnClickListener(view1 -> {
                                if ( !condicio.equals("-")) {
                                    utilsProject.makeToast("No hi ha dades d'aquest partit");
                                }
                                else {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("grup", grup);
                                    map.put("competicio", competicio);
                                    map.put("jornada", finalJornadaSpinner);
                                    map.put("equipLocal", equipLocal);
                                    map.put("equipVisitant", equipVisitant);
                                    map.put("golesLocal", String.valueOf(golesLocal));
                                    map.put("golesVisitant", String.valueOf(golesVisitant));

                                    Task<QuerySnapshot> taskLocal = firebaseOperationsResultatsPage.jugadoresLocales(competicio, grup, finalJornadaSpinner, equipLocal, equipVisitant);
                                    Task<QuerySnapshot> taskVisitant = firebaseOperationsResultatsPage.jugadoresVisitantes(competicio, grup, finalJornadaSpinner, equipLocal, equipVisitant);
                                    Tasks.whenAllComplete(Arrays.asList(taskLocal,taskVisitant)).addOnCompleteListener(task -> {
                                        List<PartitJugador> jugadorsLocal = firebaseOperationsResultatsPage.getJugadorsLocal();
                                        List<PartitJugador> jugadorsVisitant = firebaseOperationsResultatsPage.getJugadorsVisitant();
                                        String gsonLocals = new Gson().toJson(jugadorsLocal);
                                        String gsonVisitants = new Gson().toJson(jugadorsVisitant);
                                        map.put("loc", gsonLocals);
                                        map.put("vis", gsonVisitants);
                                        utilsProject.goToAnotherActivity(ResultatsPartitPage.class, map);
                                    });
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
            // non implemented
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

    private void initializeVariables() {
        context = this;
        linearLayoutResultats = findViewById(R.id.linearLayoutResultats);
        firebaseOperationsResultatsPage = new FirebaseOperationsResultatsPage(context);

        backButton = findViewById(R.id.resultatsBackButton);

        spinnerJornades = findViewById(R.id.spinnerJornades);
        spinnerJornades.setBackgroundResource(R.drawable.customborder7);

        competicio = this.getIntent().getStringExtra("competicio");
        grup = this.getIntent().getStringExtra("grup");
        sizeJornades = this.getIntent().getStringExtra("sizeJornades");
        String jornadesJson = this.getIntent().getStringExtra("jornades");
        Type listType = new TypeToken<ArrayList<Jornada>>(){}.getType();
        jornades = new Gson().fromJson(jornadesJson, listType);

        utilsProject = new UtilsProject(context);

    }
}