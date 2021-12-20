package com.tfg_project.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg_project.R;

import java.util.List;

public class LliguesVirtualsAdapter extends
        RecyclerView.Adapter<LliguesVirtualsAdapter.ViewHolder> {

    private List<LliguesVirtuals> lliguesVirtualsList;

    public LliguesVirtualsAdapter(List<LliguesVirtuals> lliguesVirtuals){
        this.lliguesVirtualsList = lliguesVirtuals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View lliguesVirtualsView = inflater.inflate(R.layout.linear_layout_lligues_usuari, parent, false);

        return new ViewHolder(lliguesVirtualsView);
    }

    @Override
    public void onBindViewHolder(@NonNull LliguesVirtualsAdapter.ViewHolder holder, int position) {
        LliguesVirtuals lliguesVirtuals = lliguesVirtualsList.get(position);

        TextView textView = holder.tvNomLliga;
        textView.setText(lliguesVirtuals.getNomLligaVirtual());
        TextView textView1 = holder.tvParticipants;
        textView1.setText(lliguesVirtuals.getParticipants());
        TextView textView2 = holder.tvCompeticio;
        textView2.setText(lliguesVirtuals.getCompeticio());
        TextView textView3 = holder.tvGrup;
        textView3.setText(lliguesVirtuals.getGrup());

    }

    @Override
    public int getItemCount() {
        return lliguesVirtualsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNomLliga;
        public TextView tvCompeticio;
        public TextView tvGrup;
        public TextView tvParticipants;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomLliga = itemView.findViewById(R.id.tvNomLliga);
            tvCompeticio = itemView.findViewById(R.id.tvCompeticio);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
            tvGrup = itemView.findViewById(R.id.tvGrup);
        }
    }
}
