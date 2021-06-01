package br.com.adaca.adacalite;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import br.com.adaca.adacalite.fragments.HomeFragment;
import br.com.adaca.adacalite.service.AdacaAccountsManager;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SETTINGS_ACTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addNavigationHeader(navigationView.getHeaderView(0));

        HomeFragment homeFragment = HomeFragment.newInstance();
        if (getSupportFragmentManager().findFragmentByTag("homeFragment") == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_frame_layout, homeFragment, "homeFragment");
            fragmentTransaction.commit();
        }

        mayRequestReadContacts();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        if(homeFragment == null)
            homeFragment = HomeFragment.newInstance();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(homeFragment.isGoBack()) {
            homeFragment.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        if(homeFragment == null)
            homeFragment = HomeFragment.newInstance();

        homeFragment.onNavigation("logout");
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        if(homeFragment == null)
            homeFragment = HomeFragment.newInstance();

        if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_ACTION);
        }
        else if(id == R.id.nav_home){
            homeFragment.onNavigation("painel");
        }
        else if(id == R.id.nav_select){
            homeFragment.onNavigation("selecionarCrianca");
        }
        else if(id == R.id.nav_cadastro) {
            homeFragment.onNavigation("Autistas/list");
        }
        else if(id == R.id.nav_settings){
            homeFragment.onNavigation("Configuracoes/list");
        }
        else if(id == R.id.nav_resultados){
           homeFragment.onNavigation("Resultados/list");
        }
        else if(id == R.id.nav_session){
            homeFragment.onNavigation("Sessoes/list");
        }
        else if(id == R.id.nav_exit){
            homeFragment.onNavigation("logout");
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!mayRequestReadContacts()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.no_permission_read_contacts_alert)
                            .setNeutralButton("OK", (dialogInterface, i) -> System.exit(1))
                            .show();
                }
            }
        }
    }

    private void addNavigationHeader(@NonNull View navigationHeader) {
        TextView textName = navigationHeader.findViewById(R.id.name_user_login);
        TextView textType = navigationHeader.findViewById(R.id.email_user_login);
        ImageView imageView = navigationHeader.findViewById(R.id.imageView);

        String[] profile = AdacaAccountsManager.readUserProfile(getBaseContext());
        textName.setText((profile[0] != null) ? profile[0] : "Adaca");
        textType.setText(AdacaAccountsManager.getUserName(getBaseContext()));
        try {
            imageView.setImageURI(Uri.parse(profile[1]));
        } catch (Exception e) {
            Log.e("adaca::MainActivity", e.getMessage(), e);
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    private boolean mayRequestReadContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{READ_CONTACTS}, 1);
        return false;
    }
}
