package com.tfg_project.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.tfg_project.R;
import com.tfg_project.clases.AlineacioJugadorPuntuacio;
import com.tfg_project.clases.ClassificacioAdapter;
import com.tfg_project.clases.ClassificacioBean;
import com.tfg_project.clases.Jornada;
import com.tfg_project.clases.JugadorResultat;
import com.tfg_project.clases.LoadingDialog;
import com.tfg_project.clases.Resultat;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuLligaVirtual extends AppCompatActivity {

    private TextView tvCompeticioGrup;
    private TextView tvNomLligaVirtual;
    private Button bGoToPlantilla;
    private Button bGoToResultats;
    private String grup;
    private String competicio;
    private ImageButton goBackButton;
    private List<Jornada> jornades;
    private Map<String, String> mapResultat;
    private Map<String, AlineacioJugadorPuntuacio> mapPuntuacio;

    private String nomLligaVirtual;
    private Button bGoToPuntuacions;
    private Map<JugadorResultat,String> jugadorsResultat;
    private String userMail;
    private int countSize;
    private RecyclerView recyclerViewClassificacio;
    private Activity activity;
    private LoadingDialog loadingDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lliga_virtual);

        initializeVariables();

        goBackButton.setOnClickListener(view -> {
            finish();
        });
        countSize = 0;
        jornades = new ArrayList<>();
        mapPuntuacio = new HashMap<>();

        bGoToPuntuacions.setOnClickListener(view -> {
            Map<String, String> mapAux = new HashMap<>();
            mapAux.put("nomLligaVirtual", nomLligaVirtual);
            goToAnotherActivity(Puntuacions.class, mapAux);
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
            mapResultat.put("competicio", comp);
            mapResultat.put("grup", grup);
            mapResultat.put("jornades", "");
            FirebaseFirestore.getInstance().collection("PartitsJugats").document(comp).collection("Grups").document("grup" + grup).collection("Jornades").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    mapResultat.put("sizeJornades", String.valueOf(task.getResult().getDocuments().size()));
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    executeForEachDocumentResultat(documents);
                }
            });
        });

        bGoToPlantilla.setOnClickListener(view -> {
            Map<String, String> map = new HashMap<>();
            map.put("competicio", competicio);
            map.put("grup", grup);
            map.put("nomLligaVirtual", nomLligaVirtual);
            map.put("jornades", String.valueOf(jornades.size()));
            goToAnotherActivity(PlantillaPage.class, map);
        });
        ArrayList<ClassificacioBean> classificacioBeanArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual).collection("Classificacio").get().addOnCompleteListener(task -> {
            int sizeDocs = task.getResult().getDocuments().size();
            final int[] count = {0};
            for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                String emailUser = documentSnapshot.getId();
                double punts = (double) documentSnapshot.get("punts");
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                String puntsString = df.format(punts);
                FirebaseFirestore.getInstance().collection("Usuari").document(emailUser).get().addOnCompleteListener(task1 -> {
                    String username = (String) task1.getResult().get("usuari");
                    classificacioBeanArrayList.add(new ClassificacioBean(username, puntsString));
                    count[0]++;
                    if ( count[0] == sizeDocs ){
                        Collections.sort(classificacioBeanArrayList, new Comparator<ClassificacioBean>(){
                            public int compare(ClassificacioBean c1, ClassificacioBean c2 ){
                                Double c1Double = Double.valueOf(c1.getPunts().replace(",","."));
                                Double c2Double = Double.valueOf(c2.getPunts().replace(",","."));

                                if ( c1Double > c2Double){
                                    return -1;
                                }
                                if ( c1Double < c2Double){
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        ClassificacioAdapter adapter = new ClassificacioAdapter(classificacioBeanArrayList);
                        recyclerViewClassificacio.setAdapter(adapter);
                        recyclerViewClassificacio.setLayoutManager(new LinearLayoutManager(this));
                    }
                });
            }
        });
    }


    private void executeForEachDocumentResultat(List<DocumentSnapshot> documents) {
        final int[] count = {0};
        for (DocumentSnapshot doc : documents) {
            doc.getReference().collection("Partits").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NewApi")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                    List<Resultat> resultats = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot1 : task1.getResult().getDocuments()) {
                        String equipLocal = (String) documentSnapshot1.get("equipLocal");
                        String equipVisitant = (String) documentSnapshot1.get("equipVisitant");
                        int golesLocal = ((Long) documentSnapshot1.get("golesLocal")).intValue();
                        int golesVisitant = ((Long) documentSnapshot1.get("golesVisitante")).intValue();
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
                    mapResultat.put("jornades", json);
                    count[0]++;
                    if ( count[0] == documents.size()) {
                        loadingDialog.dissmisDialog();
                        goToAnotherActivity(ResultatsPage.class, mapResultat);
                    }
                }
            });
        }
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

    private void makeToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void initializeVariables() {
        System.out.println("LLEGO");
        activity = this;
        userMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        jugadorsResultat = new HashMap<>();
        goBackButton = findViewById(R.id.ibGoBack);

        bGoToPuntuacions = findViewById(R.id.bPuntuacionsMenuLligaVirtual);
        bGoToPlantilla = findViewById(R.id.bGoToPlantilla);
        bGoToResultats = findViewById(R.id.bGoToResultats);

        tvNomLligaVirtual = findViewById(R.id.tvMenuNomLligaVirtual);
        tvCompeticioGrup = findViewById(R.id.tvMenuLligaCompeticioGrup);

        nomLligaVirtual = this.getIntent().getStringExtra("nomLliga");
        grup = this.getIntent().getStringExtra("grup");
        competicio = this.getIntent().getStringExtra("competicio");

        mapResultat = new HashMap<>();

        tvNomLligaVirtual.setText(nomLligaVirtual);
        tvCompeticioGrup.setText(new StringBuilder().append(competicio).append(" - Grup: ").append(grup).toString());

        recyclerViewClassificacio = findViewById(R.id.recyclerViewClassificacio);
    }

}