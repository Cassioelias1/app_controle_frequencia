package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetalhamentoAulaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhamento_aula);
        initBottomNavigation();
        //Fazer o find no servidor para trazer as presenças validadas do aluno
        //O find deve ser feito pelo id do academico e da turma onde ele está logado.
        //Futuramente após implementar o login salvar o id da turma e do acamido no AppContext ou naquele SharedPreferences.

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
