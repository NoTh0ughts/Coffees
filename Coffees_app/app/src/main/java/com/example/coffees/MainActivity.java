package com.example.coffees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    Toolbar custom_toolbar;

    ConstraintLayout main_constraintLayout;
    ConstraintSet constraintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custom_toolbar = findViewById(R.id.custom_toolbar_id);
        main_constraintLayout = findViewById(R.id.main_constraintLayout);
        setToolbar("Coffees",null);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_menuFragment, R.id.nav_favoritesFragment, R.id.nav_profileFragment)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //navController.navigate(R.id.nav_menuFragment); //КОСТЫЛИЩЕ
    }

    public void setToolbar(String title, Drawable navicon){
        if (custom_toolbar != null){
            AppCompatActivity actionBar = this;
            actionBar.setSupportActionBar(custom_toolbar);
            custom_toolbar.setBackgroundColor(Color.parseColor("#1f3933"));
            custom_toolbar.setTitle(title);
            custom_toolbar.setNavigationIcon(navicon);

            //setDefaultToolbar();
        }
    }

    public Toolbar getToolbar(){
        return custom_toolbar;
    }
}