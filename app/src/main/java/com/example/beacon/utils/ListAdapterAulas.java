package com.example.beacon.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beacon.R;
import com.example.beacon.api.models.Turma;

import java.util.List;

public class ListAdapterAulas extends ArrayAdapter<Turma> {

    private Context context;
    private List<Turma> itens;
    public ListAdapterAulas(Context context, List<Turma> itens){
        super(context, 0, itens);
        this.context = context;
        this.itens = itens;
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
                System.out.println(turma.getId());
            }
        });

        TextView dataSaida = convertView.findViewById(R.id.nome_item);
        dataSaida.setText(turma.getDescricao());

        return convertView;
    }
}
