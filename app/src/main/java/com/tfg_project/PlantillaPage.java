package com.tfg_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class PlantillaPage extends AppCompatActivity {

    Spinner spinnerAlineacions;
    LinearLayout llAtacantes, llMediocampo, llDefensa, llPortero;
    LinearLayout linearLayoutAlineacio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_page);

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
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView) linearLayout.getChildAt(0);
                textView.setText("NO");

            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView) linearLayout.getChildAt(0);
                textView.setText("YES");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}