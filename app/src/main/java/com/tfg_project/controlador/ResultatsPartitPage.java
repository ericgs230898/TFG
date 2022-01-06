package com.tfg_project.controlador;

import android.content.Context;
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

    // private static final String TAG = "RESULTATS_PARTIT_PAGE_TAG";
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

        initializeVariables();
        setAdapterJugadorResultat();

        final String competicio = this.getIntent().getStringExtra("competicio");
        final String grup = this.getIntent().getStringExtra("grup");
        final String jornada = this.getIntent().getStringExtra("jornada");
        final String equipLocal = this.getIntent().getStringExtra("equipLocal");
        final String equipVisitant = this.getIntent().getStringExtra("equipVisitant");
        final String jugadorsLocal = this.getIntent().getStringExtra("loc");
        final String jugadorsVisitant = this.getIntent().getStringExtra("vis");
        final String golesLocal = this.getIntent().getStringExtra("golesLocal");
        final String golesVisitant = this.getIntent().getStringExtra("golesVisitant");
        final Type listType = new TypeToken<List<PartitJugador>>(){}.getType();
        final List<PartitJugador> jugLocals = new Gson().fromJson(jugadorsLocal, listType);
        final List<PartitJugador> jugVisitants = new Gson().fromJson(jugadorsVisitant, listType);
        titlePartit.setText(new StringBuilder().append(equipLocal).append(DOS_PUNTS).append(golesLocal).append(ENTER).append(equipVisitant).append(DOS_PUNTS).append(golesVisitant).toString());

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
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListLocalTitulars.get(position));
                System.out.println(jugadorResultatListLocalTitulars.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewLocalSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewLocalSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListLocalSuplents.get(position));
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
                showAlertDialog(jugadorResultatListVisitantTitulars.get(position));
                System.out.println(jugadorResultatListVisitantTitulars.get(position).getNom());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewVisitantSuplents.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerViewVisitantSuplents, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showAlertDialog(jugadorResultatListVisitantSuplents.get(position));
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

    private void showAlertDialog(JugadorResultat pj) {
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
        tvMinuts.setText(new StringBuilder().append(getString(R.string.minuts_jugats)).append(DOS_PUNTS).append(pj.getMinutsJugats()).toString());
        tvGols.setText(new StringBuilder().append(getString(R.string.gols_marcats)).append(DOS_PUNTS).append(pj.getGolesMarcados()).toString());
        tvGolsPenal.setText(new StringBuilder().append(getString(R.string.gols_penal)).append(DOS_PUNTS).append(pj.getGolesMarcadosPenalti()).toString());
        tvGolsPP.setText(new StringBuilder().append(getString(R.string.gols_propia)).append(DOS_PUNTS).append(String.valueOf(pj.getGolesMarcadosPropia())).toString());
        if ( pj.isPortero() ) tvGolesEncajados.setText(new StringBuilder().append(getString(R.string.gols_rebuts)).append(DOS_PUNTS).append(pj.getGolesEncajados()).toString());
        int grogues = 0;
        if ( pj.isTarjetaAmarilla1()) grogues++;
        if ( pj.isTarjetaAmarilla2()) grogues++;
        tvGrogues.setText(new StringBuilder().append(getString(R.string.targetes_grogues)).append(DOS_PUNTS).append(grogues).toString());
        if ( pj.isTarjetaRoja1() ) tvVermelles.setText(new StringBuilder().append(getString(R.string.targetes_vermelles)).append(DOS_PUNTS).append("1").toString());
        else tvVermelles.setText(new StringBuilder().append(getString(R.string.targetes_vermelles)).append(DOS_PUNTS).append("0").toString());


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