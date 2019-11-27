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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Class for the appropriate selection and process of each heuristic algorithm.
 */
public class SearchAlgorithms extends AppCompatActivity {
    Intent intent;
    Bundle bundle;

    /**
     * Getting the initial variables available for the run of the activity, like the
     * bundle and the intent.
     * @param savedInstanceState Strings bundle with the most important information
     *                           from the last activity.
     */
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

    /**
     * Method called by user on back button pressed.
     * @param item activity item related to the context.
     * @return a true value.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //getSupportFragmentManager().getPrimaryNavigationFragment().onStop();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments.get(0).onStop();
        super.onBackPressed();
        return true;//super.onOptionsItemSelected(item);
    }

    /**
     * Method called by user on back button pressed.
     */
    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        fragments.get(0).onStop();
        super.onBackPressed();
    }

    /**
     * Implementation of the fragments for each heuristic algorithm.
     */
    private class NavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        /**
         * Depending of the heuristic algoritm to show, we create and load the corresponding fragment.
         * @param menuItem
         * @return
         */
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
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            fragments.get(0).onStop();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    }
}
