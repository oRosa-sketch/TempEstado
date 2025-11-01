package com.faculdade.tempestado.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faculdade.tempestado.model.PrevisaoDiaria;
import com.faculdade.tempestado.R;

import java.util.List;

public class PrevisaoAdapter extends RecyclerView.Adapter<PrevisaoAdapter.PrevisaoViewHolder> {

    private List<PrevisaoDiaria> previsaoList;
    private Context context;

    public PrevisaoAdapter(Context context, List<PrevisaoDiaria> previsaoList) {
        this.context = context;
        this.previsaoList = previsaoList;
    }

    @NonNull
    @Override
    public PrevisaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_previsao, parent, false);
        return new PrevisaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrevisaoViewHolder holder, int position) {
        PrevisaoDiaria previsao = previsaoList.get(position);

        String dataDia = previsao.getData() + " (" + previsao.getDiaDaSemana() + ")";
        String tempMax = "Max: " + previsao.getTemperaturaMaxima() + "°C";
        String tempMin = "Min: " + previsao.getTemperaturaMinima() + "°C";

        holder.tvItemDataDia.setText(dataDia);
        holder.tvItemDescricao.setText(previsao.getDescricao());
        holder.tvItemMax.setText(tempMax);
        holder.tvItemMin.setText(tempMin);
    }

    @Override
    public int getItemCount() {
        if (previsaoList == null) {
            return 0;
        }
        return previsaoList.size();
    }

    public static class PrevisaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemDataDia;
        TextView tvItemDescricao;
        TextView tvItemMax;
        TextView tvItemMin;

        public PrevisaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemDataDia = itemView.findViewById(R.id.tvItemDataDia);
            tvItemDescricao = itemView.findViewById(R.id.tvItemDescricao);
            tvItemMax = itemView.findViewById(R.id.tvItemMax);
            tvItemMin = itemView.findViewById(R.id.tvItemMin);
        }
    }
}