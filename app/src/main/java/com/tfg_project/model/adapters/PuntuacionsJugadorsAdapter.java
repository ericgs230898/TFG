package com.tfg_project.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;
import com.tfg_project.model.beans.JugadorPuntuacio;

import java.util.List;

public class PuntuacionsJugadorsAdapter extends RecyclerView.Adapter<PuntuacionsJugadorsAdapter.ViewHolder> {

    final List<JugadorPuntuacio> jugadorPuntuacioList;

    public PuntuacionsJugadorsAdapter(List<JugadorPuntuacio> list){
        jugadorPuntuacioList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_layout_jugador_puntuacio, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntuacionsJugadorsAdapter.ViewHolder holder, int position) {
        String nomJugador = jugadorPuntuacioList.get(position).getNomJugador();
        String punts = jugadorPuntuacioList.get(position).getPunts();
        String posicio = jugadorPuntuacioList.get(position).getPosicio();
        holder.getTvPuntuacio().setText(punts);
        holder.getTvNomJugador().setText(nomJugador);
        holder.getTvPosicio().setText(posicio);
    }

    @Override
    public int getItemCount() {
        return jugadorPuntuacioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomJugador;
        private final TextView tvPuntuacio;
        private final TextView tvPosicio;

        public ViewHolder(View view) {
            super(view);
            tvNomJugador = view.findViewById(R.id.tvJugadorPuntuacioNomJugador);
            tvPuntuacio = view.findViewById(R.id.tvJugadorPuntuacioPunts);
            tvPosicio = view.findViewById(R.id.tvJugadorPuntuacioPosicio);
        }

        public TextView getTvNomJugador() {
            return tvNomJugador;
        }

        public TextView getTvPuntuacio() {
            return tvPuntuacio;
        }

        public TextView getTvPosicio() {
            return tvPosicio;
        }
    }
}
