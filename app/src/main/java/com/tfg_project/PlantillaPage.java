package com.tfg_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void setAlineacion(int atacantes, int mediocampo, int defensa, int portero){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        while ( llAtacantes.getChildCount() < atacantes ) {
            TextView tvJugador = new TextView(this);
            tvJugador.setText("Atacante1");
            llAtacantes.addView(tvJugador, params);
        }
        while ( llAtacantes.getChildCount() > atacantes ) {
            llAtacantes.removeViewAt(llAtacantes.getChildCount()-1);
        }
        while ( llMediocampo.getChildCount() < mediocampo ) {
            TextView tvJugador = new TextView(this);
            tvJugador.setText("Medio1");
            llMediocampo.addView(tvJugador, params);
        }
        while ( llMediocampo.getChildCount() > mediocampo ) {
            llMediocampo.removeViewAt(llMediocampo.getChildCount()-1);
        }
        while ( llDefensa.getChildCount() < defensa ) {
            TextView tvJugador = new TextView(this);
            tvJugador.setText("Defensa1");
            llDefensa.addView(tvJugador, params);
        }
        while ( llDefensa.getChildCount() > defensa ) {
            llDefensa.removeViewAt(llDefensa.getChildCount()-1);
        }
        while ( llPortero.getChildCount() < portero ) {
            TextView tvJugador = new TextView(this);
            tvJugador.setText("Portero1");
            llPortero.addView(tvJugador, params);
        }
    }
}