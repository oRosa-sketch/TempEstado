package com.faculdade.tempestado.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.faculdade.tempestado.R;

public class AberturaActivity extends AppCompatActivity {

    private static final int TEMPO_SPLASH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);
        iniciarTimerSplash();
    }

    private void iniciarTimerSplash() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AberturaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TEMPO_SPLASH);
    }
}