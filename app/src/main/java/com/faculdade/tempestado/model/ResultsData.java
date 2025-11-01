package com.faculdade.tempestado.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResultsData {

    @SerializedName("forecast")
    private List<PrevisaoDiaria> forecast;

    public List<PrevisaoDiaria> getForecast() {
        return forecast;
    }

    public void setForecast(List<PrevisaoDiaria> forecast) {
        this.forecast = forecast;
    }
}