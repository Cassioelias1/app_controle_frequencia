package com.example.beacon.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beacon.AulasActivity;
import com.example.beacon.DetalhamentoAulaActivity;
import com.example.beacon.R;
import com.example.beacon.api.models.Turma;

import java.util.List;

public class ListAdapterAulas extends ArrayAdapter<Turma> {

    private Context context;
    private List<Turma> itens;
    private AulasActivity aulasActivity;
    public ListAdapterAulas(Context context, AulasActivity aulasActivity, List<Turma> itens){
        super(context, 0, itens);
        this.context = context;
        this.itens = itens;
        this.aulasActivity = aulasActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Turma turma = itens.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_aulas, null);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View o) {
                aulasActivity.startActivity(new Intent(aulasActivity, DetalhamentoAulaActivity.class));
            }
        });

        TextView dataSaida = convertView.findViewById(R.id.nome_item);
        dataSaida.setText(turma.getDescricao());

        return convertView;
    }
}
