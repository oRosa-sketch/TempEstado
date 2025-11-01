package com.faculdade.tempestado.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faculdade.tempestado.network.HgBrasilApiService;
import com.faculdade.tempestado.adapter.PrevisaoAdapter;
import com.faculdade.tempestado.model.PrevisaoDiaria;
import com.faculdade.tempestado.model.PrevisaoResponse;
import com.faculdade.tempestado.R;
import com.faculdade.tempestado.model.ResultsData;
import com.faculdade.tempestado.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrevisaoFragment extends Fragment implements AtualizavelFragment {

    private RecyclerView recyclerView;
    private PrevisaoAdapter adapter;
    private List<PrevisaoDiaria> previsaoList;
    private ProgressBar progressBar;

    private static final String API_KEY = "84c71491";
    private String cidadeAtual = "Sao Paulo,SP";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_previsao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewPrevisao);
        progressBar = view.findViewById(R.id.progressBar);

        previsaoList = new ArrayList<>();
        adapter = new PrevisaoAdapter(getContext(), previsaoList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        buscarPrevisao(cidadeAtual);
    }

    public void buscarPrevisao(String cidade) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        HgBrasilApiService apiService = RetrofitClient.getApiService();
        Call<PrevisaoResponse> call = apiService.getPrevisao(API_KEY, cidade);

        call.enqueue(new Callback<PrevisaoResponse>() {
            @Override
            public void onResponse(Call<PrevisaoResponse> call, Response<PrevisaoResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ResultsData results = response.body().getResults();

                    if (results != null && results.getForecast() != null) {
                        previsaoList.clear();
                        previsaoList.addAll(results.getForecast());
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Nenhum resultado encontrado para esta cidade.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Falha ao buscar dados. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PrevisaoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("PrevisaoFragment", "Erro na chamada da API", t);
                Toast.makeText(getContext(), "Erro de rede: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void atualizarCidade(String cidade, double latitude, double longitude) {
        cidadeAtual = cidade;
        buscarPrevisao(cidadeAtual);
    }
}