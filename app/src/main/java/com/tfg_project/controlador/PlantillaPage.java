package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tfg_project.R;
import com.tfg_project.model.beans.JornadaData;
import com.tfg_project.model.beans.Jugador;
import com.tfg_project.model.beans.JugadorPosicio;
import com.tfg_project.model.firestore.FirebaseOperationsPlantillaPage;
import com.tfg_project.model.utils.UtilsProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlantillaPage extends AppCompatActivity {
    private static final String ALINEACIO_433 = "4-3-3";
    private static final String ALINEACIO_442 = "4-4-2";
    private static final String ALINEACIO_451 = "4-5-1";
    private static final String ALINEACIO_343 = "3-4-3";
    private static final String ALINEACIO_352 = "3-5-2";
    private static final String ALINEACIO_541 = "5-4-1";
    private static final String ALINEACIO_532 = "5-3-2";

    private Spinner spinnerAlineacions;
    private LinearLayout linearLayoutAlineacio;

    private List<Jugador> jugadorsEquip;
    private List<String> equips;

    private ArrayAdapter<String> adapterJugadors;
    private String nomLligaVirtual;
    private Spinner jugadorsPossibles;
    private AlertDialog dialog;
    private Spinner spinnerJornadesPossibles;
    private List<JornadaData> jornadesData;
    private UtilsProject utilsProject;
    private ImageButton backButton;
    private Context context;
    private TextView tvDataMinJornada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();

        backButton.setOnClickListener(view -> finish());


        String competicio = this.getIntent().getStringExtra("competicio");
        String grup = this.getIntent().getStringExtra("grup");
        nomLligaVirtual = this.getIntent().getStringExtra("nomLligaVirtual");
        competicio = competicio.toLowerCase().replace(' ', '-');
        grup = "grup" + grup;
        FirebaseOperationsPlantillaPage firebaseOperationsPlantillaPage = new FirebaseOperationsPlantillaPage(context);
        Task<QuerySnapshot> task1 = firebaseOperationsPlantillaPage.getJornadesPossibles(competicio, grup);
        Tasks.whenAllComplete(task1).addOnCompleteListener(task -> {
            jornadesData = firebaseOperationsPlantillaPage.getJornadesData();
            final ArrayAdapter<String> adapterJornades = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, firebaseOperationsPlantillaPage.getJornades());
            spinnerJornadesPossibles.setAdapter(adapterJornades);
            int index = getJornadaMesPropera();
            tvDataMinJornada.setText("INICI: " + jornadesData.get(index).getDataMin() + " - FI: " + jornadesData.get(index).getDataMax());
            spinnerJornadesPossibles.setSelection(index);
        });
        spinnerJornadesPossibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornAux = (String) spinnerJornadesPossibles.getSelectedItem();
                tvDataMinJornada.setText("INICI: " + jornadesData.get(i).getDataMin() + " - FI: " + jornadesData.get(i).getDataMax());
                Task<DocumentSnapshot> task2 = firebaseOperationsPlantillaPage.getJugadorsDisponibles(nomLligaVirtual, jornAux);
                Tasks.whenAllComplete(task2).addOnCompleteListener(task -> {
                    Map<String, Object> map = firebaseOperationsPlantillaPage.getMap();
                    String alineacio = "";
                    List<String> jugadorsDc = new ArrayList<>();
                    List<String> jugadorsDfc = new ArrayList<>();
                    List<String> jugadorsMc = new ArrayList<>();
                    String porter = "";
                    if ( map != null ) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getKey().equals("alineacio")) {
                                alineacio = (String) entry.getValue();
                            } else {
                                if ("DC".equals(entry.getValue())) {
                                    jugadorsDc.add(entry.getKey());
                                } else if ("MC".equals(entry.getValue())) {
                                    jugadorsMc.add(entry.getKey());
                                } else if ("DFC".equals(entry.getValue())) {
                                    jugadorsDfc.add(entry.getKey());
                                } else if ("POR".equals(entry.getValue())) {
                                    porter = entry.getKey();
                                }
                            }
                        }
                        List<String> jugadorsBD = new ArrayList<>();
                        jugadorsBD.addAll(jugadorsDc);
                        jugadorsBD.addAll(jugadorsDfc);
                        jugadorsBD.addAll(jugadorsMc);
                        if (!"".equals(porter)) jugadorsBD.add(porter);
                        if (!"".equals(alineacio)) {
                            int indexAlineacio = getIndexAlineacio(alineacio);
                            spinnerAlineacions.setSelection(indexAlineacio);
                            if (jugadorsBD.size() < 11) {
                                int iAux=0;
                                while (iAux < 11){
                                    iAux++;
                                    jugadorsBD.add("");
                                }
                            }
                            linearLayoutAlineacio.removeAllViews();
                            inflateLayout(getLayoutValue(alineacio));
                            updatePlantilla(jugadorsBD);
                        }
                    } else {
                        linearLayoutAlineacio.removeAllViews();
                        spinnerAlineacions.setSelection(0);
                        inflateLayout(getLayoutValue(ALINEACIO_433));
                        updatePlantilla(new ArrayList<>());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // non implemented
            }
        });
        Task<QuerySnapshot> task3 = firebaseOperationsPlantillaPage.getJugadorsCompeticioGrup(competicio, grup);
        Tasks.whenAllComplete(task3).addOnCompleteListener(task -> {
            equips = firebaseOperationsPlantillaPage.getEquips();
            jugadorsEquip = firebaseOperationsPlantillaPage.getJugadorsEquip();
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alineacions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlineacions.setAdapter(adapter);

        linearLayoutAlineacio = findViewById(R.id.ll_alineacio);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.linear_layout_433, null);
        linearLayoutAlineacio.addView(myView);

        Button bGuardarPlantilla = findViewById(R.id.bGuardarPlantilla);
        bGuardarPlantilla.setOnClickListener(view -> {
            String jornadaSeleccionada = (String) spinnerJornadesPossibles.getSelectedItem();
            if ( dataAnteriorADataActual(jornadaSeleccionada) ) {
                List<JugadorPosicio> jugadorGuardar = new ArrayList<>();
                LinearLayout llDelanteros = ((LinearLayout) ((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(0));
                LinearLayout llMediocampo = ((LinearLayout) ((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(2));
                LinearLayout llDefensa = ((LinearLayout) ((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(4));
                LinearLayout llPortero = ((LinearLayout) ((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(6));

                for (int i = 0; i < llDelanteros.getChildCount(); i++) {
                    String nomJugador = ((TextView) ((LinearLayout) llDelanteros.getChildAt(i)).getChildAt(0)).getText().toString();
                    if (!nomJugador.equals(""))
                        jugadorGuardar.add(new JugadorPosicio(nomJugador, "DC"));
                }
                for (int i = 0; i < llMediocampo.getChildCount(); i++) {
                    String nomJugador = ((TextView) ((LinearLayout) llMediocampo.getChildAt(i)).getChildAt(0)).getText().toString();
                    if (!nomJugador.equals(""))
                        jugadorGuardar.add(new JugadorPosicio(nomJugador, "MC"));
                }
                for (int i = 0; i < llDefensa.getChildCount(); i++) {
                    String nomJugador = ((TextView) ((LinearLayout) llDefensa.getChildAt(i)).getChildAt(0)).getText().toString();
                    if (!nomJugador.equals(""))
                        jugadorGuardar.add(new JugadorPosicio(nomJugador, "DFC"));
                }
                for (int i = 0; i < llPortero.getChildCount(); i++) {
                    String nomJugador = ((TextView) ((LinearLayout) llPortero.getChildAt(i)).getChildAt(0)).getText().toString();
                    if (!nomJugador.equals(""))
                        jugadorGuardar.add(new JugadorPosicio(nomJugador, "POR"));
                }
                Set<String> set = new HashSet<>();
                for (JugadorPosicio nomJ : jugadorGuardar) {
                    set.add(nomJ.getNomJugador());
                }
                if (set.size() != jugadorGuardar.size()) utilsProject.makeToast("Hi ha algun jugador duplicat!" );
                else {
                    utilsProject.makeToast("S'ha guardat la teva plantilla correctament!");
                    String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                    String jornada = (String) spinnerJornadesPossibles.getSelectedItem();
                    Map<String, Object> mapJugadors = new HashMap<>();
                    for (JugadorPosicio jugadorPosicio : jugadorGuardar) {
                        mapJugadors.put(jugadorPosicio.getNomJugador(), jugadorPosicio.getPosicio());
                    }
                    mapJugadors.put("alineacio", spinnerAlineacions.getSelectedItem());
                    mapJugadors.put("puntuat", false);
                    mapJugadors.put("puntuat2", false);
                    Map<String, Object> mapAux = new HashMap<>();
                    mapAux.put("jornada", jornada);
                    firebaseOperationsPlantillaPage.guardarPlantilla(nomLligaVirtual, jornada, username, mapAux, mapJugadors);
                }
            } else {
                utilsProject.makeToast("Aquesta jornada està en joc o ja ha estat jugada!");
            }
        });

        spinnerAlineacions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinnerAlineacions.getSelectedItem().toString();
                Log.i("INFO", text);
                switch (text) {
                    case ALINEACIO_433: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_433);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_442: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_442);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_451: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_451);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_343: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_343);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_352: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_352);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_541: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_541);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                    case ALINEACIO_532: {
                        List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                        linearLayoutAlineacio.removeAllViews();
                        inflateLayout(R.layout.linear_layout_532);
                        updatePlantilla(jugadorsSelect);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // non implemented
            }
        });
    }

    private void initializeVariables() {
        backButton = findViewById(R.id.ibBackButtonPlantilla);
        jornadesData = new ArrayList<>();
        context = this;
        utilsProject = new UtilsProject(context);
        spinnerJornadesPossibles = findViewById(R.id.spinnerJornadesPlantilla);
        spinnerJornadesPossibles.setBackgroundResource(R.drawable.customborder8);
        spinnerAlineacions = findViewById(R.id.spinnerAlineacions);
        spinnerAlineacions.setBackgroundResource(R.drawable.customborder8);
        tvDataMinJornada = findViewById(R.id.tvDataMinJornada);
    }

    private int getLayoutValue(String alineacio) {
        switch (alineacio) {
            case ALINEACIO_433:
                return R.layout.linear_layout_433;
            case ALINEACIO_442:
                return R.layout.linear_layout_442;
            case ALINEACIO_451:
                return R.layout.linear_layout_451;
            case ALINEACIO_352:
                return R.layout.linear_layout_352;
            case ALINEACIO_343:
                return R.layout.linear_layout_343;
            case ALINEACIO_532:
                return R.layout.linear_layout_532;
            case ALINEACIO_541:
                return R.layout.linear_layout_541;
            default:
                return -1;
        }
    }

    private static int getIndexAlineacio(String alineacio) {
        switch (alineacio) {
            case ALINEACIO_433:
                return 0;
            case ALINEACIO_442:
                return 1;
            case ALINEACIO_451:
                return 2;
            case ALINEACIO_352:
                return 3;
            case ALINEACIO_343:
                return 4;
            case ALINEACIO_532:
                return 5;
            case ALINEACIO_541:
                return 6;
        }
        return 1;
    }

    private List<String> getJugadorsFromLinearLayout(){
        List<String> listJugadors = new ArrayList<>();
        if ( linearLayoutAlineacio.getChildCount() > 0 ) {
            // dc / mc / df / por           // jugadors en la posi
            LinearLayout llDelanteros = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(0));
            LinearLayout llMediocampo = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(2));
            LinearLayout llDefensa = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(4));
            LinearLayout llPortero = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(6));
            if ( llDelanteros != null && llMediocampo!=null && llDefensa!=null && llPortero!=null ) {
                for (int i = 0; i < llDelanteros.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llDelanteros.getChildAt(i)).getChildAt(0);
                    listJugadors.add(tv.getText().toString());
                }

                for (int i = 0; i < llMediocampo.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llMediocampo.getChildAt(i)).getChildAt(0);
                    listJugadors.add(tv.getText().toString());
                }

                for (int i = 0; i < llDefensa.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llDefensa.getChildAt(i)).getChildAt(0);
                    listJugadors.add(tv.getText().toString());
                }
                TextView tv = (TextView) ((LinearLayout) llPortero.getChildAt(0)).getChildAt(0);
                listJugadors.add(tv.getText().toString());
            }
        }
        return listJugadors;
    }

    private int getJornadaMesPropera() {
        long min = -1;
        int minJornada = 0;
        for ( int i=0; i<jornadesData.size()-1; i++ ) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateNow = new Date();
            Date dateJornada = null;
            try {
                dateJornada = sdf.parse(jornadesData.get(i).getDataMin());
            } catch (ParseException e ){
                // non implemented
            }
            if ( dateJornada != null ) {
                long diffInMillies = Math.abs(dateJornada.getTime() - dateNow.getTime());
                if (min == -1) {
                    min = diffInMillies;
                } else {
                    if (diffInMillies < min) {
                        min = diffInMillies;
                        minJornada = i;
                    }
                }
            }
        }
        return minJornada;
    }

    private boolean dataAnteriorADataActual(String jornadaSeleccionada) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (JornadaData jornadaData : jornadesData) {
            if (jornadaData.getJornada().equals(jornadaSeleccionada)) {
                String dataMin = jornadaData.getDataMin();
                Date dateMin = null;
                try {
                    dateMin = simpleDateFormat.parse(dataMin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dateMin != null) {
                    Date dateNow = new Date();
                    return dateMin.after(dateNow);
                }
            }
        }
        return false;
    }

    private void updatePlantilla(List<String> jugadorsSelect) {
        if ( linearLayoutAlineacio.getChildCount() > 0 ) {
            // dc / mc / df / por           // jugadors en la posi
            LinearLayout llDelanteros = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(0));
            LinearLayout llMediocampo = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(2));
            LinearLayout llDefensa = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(4));
            LinearLayout llPortero = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(6));
            if ( llDelanteros != null && llMediocampo!=null && llDefensa!=null && llPortero!=null && !jugadorsSelect.isEmpty() ) {
                for (int i = 0; i < llDelanteros.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llDelanteros.getChildAt(i)).getChildAt(0);
                    if ( i<jugadorsSelect.size())tv.setText(jugadorsSelect.get(i));
                }

                for (int i = 0; i < llMediocampo.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llMediocampo.getChildAt(i)).getChildAt(0);
                    int indexAux = i + llDelanteros.getChildCount();
                    if ( indexAux<jugadorsSelect.size()) {
                        tv.setText(jugadorsSelect.get(indexAux));
                    }
                }

                for (int i = 0; i < llDefensa.getChildCount(); i++) {
                    TextView tv = (TextView) ((LinearLayout) llDefensa.getChildAt(i)).getChildAt(0);
                    int indexAux = i + i + llDelanteros.getChildCount() + llMediocampo.getChildCount();
                    if ( indexAux<jugadorsSelect.size()) {
                        tv.setText(jugadorsSelect.get(indexAux));
                    }
                }
                TextView tv = (TextView) ((LinearLayout) llPortero.getChildAt(0)).getChildAt(0);
                if ( (llDelanteros.getChildCount() + llMediocampo.getChildCount() + llDefensa.getChildCount()) < jugadorsSelect.size()) {
                    tv.setText(jugadorsSelect.get(llDelanteros.getChildCount() + llMediocampo.getChildCount() + llDefensa.getChildCount()));
                }
            }
        }
    }

    private void inflateLayout (int layout ) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(layout, null);
        linearLayoutAlineacio.addView(myView);
    }

    public void createDialog (View view){
        LinearLayout linearLayout = (LinearLayout) view;
        Log.e("DIALOG", "LLEGODIALOG");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Escull un dels jugadors:").setTitle("Selecciona Jugador");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertDialogView = inflater.inflate(R.layout.linear_layout_alert_dialog_seleccionar_jugador, null);
        Spinner equipsPossibles = alertDialogView.findViewById(R.id.spinnerEquipsPossibles);
        jugadorsPossibles = alertDialogView.findViewById(R.id.spinnerJugadorsPossibles);

        ArrayAdapter<String> adapterEquips = new ArrayAdapter<>(PlantillaPage.this, android.R.layout.simple_spinner_item, equips);
        equipsPossibles.setAdapter(adapterEquips);

        equipsPossibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String equip = (String) equipsPossibles.getSelectedItem();
                ArrayList<String> jugadorsPossiblesList = new ArrayList<>();
                for ( Jugador jugador : jugadorsEquip ) {
                    if ( jugador.getIdEquip().equals(equip)) {
                        jugadorsPossiblesList.add(jugador.getNomJugador());
                    }
                }
                adapterJugadors = new ArrayAdapter<>(PlantillaPage.this, android.R.layout.simple_spinner_item, jugadorsPossiblesList);
                jugadorsPossibles.setAdapter(adapterJugadors);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // non implemented
            }
        });
        builder.setView(alertDialogView);

        builder.setNegativeButton("NO", (dialogInterface, i) -> dialog.dismiss());
        builder.setPositiveButton("SI", (dialogInterface, i) -> {
            TextView textView = (TextView) linearLayout.getChildAt(0);
            String text = (String) jugadorsPossibles.getSelectedItem();
            textView.setText(text);
        });
        dialog = builder.create();
        dialog.show();
    }
}