package com.tfg_project.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.clases.JugadorResultat;
import com.tfg_project.clases.LliguesVirtuals;
import com.tfg_project.clases.LliguesVirtualsAdapter;
import com.tfg_project.clases.RecyclerItemClickListener;
import com.tfg_project.services.ServicePuntuacions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuPrincipal extends AppCompatActivity {

   /* private static final List<String> listCategoriaGrup = Arrays.asList("primera-catalana1", "primera-catalana2", "primera-catalana3",
            "segona-catalana2", "segona-catalana3", "segona-catalana4", "segona-catalana5", "segona-catalana6",
            "tercera-catalana1", "tercera-catalana2","tercera-catalana3","tercera-catalana4","tercera-catalana5", "tercera-catalana6","tercera-catalana7",
            "tercera-catalana8","tercera-catalana9","tercera-catalana10","tercera-catalana11","tercera-catalana12","tercera-catalana13","tercera-catalana14",
            "tercera-catalana15","tercera-catalana16","tercera-catalana17","tercera-catalana18","tercera-catalana19",
            "quarta-catalana1", "quarta-catalana2", "quarta-catalana3", "quarta-catalana4","quarta-catalana5","quarta-catalana6","quarta-catalana7","quarta-catalana8",
            "quarta-catalana9","quarta-catalana10","quarta-catalana11","quarta-catalana12","quarta-catalana13","quarta-catalana14","quarta-catalana15","quarta-catalana16",
            "quarta-catalana17", "quarta-catalana20","quarta-catalana21","quarta-catalana23","quarta-catalana24","quarta-catalana27","quarta-catalana28","quarta-catalana29",
            "quarta-catalana30");*/

    private static List<String> grups = Arrays.asList("grup2");//, "grup-2", "grup-5", "grup-6", "grup-7", "grup8", "grup9", "grup10", "grup11", "grup13", "grup14", "grup15", "grup16", "grup17", "grup23", "grup24");
    private static List<String> competicions = Arrays.asList("tercera-catalana");

    private static List<String> jornades = Arrays.asList("jornada1", "jornada2","jornada3","jornada4","jornada5","jornada6","jornada7","jornada8", "jornada9", "jornada10", "jornada11", "jornada12");
                                                        //"jornada11","jornada12","jornada13","jornada14","jornada15");//,"12","13","14","15","16","17","18","19",
                                                            //"20","21","22","23","24","25","26","27","28","29","30");//,"31","32","33","34");//, "jornada-2", "jornada-3", "jornada4","jornada5", "jornada6", "jornada7", "jornada8", "jornada9", "jornada10", "jornada11");

    private static final List<Integer> listGrupsCompeticions = Arrays.asList
            (R.array.grupsPrimeraCatalana, R.array.grupsSegonaCatalana,
                    R.array.grupsTerceraCatalana, R.array.grupsQuartaCatalana);
    private ImageButton backButton;
    private ImageButton accountButton;
    private Button crearLligaVirtualButton;
    private Button crearLligaVirtualOK;
    private Button unirLligaVirtualButton;
    private Button unirLligaVirtualOK;

    private FirebaseFirestore firebaseFirestore;
    private String emailUser;
    private AlertDialog alertDialogCrearLliga;
    private AlertDialog alertDialogUnirLliga;
    private AlertDialog alertDialogTancarApp;
    private RecyclerView recyclerView;
    private List<LliguesVirtuals> lliguesVirtualsUser;
    private LliguesVirtualsAdapter adapter;
    private Context context;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initializeVariables();


        ServicePuntuacions servicePuntuacions = new ServicePuntuacions();
        servicePuntuacions.puntuacionsTotals();
        //servicePuntuacions.puntuarJornades();

       /* for ( String comp : competicions ) {
            for ( String grup: grups ) {
                for ( String jornada : jornades ){
                    /*firebaseFirestore.collection("PartitsJugats").document(comp)
                            .collection("Grups").document(grup)
                            .collection("Jornades").document(jornada)
                            .collection("Partits").get().addOnCompleteListener(task -> {
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    System.out.println(documentSnapshot.getId());
                                    documentSnapshot.getReference().collection("JugadorsLocals").get().addOnCompleteListener(task1 -> {
                                        for ( DocumentSnapshot documentSnapshot1 : task1.getResult().getDocuments() ){
                                            System.out.println(documentSnapshot1.getId() + comp + grup + jornada);
                                            String nomJugador = documentSnapshot1.getId();
                                            int golesEncajados = 0;
                                            try{
                                                golesEncajados = ((Long) documentSnapshot1.get("GolesEncajados")).intValue();
                                            } catch (Exception e ){

                                            }
                                            int golesMarcados = ((Long) documentSnapshot1.get("GolesMarcados")).intValue();
                                            int golesPropiaPuerta = ((Long) documentSnapshot1.get("GolesMarcadosPropiaPuerta")).intValue();
                                            int golesMarcadosPenalti = ((Long) documentSnapshot1.get("GolesMarcadosPenalti")).intValue();
                                            int minutFi = 0, minutInici = 0;
                                            try {
                                                minutFi = ((Long) documentSnapshot1.get("MinutFi")).intValue();
                                                minutInici = ((Long) documentSnapshot1.get("MinutInici")).intValue();
                                            } catch (Exception e){

                                            }
                                            boolean portero = (boolean) documentSnapshot1.get("Portero");
                                            boolean tarjetaAmarilla1 = (boolean) documentSnapshot1.get("TarjetaAmarilla1");
                                            boolean tarjetaAmarilla2 = (boolean) documentSnapshot1.get("TarjetaAmarilla2");
                                            boolean tarjetaRoja = (boolean) documentSnapshot1.get("TarjetaRoja");
                                            int minutsJugats = 0;
                                            if ( minutInici != -1 ) {
                                                if ( minutFi == -1 ) {
                                                    minutsJugats = 90 - minutInici;
                                                } else {
                                                    minutsJugats = minutFi - minutInici;
                                                }
                                            }
                                            puntuaJugador(new JugadorResultat(nomJugador, minutsJugats,
                                                    tarjetaAmarilla1, tarjetaAmarilla2, tarjetaRoja,
                                                    golesMarcados, golesMarcadosPenalti, golesPropiaPuerta,
                                                    portero, golesEncajados), comp, grup, jornada);                                        }
                                    });
                                    documentSnapshot.getReference().collection("JugadorsVisitant").get().addOnCompleteListener(task2 -> {
                                        for ( DocumentSnapshot documentSnapshot2 : task2.getResult().getDocuments() ){
                                            System.out.println(documentSnapshot2.getId());
                                            String nomJugador = documentSnapshot2.getId();
                                            int golesEncajados = 0;
                                            try{
                                                golesEncajados = ((Long) documentSnapshot2.get("GolesEncajados")).intValue();
                                            } catch (Exception e ){

                                            }
                                            int golesMarcados = ((Long) documentSnapshot2.get("GolesMarcados")).intValue();
                                            int golesPropiaPuerta = ((Long) documentSnapshot2.get("GolesMarcadosPropiaPuerta")).intValue();
                                            int golesMarcadosPenalti = ((Long) documentSnapshot2.get("GolesMarcadosPenalti")).intValue();
                                            int minutFi = 0, minutInici = 0;
                                            try {
                                                minutFi = ((Long) documentSnapshot2.get("MinutFi")).intValue();
                                                minutInici = ((Long) documentSnapshot2.get("MinutInici")).intValue();
                                            } catch (Exception e){

                                            }
                                            boolean portero = (boolean) documentSnapshot2.get("Portero");
                                            boolean tarjetaAmarilla1 = (boolean) documentSnapshot2.get("TarjetaAmarilla1");
                                            boolean tarjetaAmarilla2 = (boolean) documentSnapshot2.get("TarjetaAmarilla2");
                                            boolean tarjetaRoja = (boolean) documentSnapshot2.get("TarjetaRoja");
                                            int minutsJugats = 0;
                                            if ( minutInici != -1 ) {
                                                if ( minutFi == -1 ) {
                                                    minutsJugats = 90 - minutInici;
                                                } else {
                                                    minutsJugats = minutFi - minutInici;
                                                }
                                            }
                                            puntuaJugador(new JugadorResultat(nomJugador, minutsJugats,
                                                    tarjetaAmarilla1, tarjetaAmarilla2, tarjetaRoja,
                                                    golesMarcados, golesMarcadosPenalti, golesPropiaPuerta,
                                                    portero, golesEncajados), comp, grup, jornada);
                                        }
                                    });
                                }
                            });*/

                  /*  firebaseFirestore.collection("PartitsJugats").document(comp)
                            .collection("Grups").document(grup)
                            .collection("Jornades").document(jornada).collection("Partits").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String acta = (String) documentSnapshot.get("acta");
                                ServicePartitsJugadors servicePartitsJugadors = new ServicePartitsJugadors();
                                servicePartitsJugadors.ejecutar(acta, grup, comp, jornada);
                            }
                        }
                    });
                }
            }
        }*/


        setAdapterLliguesVirtuals();
        getLliguesVirtualsUsuari();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogTancarApp.show();
            }
        });

        accountButton.setOnClickListener(view -> {
            goToAccountPage();
        });

        crearLligaVirtualButton.setOnClickListener(view -> {
            createAlertDialogCrearLligaVirtual();
        });

        unirLligaVirtualButton.setOnClickListener(view -> {
            createAlertDialogUnirseLligaVirtual();
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goToMenuLligaVirtual(lliguesVirtualsUser.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void puntuaJugador(JugadorResultat jugadorResultat, String comp, String grup, String jorn) {
        double punts = 0;
        punts = punts + jugadorResultat.getMinutsJugats()*0.01;
        punts = punts + jugadorResultat.getGolesMarcados()*10;
        punts = punts + jugadorResultat.getGolesMarcadosPenalti()*7.5;
        punts = punts - jugadorResultat.getGolesMarcadosPropia()*5;
        if ( jugadorResultat.isTarjetaAmarilla1() ) punts = punts - 3;
        if ( jugadorResultat.isTarjetaAmarilla2() ) punts = punts - 3;
        if ( jugadorResultat.isTarjetaRoja1() ) punts = punts - 10;
        if ( jugadorResultat.isPortero() ) {
            if (jugadorResultat.getMinutsJugats()>0 && jugadorResultat.getGolesEncajados() == 0){
                punts = punts + 10;
            } else {
                punts = punts - jugadorResultat.getGolesEncajados()*1.5;
            }
            Map<String, String> mapPuntuacio = new HashMap<>();
            mapPuntuacio.put("POR", String.valueOf(punts));
            firebaseFirestore.collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn)
                    .collection("Jugadors").document(jugadorResultat.getNom()).set(mapPuntuacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("PORTER PUNTUAT");
                }
            });
        } else {
            Map<String, String> mapPuntuacio = new HashMap<>();
            mapPuntuacio.put("DC", String.valueOf(punts));
            mapPuntuacio.put("MC", String.valueOf(punts));
            if (jugadorResultat.getMinutsJugats()>0 && jugadorResultat.getGolesEncajados() == 0){
                punts = punts + 7.5;
            } else {
                punts = punts - jugadorResultat.getGolesEncajados();
            }
            mapPuntuacio.put("DFC", String.valueOf(punts));
            firebaseFirestore.collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn)
                    .collection("Jugadors").document(jugadorResultat.getNom()).set(mapPuntuacio).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("JUGADOR PUNTUAT");
                }
            });
        }

    }

    private void consultaDates(List<String> dates, String competicio, String grup, String jornada) {
        List<Date> datesList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for ( String data : dates) {
            if (data != null && !"".equals(data)) {
                try {
                    Date date = simpleDateFormat.parse(data);
                    datesList.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Date dateMin = Collections.min(datesList);
        Date dateMax = Collections.max(datesList);

        String dateMinString = simpleDateFormat.format(dateMin);
        String dateMaxString = simpleDateFormat.format(dateMax);

        Map<String, String> mapData = new HashMap<>();
        mapData.put("dataInici", dateMinString);
        mapData.put("dataFi", dateMaxString);
        firebaseFirestore.collection("PartitsJugats").document(competicio)
                .collection("Grups").document(grup)
                .collection("Jornades").document(jornada).set(mapData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("completed");
            }
        });
    }

    private void executeForEachDocument(String acta) {
    }

    private void goToMenuLligaVirtual(LliguesVirtuals lliga){
        Intent intent = new Intent(this, MenuLligaVirtual.class);
        intent.putExtra("nomLliga", lliga.getNomLligaVirtual());
        intent.putExtra("competicio", lliga.getCompeticio());
        intent.putExtra("grup", lliga.getGrup());
        intent.putExtra("participants", lliga.getParticipants());
        startActivity(intent);
    };

    @Override
    public void onBackPressed(){
        alertDialogTancarApp.show();
    }

    private void setAdapterLliguesVirtuals() {
        adapter = new LliguesVirtualsAdapter(lliguesVirtualsUser);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getLliguesVirtualsUsuari() {
        firebaseFirestore.collection("UsuariLligaVirtual").document(emailUser).get().addOnCompleteListener(task -> {
            ArrayList<String> listLligues = (ArrayList<String>) task.getResult().get("Lligues");
            if (listLligues != null){
                for (String nomLliga : listLligues) {
                    if (!nomLliga.equals("")) {
                        addInfoLliga(nomLliga);
                    }
                }
            }
        });
    }

    private void addInfoLliga(String nomLlVirtual) {
        firebaseFirestore.collection("LliguesVirtuals").document(nomLlVirtual).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if ( task.isSuccessful() ) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String competicio = (String) documentSnapshot.get("competicio");
                    String grup = (String) documentSnapshot.get("grup");
                    ArrayList<String> participants = (ArrayList<String>) documentSnapshot.get("usuaris");
                    if ( participants != null ) {
                        lliguesVirtualsUser.add(new LliguesVirtuals(nomLlVirtual, String.valueOf(participants.size()), competicio, grup));
                    }
                    else lliguesVirtualsUser.add(new LliguesVirtuals(nomLlVirtual, "1", competicio, grup));
                    adapter.notifyItemInserted(lliguesVirtualsUser.size()-1);
                }
            }
        });
    }

    private void createAlertDialogUnirseLligaVirtual(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertDialogView = inflater.inflate(R.layout.linear_layout_unir_lliga_virtual, null);

        EditText password = alertDialogView.findViewById(R.id.etPassword_UnirLligaVirtual);
        EditText nomLligaVirtual = alertDialogView.findViewById(R.id.etUsername_UnirLligaVirtual);

        unirLligaVirtualOK = alertDialogView.findViewById(R.id.bUnirLligaOK);
        unirLligaVirtualOK.setOnClickListener(view -> {
            firebaseFirestore.collection("LliguesVirtuals").document(nomLligaVirtual.getText().toString()).get().addOnCompleteListener(task -> {
                if ( task.isSuccessful() ) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String passwordDB = (String) documentSnapshot.get("password");
                    if (password.getText().toString().equals(passwordDB)){
                        makeToast("Has sigut afegit a la lliga virtual!");
                        lliguesVirtualsUser.add(new LliguesVirtuals(nomLligaVirtual.getText().toString(), String.valueOf(lliguesVirtualsUser.size()+1), (String) documentSnapshot.get("competicio"), (String) documentSnapshot.get("grup")));
                        adapter.notifyItemInserted(lliguesVirtualsUser.size()-1);
                        putNewUserToLligaVirtual(nomLligaVirtual.getText().toString());
                    } else {
                        makeToast("El nom de la lliga o la contrasenya no són correctes");
                    }
                }
                else {
                    makeToast("El nom de la lliga o la contrasenya no són correctes");
                }
            });
            alertDialogUnirLliga.dismiss();
        });

        builder.setView(alertDialogView);
        builder.setTitle("Unir-se Lliga Virtual");
        alertDialogUnirLliga = builder.create();
        alertDialogUnirLliga.show();
    }

    private void putNewUserToLligaVirtual(String nomLligaVirtual) {
        DocumentReference documentReference = firebaseFirestore.collection("UsuariLligaVirtual").document(emailUser);
        documentReference.update("Lligues", FieldValue.arrayUnion(nomLligaVirtual)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("TAG","CORRECT");
                } else {
                    Log.d("TAG", "INCORRECT");
                }
            }
        });
        DocumentReference documentReference1 = firebaseFirestore.collection("LliguesVirtuals").document(nomLligaVirtual);
        documentReference1.update("usuaris", FieldValue.arrayUnion(emailUser)).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d("TAG","CORRECT");
            } else {
                Log.d("TAG", "INCORRECT");
            }
        });
        Map<String, String> mapPunts = new HashMap<>();
        mapPunts.put("punts", "0");
        documentReference1.collection("Classificacio").document(emailUser).set(mapPunts).addOnCompleteListener(task -> {

        });
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showError() {
    }

    private void createAlertDialogCrearLligaVirtual() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            Map<String,Object> map = new HashMap<>();
            map.put("nomLligaVirtual", nomLligaVirtual.getText().toString());
            map.put("password", password.getText().toString());
            map.put("competicio", spinnerCompeticio.getSelectedItem().toString());
            map.put("grup", spinnerGrups.getSelectedItem().toString());
            map.put("maxParticipants", spinnerMaxParticipants.getSelectedItem().toString());
            List<String> listUsuari = Arrays.asList(emailUser);
            map.put("usuaris", listUsuari);
            firebaseFirestore.collection("LliguesVirtuals").document(nomLligaVirtual.getText().toString()).set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("TAG", "TASK COMPLETED SUCCESSFULLY");
                }
            });
            Map<String, String> mapPunts = new HashMap<>();
            mapPunts.put("punts", "0");
            firebaseFirestore.collection("LliguesVirtuals").document(nomLligaVirtual.getText().toString()).collection("Classificacio").document(emailUser).set(mapPunts).addOnCompleteListener(task -> {

            });
            firebaseFirestore.collection("UsuariLligaVirtual").document(emailUser).update("Lligues", FieldValue.arrayUnion(nomLligaVirtual.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( task.isSuccessful() ) {
                        Log.d("TAG", "TASK LLIGUES SUCCESSFuLL");
                    }
                }
            });
            lliguesVirtualsUser.add(new LliguesVirtuals(nomLligaVirtual.getText().toString(), "1", spinnerCompeticio.getSelectedItem().toString(), spinnerGrups.getSelectedItem().toString()));
            adapter.notifyItemInserted(lliguesVirtualsUser.size()-1);
            alertDialogCrearLliga.dismiss();
        });

        builder.setView(alertDialogView);
        builder.setTitle("Crear Lliga Virtual");
        alertDialogCrearLliga = builder.create();
        alertDialogCrearLliga.show();
    }

    private void goToAccountPage() {
        Intent intent = new Intent(this, PerfilPage.class);
        intent.putExtra("mail", emailUser);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void initializeVariables() {
        backButton = findViewById(R.id.backButtonMenuPrincipal);
        accountButton = findViewById(R.id.accountButton);
        crearLligaVirtualButton = findViewById(R.id.bCrearLligaVirtual);
        unirLligaVirtualButton = findViewById(R.id.bUnirLligaVirtual);
        firebaseFirestore = FirebaseFirestore.getInstance();
        emailUser = getIntent().getExtras().getString("email");
        username = getIntent().getExtras().getString("username");

        recyclerView = findViewById(R.id.recyclerView_llistaLligues);
        lliguesVirtualsUser = new ArrayList<>();
        context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Vols tancar l'aplicació?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialogTancarApp.dismiss();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialogTancarApp.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        alertDialogTancarApp = builder.create();
    }
}