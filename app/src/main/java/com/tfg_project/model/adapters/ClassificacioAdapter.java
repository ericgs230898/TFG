package com.tfg_project.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;
import com.tfg_project.model.beans.ClassificacioBean;

import java.util.List;

public class ClassificacioAdapter extends
        RecyclerView.Adapter<ClassificacioAdapter.ViewHolder> {

    private List<ClassificacioBean> classificacioAdapterList;

    public ClassificacioAdapter(List<ClassificacioBean> classificacioAdapterList) {
        this.classificacioAdapterList = classificacioAdapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View classificacioView = inflater.inflate(R.layout.linear_layout_classificacio, parent, false);

        return new ClassificacioAdapter.ViewHolder(classificacioView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificacioAdapter.ViewHolder holder, int position) {
        ClassificacioBean classificacioBean = classificacioAdapterList.get(position);

        TextView tvPunts = holder.tvPuntsUsuari;
        tvPunts.setText(classificacioBean.getPunts());
        TextView tvUsername = holder.tvNomUsuari;
        tvUsername.setText(classificacioBean.getUsername());
        ImageView posicio = holder.ivPosicio;
        posicio.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
        posicio.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if ( position == 0 ) {
            posicio.setImageResource(R.drawable.primer_puesto);
        } else if ( position == 1 ) {
            posicio.setImageResource(R.drawable.segon_puesto);
        } else if ( position == 2 ) {
            posicio.setImageResource(R.drawable.tercer_puesto);
        }
    }

    @Override
    public int getItemCount() {
        return classificacioAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNomUsuari;
        public TextView tvPuntsUsuari;
        public ImageView ivPosicio;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomUsuari = itemView.findViewById(R.id.tvClassificacioUsuari);
            tvPuntsUsuari = itemView.findViewById(R.id.tvClassificacioPunts);
            ivPosicio = itemView.findViewById(R.id.ivPosicioClassificacio);
        }
    }
}
