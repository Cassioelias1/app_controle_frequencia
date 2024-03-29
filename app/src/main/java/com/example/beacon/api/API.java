package com.example.beacon.api;

import com.example.beacon.api.endpoints.AcademicoEndPoint;
import com.example.beacon.api.endpoints.PresencaEndPoint;
import com.example.beacon.api.endpoints.TurmaEndPoint;
import com.example.beacon.api.models.Academico;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.api.models.Turma;
import com.example.beacon.api.wrappers.PresencasAulasWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
//    private static final String URL = "http://192.168.0.104:7555";
    private static final String URL = "http://scf.cassioelias.net.br";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).build();


    public static void getAcademicos(final Callback<List<Academico>> callback) {
        AcademicoEndPoint endPoint = retrofit.create(AcademicoEndPoint.class);
        Call<List<Academico>> call;

        call = endPoint.GetAcademicos();
        call.enqueue(callback);
    }

    public static void validarPresenca(Presenca presenca, final Callback<Presenca> callback) {
        PresencaEndPoint endPoint = retrofit.create(PresencaEndPoint.class);
        Call<Presenca> call;

        call = endPoint.validarPresenca(presenca);
        call.enqueue(callback);
    }

    public static void validarPresencasNaoComputadas(List<Presenca> presencas, final Callback<List<Presenca>> callback) {
        PresencaEndPoint endPoint = retrofit.create(PresencaEndPoint.class);
        Call<List<Presenca>> call;

        call = endPoint.validarPresencaNaoComputadas(presencas);
        call.enqueue(callback);
    }

    public static void validarLogin(final Callback<List<Academico>> callback, String codigo, String senha){
        AcademicoEndPoint endPoint = retrofit.create(AcademicoEndPoint.class);
        Call<List<Academico>> call;

        call = endPoint.GetAcademicoByEmailSenha(codigo, senha);
        call.enqueue(callback);
    }

    public static void getTurmasFromAcademicoId(final Callback<List<Turma>> callback, String academicoId){
        TurmaEndPoint endPoint = retrofit.create(TurmaEndPoint.class);
        Call<List<Turma>> call;

        call = endPoint.getTurmas(academicoId);
        call.enqueue(callback);
    }

    public static void getPresencaDiaAcademico(final Callback<List<Presenca>> callback, String academicoId, String dataPresenca) {
        PresencaEndPoint endPoint = retrofit.create(PresencaEndPoint.class);
        Call<List<Presenca>> call;

        call = endPoint.getPresencasValidadas(academicoId, dataPresenca);
        call.enqueue(callback);
    }

    public static void getAllPresencas(final Callback<List<PresencasAulasWrapper>> callback, String academicoId) {
        PresencaEndPoint endPoint = retrofit.create(PresencaEndPoint.class);
        Call<List<PresencasAulasWrapper>> call;

        call = endPoint.getAllPresencas(academicoId);
        call.enqueue(callback);
    }

    public static void getAulaDiaAcademico(final Callback<List<Turma>> callback, String academicoId, String diaSemana) {
        AcademicoEndPoint endPoint = retrofit.create(AcademicoEndPoint.class);
        Call<List<Turma>> call;

        call = endPoint.getAulaDiaAcademico(academicoId, diaSemana);
        call.enqueue(callback);
    }
}
