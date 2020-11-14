package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Turma implements Serializable {
    @SerializedName("id")
    private Integer id;//Implementar herança

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("periodo")
    private String periodo;

    public Turma() {
    }

    public Turma(Integer id, String descricao, String periodo) {
        this.id = id;
        this.descricao = descricao;
        this.periodo = periodo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
