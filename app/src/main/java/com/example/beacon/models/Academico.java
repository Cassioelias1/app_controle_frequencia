package com.example.beacon.models;

public class Academico {

    private String nome;
    private String codigo;
    private String senha;

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
}
