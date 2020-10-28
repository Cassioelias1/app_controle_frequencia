package com.example.beacon.services;

import com.example.beacon.api.models.Academico;

public class AcademicoService {
    public static AcademicoService Instance(){
        return new AcademicoService();
    }

    public Academico findByCodigoAndSenha(String email, String senha) {
        return null;
    }
}
