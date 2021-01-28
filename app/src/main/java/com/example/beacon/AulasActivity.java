package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.models.Turma;
import com.example.beacon.utils.ListAdapterAulas;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class AulasActivity extends AppCompatActivity {
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private ListAdapterAulas listAdapterTeste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);
        final ListView listAulas = findViewById(R.id.listAulas);
        final AulasActivity aulasActivity = this;
        initBottomNavigation();

        List<Turma> turmas = Arrays.asList(new Turma("Frequência Aula 1"), new Turma("Frequência Aula 2"));

        listAdapterTeste = new ListAdapterAulas(getApplicationContext(), aulasActivity, turmas);
        listAulas.setAdapter(listAdapterTeste);

//        API.getTurmasFromAcademicoId(new Callback<List<Turma>>() {
//            @Override
//            public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
//                List<Turma> turmas = response.body();
//                if (turmas != null && !turmas.isEmpty()) {
//                    listAdapterTeste = new ListAdapterAulas(getApplicationContext(), aulasActivity, turmas);
//                    listAulas.setAdapter(listAdapterTeste);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Turma>> call, Throwable t) {
//                //Implementar tratamento?
//            }
//        }, ID_SIMULATE_ACADEMICO);
    }

    private void initBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.aulas);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.status:
                        startActivity(new Intent(getApplicationContext(), RequestPermissionActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.aulas:
//                        startActivity(new Intent(getApplicationContext(), AulasActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sincronizacao:
                        startActivity(new Intent(getApplicationContext(), SincronizacaoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
