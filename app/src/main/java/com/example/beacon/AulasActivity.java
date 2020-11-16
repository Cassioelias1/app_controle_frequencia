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
        initBottomNavigation();

        List<Turma> turmas = Arrays.asList(new Turma("Teste"), new Turma("teste2"), new Turma("teste3"), new Turma("teste4"), new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"),new Turma("teste2"));

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
                        startActivity(new Intent(getApplicationContext(), StatusActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.aulas:
//                        startActivity(new Intent(getApplicationContext(), AulasActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.presencas:
                        startActivity(new Intent(getApplicationContext(), PresencasActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
