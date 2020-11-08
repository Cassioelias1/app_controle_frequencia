package com.example.beacon;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private Handler handler;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_status, container, false);
        handler = new Handler();

        MaterialCardView materialCardView1915 = view.findViewById(R.id.card);
        TextView textView1915 = view.findViewById(R.id.textView1915);

        MaterialCardView materialCardView2015 = view.findViewById(R.id.card2);
        TextView textView2015 = view.findViewById(R.id.textView2015);

        MaterialCardView materialCardView2100 = view.findViewById(R.id.card3);
        TextView textView2100 = view.findViewById(R.id.textView2100);

        MaterialCardView materialCardView2140 = view.findViewById(R.id.card4);
        TextView textView2140 = view.findViewById(R.id.textView2140);

        onInitThread(materialCardView1915, textView1915, 23, 11);
        onInitThread(materialCardView2015, textView2015, 23, 11);
        onInitThread(materialCardView2100, textView2100, 23, 11);
        onInitThread(materialCardView2140, textView2140, 23, 11);

        List<MaterialCardView> materialCardViews = new ArrayList<>();
        materialCardViews.add(materialCardView1915);
        materialCardViews.add(materialCardView2015);
        materialCardViews.add(materialCardView2100);
        materialCardViews.add(materialCardView2140);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(textView1915);
        textViews.add(textView2015);
        textViews.add(textView2100);
        textViews.add(textView2140);

        resetCardsPresencas(materialCardViews, textViews, 23, 7);

        return view;
    }

    public void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute) {
        final boolean[] presencaValidada = {false};
        new Thread() {
            @Override
            public void run() {
                while (!presencaValidada[0]) {
                    try {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                            presencaValidada[0] = true;

                            Presenca presenca = new Presenca(ID_SIMULATE_ACADEMICO, KEY_SIMULATE_BEACON, LocalDateTime.now().toString());
                            API.validarPresenca(presenca, new Callback<Presenca>() {
                                @Override
                                public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                                    if (response.body() != null){
//                                        textView.setText(response.body().getMensagemRetorno());
                                        if (response.body().getMensagemRetorno() != null){
                                            handler.post(new Runnable(){
                                                @Override
                                                public void run() {
                                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                                    textView.setText("Presença válidada!");
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Presenca> call, Throwable t) {
//                                    textView.setText("Erro ao validar sua presença, iremos realizar uma nova tentativa em um minuto.");
//                                    presencaValidada[0] = false;
                                    System.out.println("DEU RUIM CARA AF");
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

    private void resetCardsPresencas(final List<MaterialCardView> materialCardViews, final List<TextView> textViews, final Integer hour, final Integer minute){
        final boolean[] resetRealizado = {false};

        new Thread(){
            @Override
            public void run() {
                try {
                    while (!resetRealizado[0]) {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()) {
                            resetRealizado[0] = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (MaterialCardView materialCardView : materialCardViews) {
                                        materialCardView.setBackgroundColor(Color.parseColor("#E7731E"));
                                    }

                                    for (TextView textView : textViews) {
                                        textView.setText("Aguardando Horário");
                                    }
                                }
                            });

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
