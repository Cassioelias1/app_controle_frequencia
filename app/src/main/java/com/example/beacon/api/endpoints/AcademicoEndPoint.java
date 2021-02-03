package com.example.beacon.api.endpoints;

import com.example.beacon.api.models.Academico;
import com.example.beacon.api.wrappers.AcademicoTurmaWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AcademicoEndPoint {
    @GET("/api/academicos")
    Call<List<Academico>> GetAcademicos();

    @GET("/api/academicos/login")
    Call<AcademicoTurmaWrapper> GetAcademicoByEmailSenha(@Query("email") final String email, @Query("senha") final String senha);

//    @GET("/api/escola/login")
//    Call<Escola> GetEscolasByCodigoSenha(@Query("usuario_smart") final String usuario_smart, @Query("senha_smart") final String senha_smart);
}
