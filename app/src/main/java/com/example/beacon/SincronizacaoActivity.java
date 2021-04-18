package com.example.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.sqlite.BancoController;
import com.example.beacon.utils.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizacaoActivity extends AppCompatActivity {
    Context context = this;
    BancoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacao);
        initBottomNavigation();
        initButtons();
        verificarPresencasNaoComputadas();
    }

    private List<Presenca> getPresencasNaoComputadasSqlite(){
        if (controller == null){
            controller = new BancoController(context);
        }
        return controller.list();
    }

    private void verificarPresencasNaoComputadas(){
        List<Presenca> presencas = getPresencasNaoComputadasSqlite();

        TextView numeroPresencasNaoComputadas = findViewById(R.id.numeroPresencasNaoComputadas);
        numeroPresencasNaoComputadas.setText(presencas != null && !presencas.isEmpty()
                ? "Você possui " + presencas.size() + " presenças não computadas."
                : "Você não possui presenças para sincronizar.");
    }

    private void initButtons(){
        Button button = findViewById(R.id.buttonAsync);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Presenca> presencas = getPresencasNaoComputadasSqlite();
                if (presencas.size() == 0){
                    Util.showToastMessage(context, "Você não possui presenças para sincronizar.");
                    return;
                }
//                for (Presenca presenca : presencas) {
//                    if (presenca.getIdTurma() == null){
//                        presenca.setIdTurma("1");
//                    }
//                }
                sendPresencasNaoComputadas(presencas);
            }
        });
    }

    private void deletePresencasSqliteSincronizadas(List<Presenca> presencas){
        for (Presenca presenca : presencas) {
            controller.delete(presenca.getIdSqlite());
        }

        verificarPresencasNaoComputadas();
    }

    private void sendPresencasNaoComputadas(List<Presenca> presencas) {
        API.validarPresencasNaoComputadas(presencas, new Callback<List<Presenca>>() {
            @Override
            public void onResponse(Call<List<Presenca>> call, Response<List<Presenca>> response) {
                Util.showToastMessage(context, "Presenças sincronizadas com o servidor com sucesso.");
                deletePresencasSqliteSincronizadas(presencas);
            }

            @Override
            public void onFailure(Call<List<Presenca>> call, Throwable t) {
                Util.showToastMessage(context, "Não foi possível fazer a sincronização com o servidor, tente novamente mais tarde.");
            }
        });
    }

    private void initBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.sincronizacao);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.status:
                        startActivity(new Intent(getApplicationContext(), RequestPermissionActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.aulas:
                        startActivity(new Intent(getApplicationContext(), AulasActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.sincronizacao:
//                        startActivity(new Intent(getApplicationContext(), SincronizacaoActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
