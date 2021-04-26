package com.example.beacon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.context.AppContext;
import com.example.beacon.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhamentoAulaActivity extends AppCompatActivity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        //Fazer o find no servidor para trazer as presenças validadas do aluno
        //O find deve ser feito pelo id do academico e da turma onde ele está logado.
        //Futuramente após implementar o login salvar o id da turma e do acamido no AppContext ou naquele SharedPreferences.

//        TextView textView = findViewById(R.id.horario_detalhamento_presenca);
//        textView.setText(LocalDate.parse(dataValidacaoSelected).toString());

        getPresencasJaValidadas(LocalDate.parse(AppContext.DATA_VALIDACAO_SELECTED).toString());
    }

    private void setNomeAulaPresencaComputada(Presenca presenca){
        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
        textViewNomeDisciplica.setText(presenca != null ? presenca.getDescricao() : "Disciplina não encontrada.");
    }

    private void getPresencasJaValidadas(String data){
        API.getPresencaDiaAcademico(new Callback<List<Presenca>>() {
            @Override
            public void onResponse(Call<List<Presenca>> call, Response<List<Presenca>> response) {
                List<Presenca> presencas = response.body();

                if (presencas != null) {
                    setNomeAulaPresencaComputada(presencas.get(0));//posso encaminhar a primeira pois o nome da aula é o mesmo para todos.
                    for (Presenca presenca : presencas) {
                        String nameMaterialCard = presenca.getMaterialCardId();
                        String nameTextView = presenca.getTextViewId();
                        if (nameMaterialCard != null && nameTextView != null) {
                            int idMaterialCard = getResources().getIdentifier(nameMaterialCard, "id", context.getPackageName());
                            int idTextView = getResources().getIdentifier(nameTextView, "id", context.getPackageName());
                            MaterialCardView materialCardView = findViewById(idMaterialCard);
                            TextView textView = findViewById(idTextView);

                            if (materialCardView != null && textView != null) {
                                if ("PRESENTE".equals(presenca.getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                    textView.setText("Presença válidada!");
                                } else if ("AUSENTE".equals(presenca.getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#b00e29"));
                                    textView.setText("Falta computada!");
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Presenca>> call, Throwable t) {

            }
        }, Shared.getString(context, "academico_id"), data);
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
