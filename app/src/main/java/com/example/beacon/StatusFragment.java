package com.example.beacon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.beacon.context.Context;

public class StatusFragment extends Fragment {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        final Button button = view.findViewById(R.id.buttonTeste);
        final TextView textView = view.findViewById(R.id.textViewTeste);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Implementar algo do tipo Context para armazenar o academico logado
                    //String mensagemRetorno = StatusService.Instance().validarPresencaByBeacon("123", "1");
                    //Usar o SharedPreferences ??????
                    String mensagemRetorno = Context.getAcademicoId().toString();
                    textView.setText(mensagemRetorno);
                }
            });
        }

        return view;
    }
}
