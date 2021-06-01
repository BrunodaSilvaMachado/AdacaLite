package br.com.adaca.adacalite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hide();
        Handler handler = new Handler();

        Runnable entry = () -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        };

        Runnable exit = () -> new AlertDialog.Builder(SplashActivity.this)
                .setTitle(R.string.alert)
                .setMessage(R.string.no_connectivity_service)
                .setNeutralButton("OK", (dialogInterface, i) -> System.exit(1))
                .show();

        if (isOnline()){
            handler.postDelayed(entry, 1000);
        }
        else{
            handler.postDelayed(exit, 2000);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hide();
    }

    @SuppressLint("InlinedApi")
    private void hide(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }catch (Exception e) {
            Toast.makeText(SplashActivity.this,R.string.no_connectivity_service,Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }
}
