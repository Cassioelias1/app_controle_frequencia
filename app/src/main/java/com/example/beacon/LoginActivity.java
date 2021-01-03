package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButtons();
    }

    private void initButtons(){
        Button buttonAuthLogin = findViewById(R.id.buttonAuthLogin);
        buttonAuthLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthAcademico();
            }
        });
    }

    private void showTextNotCredencials(){
        Toast.makeText(this, "Não encontramos um usuário com essas credenciais.", Toast.LENGTH_LONG).show();
    }

    private void onAuthAcademico() {
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editPassword);
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        redirectToCentralApp();
        /*if (!Util.isNullOrEmpty(email) && !Util.isNullOrEmpty(senha)) {
            API.validarLogin(new Callback<List<Academico>>() {
                @Override
                public void onResponse(Call<List<Academico>> call, Response<List<Academico>> response) {
                    if (!response.body().isEmpty()){
                        Academico academico = response.body().get(0);

                        if (academico != null) {
                            AppContext.setAcademicoId(academico.getId());
                            redirectToCentralApp();
                        }
                    } else {
                        showTextNotCredencials();
                    }
                }

                @Override
                public void onFailure(Call<List<Academico>> call, Throwable t) {
                    //Não faz nada
                }
            }, email, senha);

        } else {
            Toast.makeText(this, "Não foi informado o E-mail ou a Senha!", Toast.LENGTH_LONG).show();
        }*/
    }

    private void redirectToCentralApp() {
        Intent itImc = new Intent(LoginActivity.this, RequestPermissionActivity.class);
        startActivity(itImc);
    }
}
