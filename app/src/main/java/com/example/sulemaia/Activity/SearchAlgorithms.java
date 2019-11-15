package com.example.sulemaia.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sulemaia.Fragment.AStar;
import com.example.sulemaia.Fragment.FirstBest;
import com.example.sulemaia.Fragment.UniformCost;
import com.example.sulemaia.Model.CharacterItem;
import com.example.sulemaia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SearchAlgorithms extends AppCompatActivity {
    Intent intent;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_algorithms);
        intent = getIntent();
        bundle = intent.getExtras();

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        NavigationListener navListener = new NavigationListener();
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Fragment selectedFragment = new UniformCost();
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    private class NavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_uniform_cost:
                    selectedFragment = new UniformCost();
                    selectedFragment.setArguments(intent.getExtras());
                    break;
                case R.id.nav_first_the_best:
                    selectedFragment = new FirstBest();
                    selectedFragment.setArguments(intent.getExtras());
                    break;
                case R.id.nav_a_star:
                    selectedFragment = new AStar();
                    selectedFragment.setArguments(intent.getExtras());
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    }
}
