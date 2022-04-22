package br.com.adaca.adacalite;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import br.com.adaca.adacalite.fragments.HomeFragment;
import br.com.adaca.adacalite.helper.Permissoes;
import br.com.adaca.adacalite.service.AdacaAccountsManager;
import com.google.android.material.navigation.NavigationView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final HomeFragment homeFragment = HomeFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addNavigationHeader(navigationView.getHeaderView(0));

        if (getSupportFragmentManager().findFragmentByTag("homeFragment") == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main_frame_layout, homeFragment, "homeFragment");
            fragmentTransaction.commit();
        }

        //validar permissões
        if (Build.VERSION.SDK_INT >= 23) {
            Permissoes.validarPermissoes(this).launch(new String[]{READ_CONTACTS});
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        homeFragment.onNavigation("logout");
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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

    private void addNavigationHeader(@NonNull View navigationHeader) {
        TextView textName = navigationHeader.findViewById(R.id.name_user_login);
        TextView textType = navigationHeader.findViewById(R.id.email_user_login);
        ImageView imageView = navigationHeader.findViewById(R.id.imageView);

        String[] profile = AdacaAccountsManager.readUserProfile(getBaseContext());
        textName.setText((profile[0] != null) ? profile[0] : "Adaca");
        textType.setText(AdacaAccountsManager.getUserName(getBaseContext()));
        if (profile[1] != null) {
            imageView.setImageURI(Uri.parse(profile[1]));
        }else{
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_reload){
            homeFragment.reload();
        }
        return super.onOptionsItemSelected(item);
    }
}
