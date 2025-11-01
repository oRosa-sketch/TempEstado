package com.faculdade.tempestado.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.faculdade.tempestado.R;
import com.faculdade.tempestado.adapter.ViewPagerAdapter;
import com.faculdade.tempestado.fragment.AtualizavelFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton fabQrCode;

    private final int REQUEST_PERMISSIONS_CODE = 1;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(),
            (ScanIntentResult result) -> {
                if(result.getContents() == null) { // MUDADO DE .Contents() PARA .getContents()
                    Toast.makeText(this, "Escaneamento cancelado", Toast.LENGTH_LONG).show();
                } else {
                    processarResultadoQrCode(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        tabLayout = findViewById(R.id.tabLayoutMain);
        viewPager = findViewById(R.id.viewPagerMain);
        fabQrCode = findViewById(R.id.fabQrCode);

        setSupportActionBar(toolbar);
        configurarAbas();
        solicitarPermissoes();

        fabQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarScanner();
            }
        });
    }

    private void iniciarScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Aponte para o QR Code da cidade");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(false);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    private void processarResultadoQrCode(String conteudo) {
        try {
            String[] partes = conteudo.split(",");

            if (partes.length == 4) {
                String cidadeParaAPI = partes[0] + "," + partes[1];
                double latitude = Double.parseDouble(partes[2]);
                double longitude = Double.parseDouble(partes[3]);

                notificarFragments(cidadeParaAPI, latitude, longitude);
            } else {
                Toast.makeText(this, "QR Code inválido. Formato: Cidade,UF,latitude,longitude", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao processar QR Code.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Erro processarResultadoQrCode", e);
        }
    }

    private void notificarFragments(String cidade, double latitude, double longitude) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof AtualizavelFragment) {
                ((AtualizavelFragment) fragment).atualizarCidade(cidade, latitude, longitude);
            }
        }
    }

    private void configurarAbas() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("Previsão");
                } else {
                    tab.setText("Mapa");
                }
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sobre) {
            Intent intent = new Intent(MainActivity.this, SobreActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void solicitarPermissoes() {
        String[] permissoes = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        ArrayList<String> permissoesNegadas = new ArrayList<>();

        for (String permissao : permissoes) {
            if (ContextCompat.checkSelfPermission(this, permissao) != PackageManager.PERMISSION_GRANTED) {
                permissoesNegadas.add(permissao);
            }
        }

        if (!permissoesNegadas.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissoesNegadas.toArray(new String[0]),
                    REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}