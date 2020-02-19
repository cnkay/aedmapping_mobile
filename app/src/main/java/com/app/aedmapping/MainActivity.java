package com.app.aedmapping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.aedmapping.Fragments.CPRFragment;
import com.app.aedmapping.Fragments.MapFragment;
import com.app.aedmapping.Fragments.RespondersFragment;
import com.app.aedmapping.Retrofit.APIManager;
import com.app.aedmapping.Retrofit.Defibrillator.Defibrillator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FloatingActionButton.OnClickListener {
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private List<Defibrillator> findAllListDefib = new ArrayList<>();
    private APIManager apiManager;
    FloatingActionButton actionButton;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ButterKnife.bind(this);
        if (checkLoginCredentials()) {
            configureComponents();
            loadFragment(new MapFragment());

        }
      /*  else
            createSignInIntent();*/
    }

    private void configureComponents() {
        actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public List<Defibrillator> getFindAllListDefib() {
        return findAllListDefib;
    }

    private boolean checkLoginCredentials() {
        if (pref.getString("personName", null) != null && pref.getString("personEmail", null) != null) {
            return true;
        }
        if (pref.getString("normalLogin", null) != null) {
            return true;
        }
        return false;
    }

    private void createSignInIntent() {
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_hands_holding_heart:
                fragment = new RespondersFragment();
                break;
            case R.id.navigation_cpr:
                fragment = new CPRFragment();
                break;
            case R.id.navigation_home:
                fragment = new MapFragment();
                break;
            case R.id.navigation_emergency:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:911")); // Dial up 911
                startActivity(intent);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void findAllDefibs() {
        apiManager = new APIManager(Constant.YOUR_PHP_WEBSITE_LINK);
        apiManager.getServices().findAllDefibrillators().enqueue(new Callback<List<Defibrillator>>() {
            @Override
            public void onResponse(Call<List<Defibrillator>> call, Response<List<Defibrillator>> response) {
                if (response.body() != null) {
                    findAllListDefib.addAll(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "findAllDefibs returned null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Defibrillator>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == actionButton.getId()) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("floatClicked", "1");
            editor.commit();
            loadFragment(new MapFragment());
        }
    }
}
