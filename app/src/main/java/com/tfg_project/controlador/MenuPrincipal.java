package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tfg_project.R;
import com.tfg_project.model.beans.LliguesVirtuals;
import com.tfg_project.model.adapters.LliguesVirtualsAdapter;
import com.tfg_project.model.adapters.RecyclerItemClickListener;
import com.tfg_project.model.firestore.FirebaseOperationsMenuPrincipal;
import com.tfg_project.model.services.ServicePuntuacions;
import com.tfg_project.model.utils.UtilsProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuPrincipal extends AppCompatActivity {
    private static final List<Integer> listGrupsCompeticions = Arrays.asList
            (R.array.grupsPrimeraCatalana, R.array.grupsSegonaCatalana,
                    R.array.grupsTerceraCatalana, R.array.grupsQuartaCatalana);
    private ImageButton backButton;
    private ImageButton accountButton;
    private Button crearLligaVirtualButton;
    private Button unirLligaVirtualButton;

    private String emailUser;
    private AlertDialog alertDialogCrearLliga;
    private AlertDialog alertDialogUnirLliga;
    private AlertDialog alertDialogTancarApp;
    private RecyclerView recyclerView;
    private List<LliguesVirtuals> lliguesVirtualsUser;
    private LliguesVirtualsAdapter adapter;
    private Context context;
    private String username;
    private UtilsProject utilsProject;
    private FirebaseOperationsMenuPrincipal firebaseOperationsMenuPrincipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        backButton.setOnClickListener(view -> alertDialogTancarApp.show());

        accountButton.setOnClickListener(view -> {
            Map<String, String> mapGoTo = new HashMap<>();
            mapGoTo.put("mail", emailUser);
            mapGoTo.put("username", username);
            utilsProject.goToAnotherActivity(PerfilPage.class, mapGoTo);
        });

        crearLligaVirtualButton.setOnClickListener(view -> createAlertDialogCrearLligaVirtual());

        unirLligaVirtualButton.setOnClickListener(view -> createAlertDialogUnirseLligaVirtual());

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Map<String,String> mapToGo = new HashMap<>();
                mapToGo.put("nomLliga", lliguesVirtualsUser.get(position).getNomLligaVirtual());
                mapToGo.put("competicio", lliguesVirtualsUser.get(position).getCompeticio());
                mapToGo.put("grup", lliguesVirtualsUser.get(position).getGrup());
                mapToGo.put("participants", lliguesVirtualsUser.get(position).getParticipants());
                utilsProject.goToAnotherActivity(MenuLligaVirtual.class, mapToGo);
            }

            @Override
            public void onLongItemClick() {
                // NON implemented
            }
        }));
    }

    @Override
    public void onBackPressed(){
        alertDialogTancarApp.show();
    }

    private void setAdapterLliguesVirtuals() {
        adapter = new LliguesVirtualsAdapter(lliguesVirtualsUser);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapterLliguesVirtuals(List<LliguesVirtuals> lliguesVirtualsList){
        adapter = new LliguesVirtualsAdapter(lliguesVirtualsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getLliguesVirtualsUsuari() {
        Task<DocumentSnapshot> task = firebaseOperationsMenuPrincipal.getLliguesUsuari();
        Tasks.whenAllComplete(task).addOnCompleteListener(task1 -> {
            List<String> listLligues = firebaseOperationsMenuPrincipal.getListLliguesUsuari();
            List<Task<DocumentSnapshot>> listTasks = new ArrayList<>();
            for ( String lligaNom : listLligues ) {
                listTasks.add(firebaseOperationsMenuPrincipal.getInfoLligaVirtual(lligaNom));
            }
            Tasks.whenAllComplete(listTasks).addOnCompleteListener(task2 -> {
                setAdapterLliguesVirtuals(firebaseOperationsMenuPrincipal.getLliguesVirtualsUsuariList());
                lliguesVirtualsUser = firebaseOperationsMenuPrincipal.getLliguesVirtualsUsuariList();
            });
        });
    }

    private void createAlertDialogUnirseLligaVirtual(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertDialogView = inflater.inflate(R.layout.linear_layout_unir_lliga_virtual, null);

        EditText password = alertDialogView.findViewById(R.id.etPassword_UnirLligaVirtual);
        EditText nomLligaVirtual = alertDialogView.findViewById(R.id.etUsername_UnirLligaVirtual);

        Button unirLligaVirtualOK = alertDialogView.findViewById(R.id.bUnirLligaOK);
        unirLligaVirtualOK.setOnClickListener(view -> {
            Task<DocumentSnapshot> task = firebaseOperationsMenuPrincipal.addLligaVirtual(nomLligaVirtual.getText().toString(), password.getText().toString());
            Tasks.whenAllComplete(task).addOnCompleteListener(task1 -> setAdapterLliguesVirtuals(firebaseOperationsMenuPrincipal.getLliguesVirtualsUsuariList()));
            alertDialogUnirLliga.dismiss();
        });

        builder.setView(alertDialogView);
        builder.setTitle("Unir-se Lliga Virtual");
        alertDialogUnirLliga = builder.create();
        alertDialogUnirLliga.show();
    }

    private void createAlertDialogCrearLligaVirtual() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                // NON IMPLEMENTED
            }
        });
        Button crearLligaVirtualOK = alertDialogView.findViewById(R.id.bCrearLligaOK);
        crearLligaVirtualOK.setOnClickListener(view -> {
            Map<String,Object> map = new HashMap<>();
            map.put("nomLligaVirtual", nomLligaVirtual.getText().toString());
            map.put("password", password.getText().toString());
            map.put("competicio", spinnerCompeticio.getSelectedItem().toString());
            map.put("grup", spinnerGrups.getSelectedItem().toString());
            map.put("maxParticipants", spinnerMaxParticipants.getSelectedItem().toString());
            List<String> listUsuari = Collections.singletonList(emailUser);
            map.put("usuaris", listUsuari);
            List<Task<Void>> tasks = firebaseOperationsMenuPrincipal.putNewUserToLligaVirtual(nomLligaVirtual.getText().toString(), true, map);
            Tasks.whenAllComplete(tasks).addOnCompleteListener(task1 -> setAdapterLliguesVirtuals(firebaseOperationsMenuPrincipal.getLliguesVirtualsUsuariList()));
            alertDialogCrearLliga.dismiss();
        });
        builder.setView(alertDialogView);
        builder.setTitle("Crear Lliga Virtual");
        alertDialogCrearLliga = builder.create();
        alertDialogCrearLliga.show();
    }

    private void initializeVariables() {
        backButton = findViewById(R.id.backButtonMenuPrincipal);
        accountButton = findViewById(R.id.accountButton);
        crearLligaVirtualButton = findViewById(R.id.bCrearLligaVirtual);
        unirLligaVirtualButton = findViewById(R.id.bUnirLligaVirtual);
        emailUser = getIntent().getExtras().getString("email");
        username = getIntent().getExtras().getString("username");

        recyclerView = findViewById(R.id.recyclerView_llistaLligues);
        lliguesVirtualsUser = new ArrayList<>();
        context = this;
        utilsProject = new UtilsProject(context);
        firebaseOperationsMenuPrincipal = new FirebaseOperationsMenuPrincipal(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Vols tancar l'aplicació?")
                .setPositiveButton("Si", (dialog, id) -> {
                    alertDialogTancarApp.dismiss();
                    finishAffinity();
                })
                .setNegativeButton("No", (dialog, id) -> alertDialogTancarApp.dismiss());
        // Create the AlertDialog object and return it
        alertDialogTancarApp = builder.create();


    }
}