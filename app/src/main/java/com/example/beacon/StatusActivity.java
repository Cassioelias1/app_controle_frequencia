package com.example.beacon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends AppCompatActivity {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private Handler handler;

    public StatusActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        handler = new Handler();

        MaterialCardView materialCardView1915 = findViewById(R.id.card);
        TextView textView1915 = findViewById(R.id.textView1915);

        MaterialCardView materialCardView2015 = findViewById(R.id.card2);
        TextView textView2015 = findViewById(R.id.textView2015);

        MaterialCardView materialCardView2100 = findViewById(R.id.card3);
        TextView textView2100 = findViewById(R.id.textView2100);

        MaterialCardView materialCardView2140 = findViewById(R.id.card4);
        TextView textView2140 = findViewById(R.id.textView2140);

//        onInitThread(materialCardView1915, textView1915, 19, 15);
//        onInitThread(materialCardView2015, textView2015, 23, 11);
//        onInitThread(materialCardView2100, textView2100, 23, 11);
//        onInitThread(materialCardView2140, textView2140, 23, 11);

        List<MaterialCardView> materialCardViews = new ArrayList<>();
        materialCardViews.add(materialCardView1915);
        materialCardViews.add(materialCardView2015);
        materialCardViews.add(materialCardView2100);
        materialCardViews.add(materialCardView2140);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(textView1915);
        textViews.add(textView2015);
        textViews.add(textView2100);
        textViews.add(textView2140);

//        resetCardsPresencas(materialCardViews, textViews, 23, 7);

    }

    private void initBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.status);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.status:
//                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
//                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.aulas:
                        startActivity(new Intent(getApplicationContext(), AulasActivity.class));
                        overridePendingTransition(0, 0);
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

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.activity_status, container, false);
//
//
//        return view;
//    }

    private void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute) {
        final boolean[] presencaValidada = {false};
        new Thread() {
            @Override
            public void run() {
                while (!presencaValidada[0]) {
                    try {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                            presencaValidada[0] = true;

                            Presenca presenca = new Presenca(ID_SIMULATE_ACADEMICO, KEY_SIMULATE_BEACON, LocalDateTime.now().toString());
                            API.validarPresenca(presenca, new Callback<Presenca>() {
                                @Override
                                public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                                    if (response.body() != null){
//                                        textView.setText(response.body().getMensagemRetorno());
                                        if (response.body().getMensagemRetorno() != null){
                                            handler.post(new Runnable(){
                                                @Override
                                                public void run() {
                                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                                    textView.setText("Presença válidada!");
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Presenca> call, Throwable t) {
//                                    textView.setText("Erro ao validar sua presença, iremos realizar uma nova tentativa em um minuto.");
//                                    presencaValidada[0] = false;
                                    System.out.println("DEU RUIM CARA AF");
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void resetCardsPresencas(final List<MaterialCardView> materialCardViews, final List<TextView> textViews, final Integer hour, final Integer minute){
        final boolean[] resetRealizado = {false};

        new Thread(){
            @Override
            public void run() {
                try {
                    while (!resetRealizado[0]) {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()) {
                            resetRealizado[0] = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (MaterialCardView materialCardView : materialCardViews) {
                                        materialCardView.setBackgroundColor(Color.parseColor("#E7731E"));
                                    }

                                    for (TextView textView : textViews) {
                                        textView.setText("Aguardando Horário");
                                    }
                                }
                            });

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
