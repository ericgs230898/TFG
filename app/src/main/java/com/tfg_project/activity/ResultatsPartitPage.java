package com.tfg_project.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tfg_project.R;
import com.tfg_project.clases.JugadorResultat;
import com.tfg_project.clases.JugadorResultatAdapter;
import com.tfg_project.clases.PartitJugador;
import com.tfg_project.clases.RecyclerItemClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultatsPartitPage extends AppCompatActivity {
    private TextView titlePartit;
    private List<JugadorResultat> jugadorResultatListLocalTitulars;
    private List<JugadorResultat> jugadorResultatListLocalSuplents;
    private List<JugadorResultat> jugadorResultatListVisitantTitulars;
    private List<JugadorResultat> jugadorResultatListVisitantSuplents;
    private JugadorResultatAdapter adapterLocalTitulars;
    private JugadorResultatAdapter adapterLocalSuplents;
    private JugadorResultatAdapter adapterVisitantTitulars;
    private JugadorResultatAdapter adapterVisitantSuplents;
    private RecyclerView recyclerViewLocalTitulars;
    private RecyclerView recyclerViewLocalSuplents;
    private RecyclerView recyclerViewVisitantTitulars;
    private RecyclerView recyclerViewVisitantSuplents;

    private Context context;
    private List<PartitJugador> jugLocals;
    private List<PartitJugador> jugVisitants;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_partit_page);

        initializeVariables();
        setAdapterJugadorResultat();

        String competicio = this.getIntent().getStringExtra("competicio");
        String grup = this.getIntent().getStringExtra("grup");
        String jornada = this.getIntent().getStringExtra("jornada");
        String equipLocal = this.getIntent().getStringExtra("equipLocal");
        String equipVisitant = this.getIntent().getStringExtra("equipVisitant");
        String jugadorsLocal = this.getIntent().getStringExtra("loc");
        String jugadorsVisitant = this.getIntent().getStringExtra("vis");
        String golesLocal = this.getIntent().getStringExtra("golesLocal");
        String golesVisitant = this.getIntent().getStringExtra("golesVisitant");
        Type listType = new TypeToken<List<PartitJugador>>(){}.getType();
        jugLocals = new Gson().fromJson(jugadorsLocal, listType);
        jugVisitants = new Gson().fromJson(jugadorsVisitant, listType);
        titlePartit.setText(equipLocal+ ": " + golesLocal + "\n" + equipVisitant + ": " + golesVisitant);

        TextView tvLocal = findViewById(R.id.tvEquipLocalResultatPage);
        TextView tvVisitant = findViewById(R.id.tvEquipVisitantResultatPage);

        tvLocal.setText(equipLocal);
        tvVisitant.setText(equipVisitant);

       // tvEquipVisitant.setText(equipVisitant);
       // tvEquipLocal.setText(equipLocal);
        ArrayList<JugadorResultat> jugadorsLocalTitulars = new ArrayList<>();
        ArrayList<JugadorResultat> jugadorsLocalSuplents = new ArrayList<>();
        ArrayList<JugadorResultat> jugadorsVisitantTitulars = new ArrayList<>();
        ArrayList<JugadorResultat> jugadorsVisitantSuplents = new ArrayList<>();

        for ( PartitJugador pj : jugLocals ){
            int minutosJugados = 0;
            int minInici = pj.getMinutInici();
            int minFi = pj.getMinutFi();
            if ( minInici != -1 ) {
                if ( minFi != -1 ) minutosJugados = minFi - minInici;
                else minutosJugados = 90 - minInici;
            }
            JugadorResultat jugadorResultat = new JugadorResultat(pj.getNomJugador(), minutosJugados,
                    pj.isTarjetaAmarilla1(), pj.isTarjetaAmarilla2(), pj.isTarjetaRoja(),
                    pj.getGolesMarcados(), pj.getGolesMarcadosPenalti(),
                    pj.getGolesMarcadosPropiaPuerta(), pj.isPortero(), pj.getGolesEncajados());
            if ( pj.getMinutInici() == 0 ) {
                jugadorsLocalTitulars.add(jugadorResultat);
            } else {
                jugadorsLocalSuplents.add(jugadorResultat);
            }
        }
        for ( JugadorResultat jugadorResultat : jugadorsLocalTitulars ) {
            if ( jugadorResultat.isPortero() ) {
                jugadorResultatListLocalTitulars.add(jugadorResultat);
                adapterLocalTitulars.notifyItemInserted(jugadorResultatListLocalTitulars.size()-1);
                jugadorsLocalTitulars.remove(jugadorResultat);
                break;
            }
        }
        for ( JugadorResultat jugadorResultat : jugadorsLocalTitulars ) {
            jugadorResultatListLocalTitulars.add(jugadorResultat);
            adapterLocalTitulars.notifyItemInserted(jugadorResultatListLocalTitulars.size()-1);
        }
        for ( JugadorResultat jugadorResultat : jugadorsLocalSuplents ) {
            if ( jugadorResultat.isPortero() ) {
                jugadorResultatListLocalSuplents.add(jugadorResultat);
                adapterLocalSuplents.notifyItemInserted(jugadorResultatListLocalSuplents.size()-1);
                jugadorsLocalSuplents.remove(jugadorResultat);
                break;
            }
        }
        for ( JugadorResultat jugadorResultat : jugadorsLocalSuplents ) {
            jugadorResultatListLocalSuplents.add(jugadorResultat);
            adapterLocalSuplents.notifyItemInserted(jugadorResultatListLocalSuplents.size()-1);
        }


        recyclerViewLocalTitulars.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewLocalTitulars, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListLocalTitulars.get(position), true);
                System.out.println(jugadorResultatListLocalTitulars.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewLocalSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewLocalSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListLocalSuplents.get(position), true);
                System.out.println(jugadorResultatListLocalSuplents.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        for ( PartitJugador pj : jugVisitants ){
            int minutosJugados = 0;
            int minInici = pj.getMinutInici();
            int minFi = pj.getMinutFi();
            if ( minInici != -1 ) {
                if ( minFi != -1 ) minutosJugados = minFi - minInici;
                else minutosJugados = 90 - minInici;
            }
            JugadorResultat jugadorResultat = new JugadorResultat(pj.getNomJugador(), minutosJugados,
                    pj.isTarjetaAmarilla1(), pj.isTarjetaAmarilla2(), pj.isTarjetaRoja(),
                    pj.getGolesMarcados(), pj.getGolesMarcadosPenalti(),
                    pj.getGolesMarcadosPropiaPuerta(), pj.isPortero(), pj.getGolesEncajados());
            if ( pj.getMinutInici() == 0 ) {
                jugadorsVisitantTitulars.add(jugadorResultat);
            } else {
                jugadorsVisitantSuplents.add(jugadorResultat);
            }
        }

        for ( JugadorResultat jugadorResultat : jugadorsVisitantTitulars ) {
            if ( jugadorResultat.isPortero() ) {
                jugadorResultatListVisitantTitulars.add(jugadorResultat);
                adapterVisitantTitulars.notifyItemInserted(jugadorResultatListVisitantTitulars.size()-1);
                jugadorsVisitantTitulars.remove(jugadorResultat);
                break;
            }
        }
        for ( JugadorResultat jugadorResultat : jugadorsVisitantTitulars ) {
            jugadorResultatListVisitantTitulars.add(jugadorResultat);
            adapterVisitantTitulars.notifyItemInserted(jugadorResultatListVisitantTitulars.size()-1);
        }
        for ( JugadorResultat jugadorResultat : jugadorsVisitantSuplents ) {
            if ( jugadorResultat.isPortero() ) {
                jugadorResultatListVisitantSuplents.add(jugadorResultat);
                adapterVisitantSuplents.notifyItemInserted(jugadorResultatListVisitantSuplents.size()-1);
                jugadorsVisitantSuplents.remove(jugadorResultat);
                break;
            }
        }
        for ( JugadorResultat jugadorResultat : jugadorsVisitantSuplents ) {
            jugadorResultatListVisitantSuplents.add(jugadorResultat);
            adapterVisitantSuplents.notifyItemInserted(jugadorResultatListVisitantSuplents.size()-1);
        }


        recyclerViewVisitantTitulars.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewVisitantTitulars, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListVisitantTitulars.get(position), true);
                System.out.println(jugadorResultatListVisitantTitulars.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewVisitantSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewVisitantSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListVisitantSuplents.get(position), true);
                System.out.println(jugadorResultatListVisitantSuplents.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        System.out.println(competicio + " " + grup + " " + jornada + " " + equipLocal + " " + equipVisitant);
        System.out.println(jugadorsLocal);
        System.out.println(jugadorsVisitant);

    }

    private void showAlertDialog(JugadorResultat pj, boolean locals) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ESTADÍSTIQUES D'UN JUGADOR");
        final View customLayout = getLayoutInflater().inflate(R.layout.linear_layout_alert_dialog_resultat, null);
        TextView tvNom, tvMinuts, tvGols, tvGolsPenal, tvGolsPP, tvGrogues, tvVermelles, tvGolesEncajados;
        tvNom = customLayout.findViewById(R.id.tvJugadorsNom);
        tvMinuts = customLayout.findViewById(R.id.tvJugadorsMinuts);
        tvGols = customLayout.findViewById(R.id.tvJugadorsGols);
        tvGolsPenal = customLayout.findViewById(R.id.tvJugadorsGolsPenal);
        tvGolsPP = customLayout.findViewById(R.id.tvJugadorsGolsPropia);
        tvGrogues = customLayout.findViewById(R.id.tvJugadorGrogues);
        tvVermelles = customLayout.findViewById(R.id.tvJugadorsVermelles);
        tvGolesEncajados = customLayout.findViewById(R.id.tvJugadorsGolesEncajados);
        tvNom.setText(pj.getNom());
        tvMinuts.setText("Minuts jugats: " + String.valueOf(pj.getMinutsJugats()));
        tvGols.setText("Gols: " + String.valueOf(pj.getGolesMarcados()));
        tvGolsPenal.setText("Gols de penal: " + String.valueOf(pj.getGolesMarcadosPenalti()));
        tvGolsPP.setText("Gols en pròpia: " + String.valueOf(pj.getGolesMarcadosPropia()));
        if ( pj.isPortero() ) tvGolesEncajados.setText("Gols rebuts: " + pj.getGolesEncajados());
        int grogues = 0;
        if ( pj.isTarjetaAmarilla1()) grogues++;
        if ( pj.isTarjetaAmarilla2()) grogues++;
        tvGrogues.setText("Tarjetes grogues: " + String.valueOf(grogues));
        if ( pj.isTarjetaRoja1() ) tvVermelles.setText("Tarjetes vermelles: 1");
        else tvVermelles.setText("Tarjetes vermelles: 0");


        builder.setView(customLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void setAdapterJugadorResultat() {
        adapterLocalTitulars = new JugadorResultatAdapter(jugadorResultatListLocalTitulars);
        recyclerViewLocalTitulars.setAdapter(adapterLocalTitulars);
        recyclerViewLocalTitulars.setLayoutManager(new LinearLayoutManager(this));

        adapterVisitantTitulars = new JugadorResultatAdapter(jugadorResultatListVisitantTitulars);
        recyclerViewVisitantTitulars.setAdapter(adapterVisitantTitulars);
        recyclerViewVisitantTitulars.setLayoutManager(new LinearLayoutManager(this));

        adapterLocalSuplents = new JugadorResultatAdapter(jugadorResultatListLocalSuplents);
        recyclerViewLocalSuplents.setAdapter(adapterLocalSuplents);
        recyclerViewLocalSuplents.setLayoutManager(new LinearLayoutManager(this));

        adapterVisitantSuplents = new JugadorResultatAdapter(jugadorResultatListVisitantSuplents);
        recyclerViewVisitantSuplents.setAdapter(adapterVisitantSuplents);
        recyclerViewVisitantSuplents.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeVariables() {
        context = this;
        jugadorResultatListLocalTitulars = new ArrayList<>();
        jugadorResultatListVisitantTitulars = new ArrayList<>();
        jugadorResultatListLocalSuplents = new ArrayList<>();
        jugadorResultatListVisitantSuplents = new ArrayList<>();
        titlePartit = findViewById(R.id.tvPartitLocalVisitant);
        recyclerViewLocalTitulars = findViewById(R.id.recyclerViewResultatLocalTitulars);
        recyclerViewVisitantTitulars = findViewById(R.id.recyclerViewResultatVisitantTitulars);
        recyclerViewLocalSuplents = findViewById(R.id.recyclerViewResultatLocalSuplents);
        recyclerViewVisitantSuplents = findViewById(R.id.recyclerViewResultatVisitantSuplents);
       // tvEquipLocal = findViewById(R.id.tvEquipLocalResultat);
       // tvEquipVisitant = findViewById(R.id.tvEquipVisitantResultat);
    }
}