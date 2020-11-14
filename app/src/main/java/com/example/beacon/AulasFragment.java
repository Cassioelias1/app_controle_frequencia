package com.example.beacon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Turma;
import com.example.beacon.utils.ListAdapterAulas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AulasFragment extends Fragment {
    private static final String ID_SIMULATE_ACADEMICO = "2";
    public AulasFragment() {
        // Required empty public constructor
    }
    ListAdapterAulas listAdapterTeste;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_aulas, container, false);
        final ListView listAulas = view.findViewById(R.id.listAulas);

        API.getTurmasFromAcademicoId(new Callback<List<Turma>>() {
            @Override
            public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                List<Turma> turmas = response.body();
                if (turmas != null && !turmas.isEmpty()) {
                    listAdapterTeste = new ListAdapterAulas(getContext(), turmas);
                    listAulas.setAdapter(listAdapterTeste);
                }
            }

            @Override
            public void onFailure(Call<List<Turma>> call, Throwable t) {
                //Implementar tratamento?
            }
        }, ID_SIMULATE_ACADEMICO);

        return view;
    }
}
