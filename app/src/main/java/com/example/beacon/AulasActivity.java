package com.example.beacon;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Turma;
import com.example.beacon.utils.ListAdapterAulas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AulasActivity extends AppCompatActivity {
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private ListAdapterAulas listAdapterTeste;

    public AulasActivity(){

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.activity_aulas, container, false);
//                final ListView listAulas = view.findViewById(R.id.listAulas);
//        API.getTurmasFromAcademicoId(new Callback<List<Turma>>() {
//            @Override
//            public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
//                List<Turma> turmas = response.body();
//                if (turmas != null && !turmas.isEmpty()) {
//                    listAdapterTeste = new ListAdapterAulas(getContext(), null, turmas);
//                    listAulas.setAdapter(listAdapterTeste);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Turma>> call, Throwable t) {
//                //Implementar tratamento?
//            }
//        }, ID_SIMULATE_ACADEMICO);
//        return view;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);
        final ListView listAulas = findViewById(R.id.listAulas);
        final AulasActivity aulasActivity = this;

        API.getTurmasFromAcademicoId(new Callback<List<Turma>>() {
            @Override
            public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                List<Turma> turmas = response.body();
                if (turmas != null && !turmas.isEmpty()) {
                    listAdapterTeste = new ListAdapterAulas(getApplicationContext(), aulasActivity, turmas);
                    listAulas.setAdapter(listAdapterTeste);
                }
            }

            @Override
            public void onFailure(Call<List<Turma>> call, Throwable t) {
                //Implementar tratamento?
            }
        }, ID_SIMULATE_ACADEMICO);
    }
}
