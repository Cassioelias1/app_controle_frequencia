package com.example.beacon.services;

import com.example.beacon.models.Academico;

public class AcademicoService {
    public static AcademicoService Instance(){
        return new AcademicoService();
    }

    public Academico findByCodigoAndSenha(String codigo, String senha) {

        return new Academico("Nome Empty", codigo, senha);
    }
}
