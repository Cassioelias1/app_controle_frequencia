package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.wrappers.PresencasAulasWrapper;
import com.example.beacon.utils.ListAdapterAulas;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class AulasActivity extends AppCompatActivity {
    private ListAdapterAulas listAdapterAulas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);
        final ListView listAulas = findViewById(R.id.listAulas);
        final AulasActivity aulasActivity = this;
        initBottomNavigation();


        //Fazer um get no servidor para retornar todas as presencas agrupadas, o retorno na verdade vai ser um wrapper
        //pois alguns informações estão em duas tabelas diferentes;
        List<PresencasAulasWrapper> presencas = Arrays.asList(new PresencasAulasWrapper("31/10/2021", "Geografia"));

        listAdapterAulas = new ListAdapterAulas(getApplicationContext(), aulasActivity, presencas);
        listAulas.setAdapter(listAdapterAulas);
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
