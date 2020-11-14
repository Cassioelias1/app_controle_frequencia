package com.example.beacon.api.endpoints;

import com.example.beacon.api.models.Turma;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TurmaEndPoint {
    @GET("/api/turma/getbyacademico")
    Call<List<Turma>> getTurmas(@Query("academicoId") final String academicoId);

}
