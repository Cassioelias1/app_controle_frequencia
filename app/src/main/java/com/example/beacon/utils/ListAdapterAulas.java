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
import com.example.beacon.api.wrappers.PresencasAulasWrapper;
import com.example.beacon.context.AppContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListAdapterAulas extends ArrayAdapter<PresencasAulasWrapper> {

    private Context context;
    private List<PresencasAulasWrapper> presencas;
    private AulasActivity aulasActivity;
    public ListAdapterAulas(Context context, AulasActivity aulasActivity, List<PresencasAulasWrapper> presencas){
        super(context, 0, presencas);
        this.context = context;
        this.presencas = presencas;
        this.aulasActivity = aulasActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PresencasAulasWrapper presenca = presencas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_aulas, null);
        }

        TextView dataSaida = convertView.findViewById(R.id.nome_item);

        //TODO: Se possível, tentar deixar este código mais elegante
        try {
            Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(presenca.getDataValidacao());

            String text = new SimpleDateFormat("dd/MM/yyyy").format(dt);
            if (!presenca.isWithError()){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View o) {
                        AppContext.DATA_VALIDACAO_SELECTED = presenca.getDataValidacao();
                        aulasActivity.startActivity(new Intent(aulasActivity, DetalhamentoAulaActivity.class));
                    }
                });
                text += " - " + presenca.getNomeTurma();
            } else {
                dataSaida.setEnabled(false);
            }

            dataSaida.setText(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
