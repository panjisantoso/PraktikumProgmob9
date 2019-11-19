package com.example.progmobkelompok9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.progmobkelompok9.util.StringFixed;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN,MODE_PRIVATE);
        session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_mybook:
                            if (session){
                                selectedFragment = new MyDocumentFragment();
                            }
                            else{
                                selectedFragment = new NotLoginFragment();
                            }
                            break;
                        case R.id.nav_profile:
                            if (session){
                                selectedFragment = new ProfileFragment();
                            }
                            else{
                                selectedFragment = new NotLoginFragment();
                            }
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
