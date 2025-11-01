package com.faculdade.tempestado.model;

import com.google.gson.annotations.SerializedName;


public class PrevisaoDiaria {

    @SerializedName("date")
    private String data;

    @SerializedName("weekday")
    private String diaDaSemana;

    @SerializedName("max")
    private int temperaturaMaxima;

    @SerializedName("min")
    private int temperaturaMinima;

    @SerializedName("description")
    private String descricao;



    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public int getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(int temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public int getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(int temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}