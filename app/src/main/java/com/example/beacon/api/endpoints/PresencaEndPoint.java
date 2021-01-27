package com.example.beacon.api.endpoints;

import com.example.beacon.api.models.Presenca;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PresencaEndPoint {
    @GET("/api/presencas")
    Call<Presenca> getPresencas();

    @POST("/api/presencas")
    Call<Presenca> validarPresenca(@Body Presenca presenca);

    @GET("/api/presencas/getbyacademico")
    Call<List<Presenca>> getPresencasValidadas(@Query("academico_id") final String academicoId, @Query("data_presenca") final String dataPresenca);
}
