package com.example.beacon;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Academico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Button button = (Button) view.findViewById(R.id.buttonTeste);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    API.getAcademicos(new Callback<List<Academico>>() {
                        @Override
                        public void onResponse(Call<List<Academico>> call, Response<List<Academico>> response) {

                        }

                        @Override
                        public void onFailure(Call<List<Academico>> call, Throwable t) {

                        }
                    });
                }
            });
        }

        return view;
    }
}
