package com.faculdade.tempestado.model;

import com.google.gson.annotations.SerializedName;


public class PrevisaoResponse {


    @SerializedName("results")
    private ResultsData results;

    public ResultsData getResults() {
        return results;
    }

    public void setResults(ResultsData results) {
        this.results = results;
    }
}