package com.example.beacon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Academico;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AulasFragment extends Fragment {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";

    public AulasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_aulas, container, false);
        final Button button = (Button) view.findViewById(R.id.buttonTeste);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    API.getAcademicos(new Callback<List<Academico>>() {
                        @Override
                        public void onResponse(Call<List<Academico>> call, Response<List<Academico>> response) {
                            for (Academico academico : response.body()) {
                                System.out.println(academico.getNome());
                                System.out.println(academico.getCodigo());
                                System.out.println("------------------------");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Academico>> call, Throwable t) {
                            System.out.println("Erro ao recuperar os acadêmicos.");
                            System.out.println(t.getMessage());
                        }
                    });
                }
            });
        }

        return view;
    }
}
