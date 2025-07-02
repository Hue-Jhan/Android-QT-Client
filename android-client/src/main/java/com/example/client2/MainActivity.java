package com.example.client2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.client2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static ServerConnection serverConnection;
    public static void setServerConnection(ServerConnection conn) {
        serverConnection = conn;
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    //public static boolean isConnectedToServer() {
    //    return serverConnection != null && serverConnection.isConnected();
    //}
    private static boolean isConnected = false;

    public static boolean isConnectedToServer() {
        return isConnected;
    }

    public static void setConnected(boolean value) {
        isConnected = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_database, R.id.navigation_file)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}