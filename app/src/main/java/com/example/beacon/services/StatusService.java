package com.example.beacon.services;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusService {
    public static StatusService Instance(){
        return new StatusService();
    }

    public String validarPresencaByBeacon(String idBeacon, String idAcademico) {
        //Fazer um find no servidor por esse id afim de encontrar a sala que o beacon representa
        //validar a presença do aluno, retornar um boolean para confirmar o sucesso?
        final String[] messageReturn = {"Não foi possível validar sua presença"};
        API.validarPresenca(new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                if (response.body() != null) {
                    messageReturn[0] = response.body().getMensagemRetorno();
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                //Não precisar fazer nada
            }
        });

        return messageReturn[0];
    }
}
