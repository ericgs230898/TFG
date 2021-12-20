package com.tfg_project.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;

import java.util.ArrayList;
import java.util.List;

public class JugadorResultatAdapter extends RecyclerView.Adapter<JugadorResultatAdapter.ViewHolder> {
    private List<JugadorResultat> jugadorResultatsList;

    public JugadorResultatAdapter(List<JugadorResultat> jugadorResultatsList){
        this.jugadorResultatsList = jugadorResultatsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View jugadorsResultatView = inflater.inflate(R.layout.linear_layout_jugador_partit, parent, false);

        return new JugadorResultatAdapter.ViewHolder(jugadorsResultatView);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorResultatAdapter.ViewHolder holder, int position) {
        JugadorResultat jugadorResultat = jugadorResultatsList.get(position);

        TextView textView = holder.tvNomJugador;
        StringBuilder stringBuilder = new StringBuilder(" ");
        if ( jugadorResultat.isPortero() ) stringBuilder.append(jugadorResultat.getNom()).append(" (Porter)");
        else stringBuilder.append(jugadorResultat.getNom());
        textView.setText(stringBuilder.toString());
    }

    private static int getImageResourceByAccio(String accio) {
        if ( accio.equals("TarjetaAmarilla")) return R.drawable.tarjeta_amarilla;
        else if ( accio.equals("TarjetaRoja")) return R.drawable.tarjeta_roja;
        else if ( accio.equals("Gol")) return R.drawable.gol;
        else if ( accio.equals("GolPenalti")) return R.drawable.golpenal;
        else return R.drawable.golpropia;
    }

    @Override
    public int getItemCount() {
        return jugadorResultatsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNomJugador;
        public TextView tvMinutsJugats;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomJugador = itemView.findViewById(R.id.tvNomJugadorResultat);
        }
    }
}
