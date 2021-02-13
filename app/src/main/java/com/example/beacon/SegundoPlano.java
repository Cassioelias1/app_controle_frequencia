package com.example.beacon;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.context.AppContext;
import com.example.beacon.sqlite.BancoController;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SegundoPlano extends AsyncTask<Void, Void, String> {
    private BancoController controller;
    private MaterialCardView materialCardView1915;
    private TextView textView1915;
    private Integer hour;
    private Integer minute;
    private Thread thread;
    private String nameMaterialCard;
    private String nameTextView;
    private Handler handler;
    private Context context;

    public SegundoPlano(MaterialCardView materialCardView1915, TextView textView1915, Integer hour, Integer minute, Thread thread, String nameMaterialCard, String nameTextView, Handler handler, Context context) {
        this.materialCardView1915 = materialCardView1915;
        this.textView1915 = textView1915;
        this.hour = hour;
        this.minute = minute;
        this.thread = thread;
        this.nameMaterialCard = nameMaterialCard;
        this.nameTextView = nameTextView;
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        onInitThread(materialCardView1915, textView1915, hour, minute, thread, nameMaterialCard, nameTextView, handler, context);
        return null;
    }

    private void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute, Thread thread, String nameMaterialCard, String nameTextView, Handler handler, Context context) {
        final boolean[] presencaValidada = {false};

        if (thread == null){
            thread = new Thread() {
                @Override
                public void run() {
                    while (!presencaValidada[0]) {
                        try {
                            LocalDateTime agora = LocalDateTime.now();
                            LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                            if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                                //Se não tiver pelo menos 3 idsBeacon significa que o aluno não esta dentro da sala de aula, implementar outras validações (trilateração)
                                //salvar turmaId no Context.
//                                Presenca presenca = new Presenca(AppContext.getAcademicoId(), AppContext.getTurmaId(), LocalDateTime.now().toString(), nameMaterialCard, nameTextView);
                                Presenca presenca = new Presenca("1", "1", LocalDateTime.now().toString(), nameMaterialCard, nameTextView);

                                presenca.setStatusTrilateracao(false);
                                validarPresencaApi(presenca, materialCardView, textView, handler, context);

                                presencaValidada[0] = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            switch (nameMaterialCard) {
                case "card_19_15":
                    AppContext.setThread1915(thread);
                    break;
                case "card_20_15":
                    AppContext.setThread2015(thread);
                    break;
                case "card_21_00":
                    AppContext.setThread2100(thread);
                    break;
                case "card_21_40":
                    AppContext.setThread2140(thread);
                    break;
            }

            if (!thread.isAlive()){
                thread.start();
            }
        }
    }

    private void validarPresencaApi(Presenca presenca, MaterialCardView materialCardView, TextView textView, Handler handler, Context context){
        API.validarPresenca(presenca, new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                if (response.body() != null){
                    if (response.body().getMensagemRetorno() != null){
                        handler.post(new Runnable(){
                            @Override
                            public void run() {
                                if ("PRESENTE".equals(response.body().getStatus())){
                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                    textView.setText("Presença válidada!");
                                } else if("AUSENTE".equals(response.body().getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#b00e29"));
                                    textView.setText("Falta computada!");
                                }
                            }
                        });
                    }
                } else {
                    savePresencaNaoComputada(presenca, materialCardView.getId(), textView.getId(), context);
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                savePresencaNaoComputada(presenca, materialCardView.getId(), textView.getId(), context);
            }
        });
    }
    private boolean savePresencaNaoComputada(Presenca presenca, Integer materialCardId, Integer textViewId, Context context){
        if (controller == null) {
            controller = new BancoController(context);
        }
//        return controller.save(presenca.getData(), presenca.getIdAcademico(), presenca.getIdTurma(), presenca.getStatus(), materialCardId, textViewId);
        return false;
    }
}
