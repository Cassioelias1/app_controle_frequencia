package com.example.beacon.api.endpoints;

import com.example.beacon.api.models.Presenca;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PresencaEndPoint {
    @GET("/api/presencas")
    Call<Presenca> getPresencas();

//    @GET("/api/escola/login")
//    Call<Escola> GetEscolasByCodigoSenha(@Query("usuario_smart") final String usuario_smart, @Query("senha_smart") final String senha_smart);
}
