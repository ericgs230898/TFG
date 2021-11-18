package com.tfg_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlantillaPage extends AppCompatActivity {

    String categoriaGrup = "tercera-catalana12";

    Spinner spinnerAlineacions;
    LinearLayout llAtacantes, llMediocampo, llDefensa, llPortero;
    LinearLayout linearLayoutAlineacio;

    Map<String,Object> map;
    Map<String,Object> map2;

    List<String> jugadors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> listEquips = new ArrayList<>();

        db.collection("Equips").document("tercera-catalana12").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                map = task.getResult().getData();
                for ( Map.Entry<String, Object> entry : map.entrySet()){
                    System.out.println(entry.getKey());
                    listEquips.add(entry.getKey());
                }
                goToAddJugadors(listEquips);
            }
        });

        llAtacantes = findViewById(R.id.ll_atacantes);
        llMediocampo = findViewById(R.id.ll_medios);
        llDefensa = findViewById(R.id.ll_defensa);
        llPortero = findViewById(R.id.ll_portero);


        spinnerAlineacions = findViewById(R.id.spinnerAlineacions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alineacions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlineacions.setAdapter(adapter);

        linearLayoutAlineacio = findViewById(R.id.ll_alineacio);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.linear_layout_433, null);
        linearLayoutAlineacio.addView(myView);


        spinnerAlineacions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinnerAlineacions.getSelectedItem().toString();
                Log.i("INFO", text);
                if ( text.equals("4-3-3")){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_433);
                } else if ( text.equals("4-4-2") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_442);
                } else if ( text.equals("4-5-1") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_451);
                } else if ( text.equals("3-4-3") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_343);
                } else if ( text.equals("3-5-2") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_352);
                } else if ( text.equals("5-4-1") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_541);
                } else if ( text.equals("5-3-2") ){
                    linearLayoutAlineacio.removeAllViews();
                    inflateLayout(R.layout.linear_layout_532);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void goToAddJugadors(List<String> listEquips) {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> setList = sharedPreferences.getStringSet("Jugadors"+"tercera-catalana12", null);
        if ( setList == null ) {
            Log.d("DB", "no data stored!");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            System.out.println("SIZE ------> " + listEquips.size());
            for (String nomEquip : listEquips) {
                db.collection("Jugadors").document("tercera-catalana12").collection(nomEquip).document("Jugadors").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        map2 = documentSnapshot.getData();
                        for (Map.Entry<String, Object> entry : map2.entrySet()) {
                            List<String> jugDb = (List<String>) entry.getValue();
                            jugadors.addAll(jugDb);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("Jugadors"+"tercera-catalana12", new HashSet<>(jugadors));
                        editor.commit();
                        Log.d("DB", "data added");

                    }
                });
            }
        } else {
            Log.d("DB", "Data recovered");
            jugadors.addAll(setList);
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
        builder.setMessage("Message").setTitle("title");
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(PlantillaPage.this, android.R.layout.simple_spinner_item, jugadors);
        final Spinner sp1 = new Spinner(PlantillaPage.this);
        sp1.setAdapter(adp);
        builder.setView(sp1);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView) linearLayout.getChildAt(0);
                String text = (String) sp1.getSelectedItem();
                textView.setText(text);
                System.out.println("SIZE JUGADORS -> " + jugadors.size());

            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView) linearLayout.getChildAt(0);
                String text = (String) sp1.getSelectedItem();
                textView.setText(text);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}