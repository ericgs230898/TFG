package com.tfg_project.model.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;
import com.tfg_project.model.beans.JugadorResultat;

import java.util.List;

public class JugadorResultatAdapter extends RecyclerView.Adapter<JugadorResultatAdapter.ViewHolder> {
    private final List<JugadorResultat> jugadorResultatsList;
    private final Context context;

    public JugadorResultatAdapter(Context context, List<JugadorResultat> jugadorResultatsList){
        this.jugadorResultatsList = jugadorResultatsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contextParent = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(contextParent);

        View jugadorsResultatView = inflater.inflate(R.layout.linear_layout_jugador_partit, parent, false);

        return new ViewHolder(jugadorsResultatView);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorResultatAdapter.ViewHolder holder, int position) {
        JugadorResultat jugadorResultat = jugadorResultatsList.get(position);

        TextView textView = holder.getTvNomJugador();
        StringBuilder stringBuilder = new StringBuilder(" ");
        if ( jugadorResultat.isPortero() ) stringBuilder.append(jugadorResultat.getNom()).append(" (Porter)");
        else stringBuilder.append(jugadorResultat.getNom());
        textView.setText(stringBuilder.toString());
        LinearLayout llJugador = holder.getLinearLayout();
        for ( int i=0; i<jugadorResultat.getGolesMarcados(); i++ ) {
            ImageView iv = getImageView(R.drawable.gol);
            llJugador.addView(iv);
        }
        for ( int i=0; i<jugadorResultat.getGolesMarcadosPropia(); i++ ){
            ImageView iv = getImageView(R.drawable.golpropia);
            llJugador.addView(iv);
        }
        for ( int i=0; i<jugadorResultat.getGolesMarcadosPenalti(); i++ ) {
            ImageView iv = getImageView(R.drawable.golpenal);
            llJugador.addView(iv);
        }
        if ( jugadorResultat.isTarjetaAmarilla1()){
            ImageView iv = getImageView(R.drawable.tarjeta_amarilla);
            llJugador.addView(iv);
        }
        if ( jugadorResultat.isTarjetaAmarilla2()){
            ImageView iv = getImageView(R.drawable.tarjeta_amarilla);
            llJugador.addView(iv);
        }
        if ( jugadorResultat.isTarjetaRoja1()){
            ImageView iv = getImageView(R.drawable.tarjeta_roja);
            llJugador.addView(iv);
        }
    }

    private ImageView getImageView(int resource) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(resource);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50,50);
        layoutParams.setMargins(5,0,5,0);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    @Override
    public int getItemCount() {
        return jugadorResultatsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomJugador;
        private final LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomJugador = itemView.findViewById(R.id.tvNomJugadorResultat);
            linearLayout = itemView.findViewById(R.id.linearLayoutPartitJugador);
        }

        public TextView getTvNomJugador() {
            return tvNomJugador;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }
    }
}
