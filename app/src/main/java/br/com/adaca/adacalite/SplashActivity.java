package br.com.adaca.adacalite;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());

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
            handler.postDelayed(entry, 1500);
        }
        else{
            handler.postDelayed(exit, 2750);
        }
    }
    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }catch (Exception e) {
            Toast.makeText(SplashActivity.this,R.string.no_connectivity_service,Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }
}
