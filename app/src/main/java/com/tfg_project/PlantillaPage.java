package com.tfg_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PlantillaPage extends AppCompatActivity {

    Spinner spinnerAlineacions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_page);

        spinnerAlineacions = findViewById(R.id.spinnerAlineacions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alineacions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlineacions.setAdapter(adapter);

        int width= Resources.getSystem().getDisplayMetrics().widthPixels;
        int height=Resources.getSystem().getDisplayMetrics().heightPixels;

    //100dp
    }
}