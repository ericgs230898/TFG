package com.tfg_project.activity;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tfg_project.R;
import com.tfg_project.clases.Jugador;
import com.tfg_project.clases.JugadorPosicio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlantillaPage extends AppCompatActivity {

    private Spinner spinnerAlineacions;
    private LinearLayout linearLayoutAlineacio;

    private final List<String> jugadors = new ArrayList<>();
    private final List<Jugador> jugadorsEquip = new ArrayList<>();
    private final List<String> equips = new ArrayList<>();

    private ArrayAdapter<String> adp;
    private ArrayAdapter<String> adapterEquips;
    private ArrayAdapter<String> adapterJugadors;
    private String nomLligaVirtual;
    private String mailUser;

    private Spinner jugadorsPossibles;

    private AlertDialog dialog;
    private Spinner spinnerJornadesPossibles;
    private List<JornadaData> jornadesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_page);

        jornadesData = new ArrayList<>();
        mailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> listEquips = new ArrayList<>();

        ImageButton backButton = findViewById(R.id.ibBackButtonPlantilla);
        backButton.setOnClickListener(view -> finish());


        String competicio = this.getIntent().getStringExtra("competicio");
        String grup = this.getIntent().getStringExtra("grup");
        nomLligaVirtual = this.getIntent().getStringExtra("nomLligaVirtual");
        competicio = competicio.toLowerCase().replace(' ', '-');
        grup = "grup" + grup;
        final Context context = this;
        spinnerJornadesPossibles = findViewById(R.id.spinnerJornadesPlantilla);
        FirebaseFirestore.getInstance().collection("PartitsJugats").document(competicio)
                .collection("Grups").document(grup).collection("Jornades")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<String> jornades = new ArrayList<>();
                final List<JornadaData> jornadaDataAux = new ArrayList<>();
                for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments() ) {
                    String jornada = documentSnapshot.getId();
                    jornada = jornada.substring(7);
                    jornadaDataAux.add(new JornadaData("Jornada " + jornada,
                            (String) documentSnapshot.get("dataInici"),
                            (String) documentSnapshot.get("dataFi")));
                }
                for ( int i=1; i<=jornadaDataAux.size(); i++ ){
                    String jorn = "Jornada " + String.valueOf(i);
                    jornades.add(jorn);
                    for ( JornadaData jornadaDataIt: jornadaDataAux){
                        if ( jornadaDataIt.getJornada().equals(jorn)) {
                            jornadesData.add(jornadaDataIt);
                            break;
                        }
                    }
                }

                final ArrayAdapter<String> adapterJornades = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, jornades);
                spinnerJornadesPossibles.setAdapter(adapterJornades);
                int index = getJornadaMesPropera();
                spinnerJornadesPossibles.setSelection(index);
            }
        });
        spinnerJornadesPossibles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String jornAux = (String) spinnerJornadesPossibles.getSelectedItem();
                FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(nomLligaVirtual)
                        .collection("Jornada").document(jornAux)
                        .collection(mailUser).document("Plantilla").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String alineacio = "";
                        List<String> jugadorsDc = new ArrayList<>();
                        List<String> jugadorsDfc = new ArrayList<>();
                        List<String> jugadorsMc = new ArrayList<>();
                        String porter = "";
                        Map<String, Object> map = task.getResult().getData();
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
                                    for (int i = 0; jugadors.size() < 11; i++) {
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
                            inflateLayout(getLayoutValue("4-3-3"));
                            updatePlantilla(new ArrayList<>());
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        db.collection("Equip").document(competicio).collection("Grups")
                .document(grup).collection("Equips").get().addOnCompleteListener(task -> {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments() ) {
                        equips.add(documentSnapshot.getId());
                        ArrayList<String> jugadorsFirestore = (ArrayList<String>) documentSnapshot.get("jugadors");
                        for ( String nomJugador : jugadorsFirestore ) {
                            jugadorsEquip.add(new Jugador(nomJugador, documentSnapshot.getId()));
                        }
                        jugadors.addAll(jugadorsFirestore);
                    }
                });

        spinnerAlineacions = findViewById(R.id.spinnerAlineacions);
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
                    System.out.println(nomJ.getNomJugador());
                    set.add(nomJ.getNomJugador());
                }
                if (set.size() != jugadorGuardar.size()) makeToast("Hi ha algun jugador duplicat!");
                else {
                    makeToast("S'ha guardat la teva plantilla correctament!");
                    String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String jornada = (String) spinnerJornadesPossibles.getSelectedItem();
                    Map<String, Object> mapJugadors = new HashMap<>();
                    for (JugadorPosicio jugadorPosicio : jugadorGuardar) {
                        mapJugadors.put(jugadorPosicio.getNomJugador(), jugadorPosicio.getPosicio());
                    }
                    mapJugadors.put("alineacio", (String) spinnerAlineacions.getSelectedItem());
                    mapJugadors.put("puntuat", false);
                    mapJugadors.put("puntuat2", false);
                    Map<String, Object> mapAux = new HashMap<>();
                    mapAux.put("jornada", jornada);
                    FirebaseFirestore.getInstance().collection("LliguesVirtuals")
                            .document(nomLligaVirtual).collection("Jornada")
                            .document(jornada).set(mapAux);
                    FirebaseFirestore.getInstance().collection("LliguesVirtuals")
                            .document(nomLligaVirtual).collection("Jornada")
                            .document(jornada).collection(username).document("Plantilla").set(mapJugadors).addOnCompleteListener(task -> {
                        System.out.println("Task completed");
                    });
                }
            } else {
                makeToast("Aquesta jornada està en joc o ja ha estat jugada!");
            }
        });

        spinnerAlineacions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinnerAlineacions.getSelectedItem().toString();
                Log.i("INFO", text);
                if ( text.equals("4-3-3")){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_433);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("4-4-2") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_442);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("4-5-1") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_451);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("3-4-3") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_343);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("3-5-2") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_352);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("5-4-1") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_541);
                    updatePlantilla(jugadorsSelect);
                } else if ( text.equals("5-3-2") ){
                    List<String> jugadorsSelect = getJugadorsFromLinearLayout();
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_532);
                    updatePlantilla(jugadorsSelect);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getLayoutValue(String alineacio) {
        if (alineacio.equals("4-3-3")){
            return R.layout.linear_layout_433;
        } else if ( alineacio.equals("4-4-2")){
            return R.layout.linear_layout_442;
        } else if ( alineacio.equals("4-5-1")){
            return R.layout.linear_layout_451;
        } else if ( alineacio.equals("3-5-2")){
            return R.layout.linear_layout_352;
        } else if ( alineacio.equals("3-4-3")){
            return R.layout.linear_layout_343;
        } else if ( alineacio.equals("5-3-2")){
            return R.layout.linear_layout_532;
        } else if ( alineacio.equals("5-4-1")){
            return R.layout.linear_layout_541;
        } else{
            return -1;
        }
    }

    private static int getIndexAlineacio(String alineacio) {
        if (alineacio.equals("4-3-3")){
            return 0;
        } else if ( alineacio.equals("4-4-2")){
            return 1;
        } else if ( alineacio.equals("4-5-1")){
            return 2;
        } else if ( alineacio.equals("3-5-2")){
            return 3;
        } else if ( alineacio.equals("3-4-3")){
            return 4;
        } else if ( alineacio.equals("5-3-2")){
            return 5;
        } else if ( alineacio.equals("5-4-1")){
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

            }
            long diffInMillies = Math.abs(dateJornada.getTime()-dateNow.getTime());
            if ( min == -1 ) {
                min = diffInMillies;
            }
            else {
                if ( diffInMillies < min ) {
                    min = diffInMillies;
                    minJornada = i;
                }
            }
        }
        return minJornada;
    }

    private boolean dataAnteriorADataActual(String jornadaSeleccionada) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for ( JornadaData jornadaData : jornadesData ) {
            if ( jornadaData.getJornada().equals(jornadaSeleccionada)){
                String dataMin = jornadaData.getDataMin();
                Date dateMin = null;
                try {
                    dateMin = simpleDateFormat.parse(dataMin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if ( dateMin != null ){
                    Date dateNow = new Date();
                    return dateMin.after(dateNow);
                }
            }
        }
        return false;
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void updatePlantilla(List<String> jugadorsSelect) {
        if ( linearLayoutAlineacio.getChildCount() > 0 ) {
            // dc / mc / df / por           // jugadors en la posi
            LinearLayout llDelanteros = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(0));
            LinearLayout llMediocampo = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(2));
            LinearLayout llDefensa = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(4));
            LinearLayout llPortero = ((LinearLayout)((LinearLayout) linearLayoutAlineacio.getChildAt(0)).getChildAt(6));
            if ( llDelanteros != null && llMediocampo!=null && llDefensa!=null && llPortero!=null ) {
                if ( jugadorsSelect.size() > 0 ) {
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

        adapterEquips = new ArrayAdapter<>(PlantillaPage.this, android.R.layout.simple_spinner_item, equips);
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

            }
        });
        builder.setView(alertDialogView);

        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton("SI", (dialogInterface, i) -> {
            TextView textView = (TextView) linearLayout.getChildAt(0);
            String text = (String) jugadorsPossibles.getSelectedItem();
            String tvJugadorText = textView.getText().toString();
            textView.setText(text);
        });
        dialog = builder.create();
        dialog.show();
    }
}