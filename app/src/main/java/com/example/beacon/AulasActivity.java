package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.wrappers.PresencasAulasWrapper;
import com.example.beacon.context.AppContext;
import com.example.beacon.utils.ListAdapterAulas;
import com.example.beacon.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AulasActivity extends AppCompatActivity {
    private ListAdapterAulas listAdapterAulas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);
        final ListView listAulas = findViewById(R.id.listAulas);
        final AulasActivity aulasActivity = this;
        initBottomNavigation();

        API.getAllPresencas(new Callback<List<PresencasAulasWrapper>>() {
            @Override
            public void onResponse(Call<List<PresencasAulasWrapper>> call, Response<List<PresencasAulasWrapper>> response) {
                List<PresencasAulasWrapper> presencasAulasWrapperList = response.body();
                if (presencasAulasWrapperList != null){

                    listAdapterAulas = new ListAdapterAulas(getApplicationContext(), aulasActivity, presencasAulasWrapperList);
                    listAulas.setAdapter(listAdapterAulas);
                } else {
                    PresencasAulasWrapper presencasAulasWrapper = new PresencasAulasWrapper(null, null);
                    presencasAulasWrapper.setWithError(true);

                    listAdapterAulas = new ListAdapterAulas(getApplicationContext(), aulasActivity, Collections.singletonList(presencasAulasWrapper));
                    listAulas.setAdapter(listAdapterAulas);
                }
            }

            @Override
            public void onFailure(Call<List<PresencasAulasWrapper>> call, Throwable t) {
                PresencasAulasWrapper presencasAulasWrapper = new PresencasAulasWrapper(null, null);
                presencasAulasWrapper.setWithError(true);

                listAdapterAulas = new ListAdapterAulas(getApplicationContext(), aulasActivity, Collections.singletonList(presencasAulasWrapper));
                listAulas.setAdapter(listAdapterAulas);
            }
        }, Shared.getString(this, "academico_id"));
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
