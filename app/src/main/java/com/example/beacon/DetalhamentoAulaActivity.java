package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.context.AppContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetalhamentoAulaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhamento_aula);
        initBottomNavigation();
        Integer aulaId = AppContext.getAulaIdSelect();
        if (aulaId == null) {
            startActivity(new Intent(getApplicationContext(), AulasActivity.class));
        }
        //Fazer o find no servidor para trazer as presenças validadas do aluno

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
