package com.example.beacon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";
    private static final String ID_SIMULATE_ACADEMICO = "2";

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
//        final Button button = view.findViewById(R.id.buttonTeste);
        final TextView textView = view.findViewById(R.id.textViewStatus);
        onInitThread(textView);

//        if (button != null) {
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String mensagemRetorno = Context.getAcademicoId().toString();
//                    //validarPresenca();
//                }
//            });
//        }

        return view;
    }

    private void onInitThread(final TextView textView) {
        final boolean[] presencaValidada = {false};
        new Thread() {
            @Override
            public void run() {
                while (!presencaValidada[0]) {
                    try {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), 17, 42);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                            presencaValidada[0] = true;
                            Presenca presenca = new Presenca(ID_SIMULATE_ACADEMICO, KEY_SIMULATE_BEACON, LocalDateTime.now().toString());
                            API.validarPresenca(presenca, new Callback<Presenca>() {
                                @Override
                                public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                                    if (response.body() != null){
                                        textView.setText(response.body().getMensagemRetorno());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Presenca> call, Throwable t) {
                                    textView.setText("Erro ao validar sua presença, iremos realizar uma nova tentativa em um minuto.");
//                                    presencaValidada[0] = false;
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
