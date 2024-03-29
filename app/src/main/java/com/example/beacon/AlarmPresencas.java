package com.example.beacon;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.context.AppContext;
import com.example.beacon.sqlite.BancoController;
import com.example.beacon.utils.Shared;
import com.example.beacon.utils.Util;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmPresencas extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nameCard = intent.getStringExtra("nameCard");
        String nameTextView = intent.getStringExtra("nameTextView");

        Presenca presenca = new Presenca(Shared.getString(context, "academico_id"), Shared.getString(context, "turma_id"), LocalDateTime.now().toString(), nameCard, nameTextView);
        //TODO: ainda falta chamar o método da trilateração pra setar o status
        //validarPresencaApi(presenca, nameCard, nameTextView, context);
    }

    private void validarPresencaApi(Presenca presenca, String nameMaterialCard, String nameTextView, Context context){
        API.validarPresenca(presenca, new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                //NÂO PRECISA FAZER NADA POIS SERA UTILIZADO EM BACKGROUND
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Util.sendNotification("Presença válidada", "Sua presença foi validando com sucesso!", notificationManager, context);
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Util.sendNotification("Erro ao validar presença", "Sua presença não foi validando, tente realizar a sincronização!", notificationManager, context);
                //savePresencaNaoComputada(presenca, nameMaterialCard, nameTextView, context);
            }
        });
    }

    private boolean savePresencaNaoComputada(Presenca presenca, String nameMaterialCard, String nameTextView, Context context){
        BancoController controller = new BancoController(context);
//        if (controller == null) {
//            controller = new BancoController(context);
//        }
        return controller.save(presenca.getData(), presenca.getIdAcademico(), presenca.getIdTurma(), presenca.getStatus(), nameMaterialCard, nameTextView);
    }
}
