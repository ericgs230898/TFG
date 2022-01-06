package com.tfg_project.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;
import com.tfg_project.model.beans.ClassificacioBean;

import java.util.List;

public class ClassificacioAdapter2 extends
        RecyclerView.Adapter<ClassificacioAdapter2.ViewHolder> {

    private List<ClassificacioBean> classificacioAdapterList;

    public ClassificacioAdapter2(List<ClassificacioBean> classificacioAdapterList) {
        this.classificacioAdapterList = classificacioAdapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View classificacioView = inflater.inflate(R.layout.linear_layout_classificacio2, parent, false);

        return new ClassificacioAdapter2.ViewHolder(classificacioView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificacioAdapter2.ViewHolder holder, int position) {
        ClassificacioBean classificacioBean = classificacioAdapterList.get(position);

        TextView tvPunts = holder.tvPuntsUsuari;
        tvPunts.setText(classificacioBean.getPunts());
        TextView tvUsername = holder.tvNomUsuari;
        tvUsername.setText(classificacioBean.getUsername());
        TextView tvPosicio = holder.tvPosicio;
        tvPosicio.setText(String.valueOf(position+4));
        tvPosicio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

    }

    @Override
    public int getItemCount() {
        return classificacioAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNomUsuari;
        public TextView tvPuntsUsuari;
        public TextView tvPosicio;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomUsuari = itemView.findViewById(R.id.tvClassificacioUsuari);
            tvPuntsUsuari = itemView.findViewById(R.id.tvClassificacioPunts);
            tvPosicio = itemView.findViewById(R.id.tvPosicioClassificacio);
        }
    }
}
