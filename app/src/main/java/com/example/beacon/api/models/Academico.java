package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Academico implements Serializable {
    @SerializedName("nome")
    private String nome;

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("senha")
    private String senha;

    @SerializedName("email")
    private String email;

    public Academico() {
    }

    public Academico(String nome, String codigo, String senha) {
        this.nome = nome;
        this.codigo = codigo;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
