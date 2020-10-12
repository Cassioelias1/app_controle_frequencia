package com.example.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.models.Academico;
import com.example.beacon.services.AcademicoService;
import com.example.beacon.utils.Util;

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
//                onAuthAcademico();
                redirectToCentralApp();
            }
        });
    }

    private void onAuthAcademico() {
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editPassword);
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        if (!Util.isNullOrEmpty(email) && !Util.isNullOrEmpty(senha)) {

            Academico academico = AcademicoService.Instance().findByCodigoAndSenha(email, senha);
            if (academico != null) {
                redirectToCentralApp();
            } else {
                Toast.makeText(this, "As credenciais não são válidas!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Não foi informado o E-mail ou a Senha!", Toast.LENGTH_LONG).show();
        }
    }

    private void redirectToCentralApp() {
        Intent itImc = new Intent(LoginActivity.this, CentralActivity.class);
        startActivity(itImc);
    }
}
