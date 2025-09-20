package com.example.client2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.client2.databinding.ActivityMainBinding;

/**
 * Classe principale dell'applicazione che estende {@link AppCompatActivity}.
 * <p>
 * Questa activity gestisce l'interfaccia utente principale dell'applicazione.
 * </p>
 *
 * <p>
 * La classe include anche metodi statici per gestire lo stato della connessione
 * con un server remoto tramite {@link ServerConnection}.
 * </p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Binding generato automaticamente da Android ViewBinding
     * per accedere in modo tipizzato agli elementi del layout.
     */
    private ActivityMainBinding binding;

    /**
     * Connessione al server condivisa a livello di applicazione.
     */
    private static ServerConnection serverConnection;

    /**
     * Imposta la connessione al server.
     *
     * @param conn istanza di {@link ServerConnection} da associare.
     */
    public static void setServerConnection(ServerConnection conn) {
        serverConnection = conn;
    }

    /**
     * Restituisce la connessione al server attualmente impostata.
     *
     * @return l'istanza di {@link ServerConnection}, oppure {@code null} se non presente.
     */
    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    private static boolean isConnected = false;

    /**
     * Verifica se l'applicazione è connessa al server.
     */
    public static boolean isConnectedToServer() {
        return isConnected;
    }

    /**
     * Aggiorna lo stato della connessione con il server.
     *
     * @param value {@code true} se connessa, {@code false} se disconnessa.
     */
    public static void setConnected(boolean value) {
        isConnected = value;
    }

    /**
     *
     * Metodo che inizializza il binding del layout, imposta la vista principale
     * e configura la navigazione con {@link NavController} e {@link BottomNavigationView}.
     *
     * @param savedInstanceState stato precedente dell'activity, se disponibile.
     */
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
