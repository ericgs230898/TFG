package com.tfg_project.controlador;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
import com.tfg_project.model.beans.JugadorResultat;
import com.tfg_project.model.beans.PartitJugador;
import com.tfg_project.model.adapters.JugadorResultatAdapter;
import com.tfg_project.model.adapters.RecyclerItemClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultatsPartitPage extends AppCompatActivity {

    private static final String DOS_PUNTS = ": ";
    private static final String ENTER = "\n";

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
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_partit_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeVariables();
        setAdapterJugadorResultat();

        final String equipLocal = this.getIntent().getStringExtra("equipLocal");
        final String equipVisitant = this.getIntent().getStringExtra("equipVisitant");
        final String jugadorsLocal = this.getIntent().getStringExtra("loc");
        final String jugadorsVisitant = this.getIntent().getStringExtra("vis");
        final String golesLocal = this.getIntent().getStringExtra("golesLocal");
        final String golesVisitant = this.getIntent().getStringExtra("golesVisitant");
        final Type listType = new TypeToken<List<PartitJugador>>(){}.getType();
        final List<PartitJugador> jugLocals = new Gson().fromJson(jugadorsLocal, listType);
        final List<PartitJugador> jugVisitants = new Gson().fromJson(jugadorsVisitant, listType);
        titlePartit.setText(equipLocal + DOS_PUNTS + golesLocal + ENTER + equipVisitant + DOS_PUNTS + golesVisitant);

        final TextView tvLocal = findViewById(R.id.tvEquipLocalResultatPage);
        final TextView tvVisitant = findViewById(R.id.tvEquipVisitantResultatPage);

        tvLocal.setText(equipLocal);
        tvVisitant.setText(equipVisitant);

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
            public void onItemClick(int position) {
                showAlertDialog(jugadorResultatListLocalTitulars.get(position));
            }

            @Override
            public void onLongItemClick() {
            // non implemented
            }
        }));

        recyclerViewLocalSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewLocalSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showAlertDialog(jugadorResultatListLocalSuplents.get(position));
            }

            @Override
            public void onLongItemClick() {
                // non implemented
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
            public void onItemClick(int position) {
                showAlertDialog(jugadorResultatListVisitantTitulars.get(position));
            }

            @Override
            public void onLongItemClick() {
                // non implemented
            }
        }));

        recyclerViewVisitantSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewVisitantSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showAlertDialog(jugadorResultatListVisitantSuplents.get(position));
            }

            @Override
            public void onLongItemClick() {
                // non implemented
            }
        }));
    }

    private void showAlertDialog(JugadorResultat pj) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ESTADÍSTIQUES D'UN JUGADOR");
        final View customLayout = getLayoutInflater().inflate(R.layout.linear_layout_alert_dialog_resultat, null);
        TextView tvNom;
        TextView tvMinuts;
        TextView tvGols;
        TextView tvGolsPenal;
        TextView tvGolsPP;
        TextView tvGrogues;
        TextView tvVermelles;
        TextView tvGolesEncajados;
        tvNom = customLayout.findViewById(R.id.tvJugadorsNom);
        tvMinuts = customLayout.findViewById(R.id.tvJugadorsMinuts);
        tvGols = customLayout.findViewById(R.id.tvJugadorsGols);
        tvGolsPenal = customLayout.findViewById(R.id.tvJugadorsGolsPenal);
        tvGolsPP = customLayout.findViewById(R.id.tvJugadorsGolsPropia);
        tvGrogues = customLayout.findViewById(R.id.tvJugadorGrogues);
        tvVermelles = customLayout.findViewById(R.id.tvJugadorsVermelles);
        tvGolesEncajados = customLayout.findViewById(R.id.tvJugadorsGolesEncajados);
        tvNom.setText(pj.getNom());
        tvMinuts.setText(getString(R.string.minuts_jugats) + DOS_PUNTS + pj.getMinutsJugats());
        tvGols.setText(getString(R.string.gols_marcats) + DOS_PUNTS + pj.getGolesMarcados());
        tvGolsPenal.setText(getString(R.string.gols_penal) + DOS_PUNTS + pj.getGolesMarcadosPenalti());
        tvGolsPP.setText(getString(R.string.gols_propia) + DOS_PUNTS + pj.getGolesMarcadosPropia());
        if ( pj.isPortero() ) tvGolesEncajados.setText(getString(R.string.gols_rebuts) + DOS_PUNTS + pj.getGolesEncajados());
        int grogues = 0;
        if ( pj.isTarjetaAmarilla1()) grogues++;
        if ( pj.isTarjetaAmarilla2()) grogues++;
        tvGrogues.setText(getString(R.string.targetes_grogues) + DOS_PUNTS + grogues);
        if ( pj.isTarjetaRoja1() ) tvVermelles.setText(getString(R.string.targetes_vermelles) + DOS_PUNTS + "1");
        else tvVermelles.setText(getString(R.string.targetes_vermelles) + DOS_PUNTS + "0");


        builder.setView(customLayout);
        builder.setPositiveButton("OK", (dialogInterface, i) -> alertDialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void setAdapterJugadorResultat() {
        adapterLocalTitulars = new JugadorResultatAdapter(context, jugadorResultatListLocalTitulars);
        recyclerViewLocalTitulars.setAdapter(adapterLocalTitulars);
        recyclerViewLocalTitulars.setLayoutManager(new LinearLayoutManager(this));

        adapterVisitantTitulars = new JugadorResultatAdapter(context, jugadorResultatListVisitantTitulars);
        recyclerViewVisitantTitulars.setAdapter(adapterVisitantTitulars);
        recyclerViewVisitantTitulars.setLayoutManager(new LinearLayoutManager(this));

        adapterLocalSuplents = new JugadorResultatAdapter(context, jugadorResultatListLocalSuplents);
        recyclerViewLocalSuplents.setAdapter(adapterLocalSuplents);
        recyclerViewLocalSuplents.setLayoutManager(new LinearLayoutManager(this));

        adapterVisitantSuplents = new JugadorResultatAdapter(context, jugadorResultatListVisitantSuplents);
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
    }
}