package com.tfg_project.controlador;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.tfg_project.R;
import com.tfg_project.model.beans.ClassificacioBean;
import com.tfg_project.model.beans.Jornada;
import com.tfg_project.model.beans.LoadingDialog;
import com.tfg_project.model.beans.Resultat;
import com.tfg_project.model.adapters.ClassificacioAdapter;
import com.tfg_project.model.adapters.ClassificacioAdapter2;
import com.tfg_project.model.utils.UtilsProject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuLligaVirtual extends AppCompatActivity {

    private static final String COMPETICIO_CONSTANT = "competicio";
    private static final String JORNADES_CONSTANT = "jornades";

    private Button bGoToPlantilla;
    private Button bGoToResultats;
    private String grup;
    private String competicio;
    private ImageButton goBackButton;
    private List<Jornada> jornades;
    private Map<String, String> mapResultat;

    private String nomLligaVirtual;
    private Button bGoToPuntuacions;
    private RecyclerView recyclerViewClassificacio;
    private RecyclerView recyclerViewClassificacio2;
    private Activity activity;
    private LoadingDialog loadingDialog;
    private UtilsProject utilsProject;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lliga_virtual);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();

        goBackButton.setOnClickListener(view -> finish());
        jornades = new ArrayList<>();

        bGoToPuntuacions.setOnClickListener(view -> {
            Map<String, String> mapAux = new HashMap<>();
            mapAux.put("nomLligaVirtual", nomLligaVirtual);
            utilsProject.goToAnotherActivity(Puntuacions.class, mapAux);
            /*
            FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual)
                    .collection("Jornada").get().addOnCompleteListener(task -> {
                        int sizeDocuments = task.getResult().getDocuments().size();
                        if ( sizeDocuments == 0 ) {
                            Map<String, String> mapAux = new HashMap<>();
                            mapAux.put("grup", grup);
                            mapAux.put("competicio", competicio);
                            goToAnotherActivity(Puntuacions.class, mapAux);
                        }
                        int sizeDocs = task.getResult().getDocuments().size();
                        final int[] countDocs = {0};
                        for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            documentSnapshot.getReference().collection(userMail).document("Plantilla")
                                    .get().addOnCompleteListener(task1 -> {
                                        countSize++;
                                        boolean puntuat = (boolean) task1.getResult().get("puntuat");
                                        if ( puntuat ) {
                                            String alineacio = (String) task1.getResult().get("alineacio");
                                            task1.getResult().getReference().collection("Jugadors").get().addOnCompleteListener(task2 -> {
                                                List<JugadorPuntuacio> jugadorPuntuacioList = new ArrayList<>();
                                                for ( DocumentSnapshot documentSnapshot1 : task2.getResult().getDocuments()){
                                                    String jugador = documentSnapshot1.getId();
                                                    String punts = (String) documentSnapshot1.get("punts");
                                                    String posicio = (String) documentSnapshot1.get("posicio");
                                                    JugadorPuntuacio jugadorPuntuacio = new JugadorPuntuacio(posicio, jugador, punts);
                                                    jugadorPuntuacioList.add(jugadorPuntuacio);
                                                }
                                                AlineacioJugadorPuntuacio alineacioJugadorPuntuacio = new AlineacioJugadorPuntuacio(alineacio, jugadorPuntuacioList);
                                                mapPuntuacio.put(documentSnapshot.getId(), alineacioJugadorPuntuacio);
                                            });
                                        } else {
                                            countDocs[0]++;
                                        }
                                    });
                        }
                    });
            //Map<String, String> mapAux = new HashMap<>();

            //goToAnotherActivity(Puntuacions.class, null);*/
        });

        bGoToResultats.setOnClickListener(view -> {
            loadingDialog = new LoadingDialog(activity);
            loadingDialog.startLoadingDialog();
            String comp = competicio.toLowerCase().replace(' ', '-').trim();
            mapResultat.put(COMPETICIO_CONSTANT, comp);
            mapResultat.put("grup", grup);
            mapResultat.put(JORNADES_CONSTANT, "");
            FirebaseFirestore.getInstance().collection("PartitsJugats").document(comp).collection("Grups").document("grup" + grup).collection("Jornades").get().addOnCompleteListener(task -> {
                mapResultat.put("sizeJornades", String.valueOf(Objects.requireNonNull(task.getResult()).getDocuments().size()));
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                executeForEachDocumentResultat(documents);
            });
        });

        bGoToPlantilla.setOnClickListener(view -> {
            Map<String, String> map = new HashMap<>();
            map.put(COMPETICIO_CONSTANT, competicio);
            map.put("grup", grup);
            map.put("nomLligaVirtual", nomLligaVirtual);
            map.put(JORNADES_CONSTANT, String.valueOf(jornades.size()));
            utilsProject.goToAnotherActivity(PlantillaPage.class, map);
        });
        ArrayList<ClassificacioBean> classificacioBeanArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual).collection("Classificacio").get().addOnCompleteListener(task -> {
            int sizeDocs = Objects.requireNonNull(task.getResult()).getDocuments().size();
            final int[] count = {0};
            for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                String emailUser = documentSnapshot.getId();
                double punts = Double.parseDouble((String) documentSnapshot.get("punts"));
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                String puntsString = df.format(punts);
                FirebaseFirestore.getInstance().collection("Usuari").document(emailUser).get().addOnCompleteListener(task1 -> {
                    String username = (String) Objects.requireNonNull(task1.getResult()).get("usuari");
                    classificacioBeanArrayList.add(new ClassificacioBean(username, puntsString));
                    count[0]++;
                    if ( count[0] == sizeDocs ){
                        Collections.sort(classificacioBeanArrayList, (c1, c2) -> {
                            double c1Double = Double.parseDouble(c1.getPunts().replace(",", "."));
                            double c2Double = Double.parseDouble(c2.getPunts().replace(",", "."));
                            return Double.compare(c1Double, c2Double);
                        });
                        if ( classificacioBeanArrayList.size() > 3 ) {
                            ClassificacioAdapter adapter = new ClassificacioAdapter(classificacioBeanArrayList.subList(0,3));
                            recyclerViewClassificacio.setAdapter(adapter);
                            recyclerViewClassificacio.setLayoutManager(new LinearLayoutManager(this));
                            ClassificacioAdapter2 adapter2 = new ClassificacioAdapter2(classificacioBeanArrayList.subList(3,classificacioBeanArrayList.size()));
                            recyclerViewClassificacio2.setAdapter(adapter2);
                            recyclerViewClassificacio2.setLayoutManager(new LinearLayoutManager(this));
                        } else {
                            ClassificacioAdapter adapter = new ClassificacioAdapter(classificacioBeanArrayList);
                            recyclerViewClassificacio.setAdapter(adapter);
                            recyclerViewClassificacio.setLayoutManager(new LinearLayoutManager(this));
                        }

                    }
                });
            }
        });
    }


    private void executeForEachDocumentResultat(List<DocumentSnapshot> documents) {
        final int[] count = {0};
        for (DocumentSnapshot doc : documents) {
            doc.getReference().collection("Partits").get().addOnCompleteListener(task1 -> {
                List<Resultat> resultats = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot1 : task1.getResult().getDocuments()) {
                    String equipLocal = (String) documentSnapshot1.get("equipLocal");
                    String equipVisitant = (String) documentSnapshot1.get("equipVisitant");
                    int golesLocal = ((Long) Objects.requireNonNull(documentSnapshot1.get("golesLocal"))).intValue();
                    int golesVisitant = ((Long) Objects.requireNonNull(documentSnapshot1.get("golesVisitante"))).intValue();
                    String condicio = (String) documentSnapshot1.get("condicion");
                    if ( condicio.equals("D")) {
                        if ( equipLocal.equals("")) {
                            equipLocal = "Descansa";
                        }
                        else equipVisitant = "Descansa";
                    }
                    resultats.add(new Resultat(equipLocal, equipVisitant, golesLocal, golesVisitant, condicio));
                }
                jornades.add(new Jornada(doc.getId(), resultats));
                String json = new Gson().toJson(jornades);
                mapResultat.put(JORNADES_CONSTANT, json);
                count[0]++;
                if ( count[0] == documents.size()) {
                    LoadingDialog.getInstance(activity).dissmisDialog();
                    LoadingDialog.getInstance(activity).eliminateLoadingDialog();
                    utilsProject.goToAnotherActivity(ResultatsPage.class, mapResultat);
                }
            });
        }
    }

    private void initializeVariables() {
        Context context = this;
        utilsProject = new UtilsProject(context);
        activity = this;
        goBackButton = findViewById(R.id.ibGoBack);

        bGoToPuntuacions = findViewById(R.id.bPuntuacionsMenuLligaVirtual);
        bGoToPlantilla = findViewById(R.id.bGoToPlantilla);
        bGoToResultats = findViewById(R.id.bGoToResultats);

        TextView tvNomLligaVirtual = findViewById(R.id.tvMenuNomLligaVirtual);
        TextView tvCompeticioGrup = findViewById(R.id.tvMenuLligaCompeticioGrup);

        nomLligaVirtual = this.getIntent().getStringExtra("nomLliga");
        grup = this.getIntent().getStringExtra("grup");
        competicio = this.getIntent().getStringExtra(COMPETICIO_CONSTANT);

        mapResultat = new HashMap<>();

        tvNomLligaVirtual.setText(nomLligaVirtual);
        tvCompeticioGrup.setText(competicio + " - Grup: " + grup);

        recyclerViewClassificacio = findViewById(R.id.recyclerViewClassificacio);
        recyclerViewClassificacio2 = findViewById(R.id.recyclerViewClassificacio2);
    }

}