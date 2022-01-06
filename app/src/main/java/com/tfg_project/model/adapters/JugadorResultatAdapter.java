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
    private List<JugadorResultat> jugadorResultatsList;
    private Context context;

    public JugadorResultatAdapter(Context context, List<JugadorResultat> jugadorResultatsList){
        this.jugadorResultatsList = jugadorResultatsList;
        this.context = context;
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
        LinearLayout llJugador = holder.linearLayout;

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
        imageView.setImageResource(resource);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50,50, 50);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
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
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomJugador = itemView.findViewById(R.id.tvNomJugadorResultat);
            linearLayout = itemView.findViewById(R.id.linearLayoutPartitJugador);
        }
    }
}
