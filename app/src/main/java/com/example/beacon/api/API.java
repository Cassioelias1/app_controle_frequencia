package com.example.beacon.api;

import com.example.beacon.api.endpoints.AcademicoEndPoint;
import com.example.beacon.api.endpoints.PresencaEndPoint;
import com.example.beacon.api.models.Academico;
import com.example.beacon.api.models.Presenca;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static final String URL = "https://86a26f8efa76.ngrok.io";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).build();


    public static void getAcademicos(final Callback<List<Academico>> callback) {
        AcademicoEndPoint endPoint = retrofit.create(AcademicoEndPoint.class);
        Call<List<Academico>> call;

        call = endPoint.GetAcademicos();
        call.enqueue(callback);
    }

    public static void validarPresenca(final Callback<Presenca> callback) {
        PresencaEndPoint endPoint = retrofit.create(PresencaEndPoint.class);
        Call<Presenca> call;

        call = endPoint.getPresencas();
        call.enqueue(callback);
    }

}
