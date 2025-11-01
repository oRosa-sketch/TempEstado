package com.faculdade.tempestado.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faculdade.tempestado.R;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaFragment extends Fragment implements AtualizavelFragment {

    private MapView mapView;
    private GeoPoint cidadeCoordenadas = new GeoPoint(-23.5505, -46.6333); // São Paulo
    private String cidadeNome = "São Paulo";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapViewOSM);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        atualizarMapa();
    }

    private void atualizarMapa() {
        if (mapView == null) return;

        mapView.getController().setZoom(12.0);
        mapView.getController().setCenter(cidadeCoordenadas);

        mapView.getOverlays().clear();
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(cidadeCoordenadas);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(cidadeNome);
        mapView.getOverlays().add(startMarker);
        mapView.invalidate();
    }

    @Override
    public void atualizarCidade(String nome, double lat, double lon) {
        cidadeNome = nome;
        cidadeCoordenadas = new GeoPoint(lat, lon);
        if (mapView != null) {
            atualizarMapa();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}