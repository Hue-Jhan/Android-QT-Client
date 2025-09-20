package com.example.client2.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.client2.MainActivity;
import com.example.client2.R;
import com.example.client2.ServerConnection;
import com.example.client2.databinding.FragmentHomeBinding;

/**
 * {@code HomeFragment} è il fragment principale dell'applicazione che consente
 * all'utente di inserire l'indirizzo IP e la porta del server per stabilire una
 * connessione tramite {@link ServerConnection}.
 * <p>
 * Fornisce un'interfaccia semplice con campi di input e un pulsante di connessione.
 * Una volta stabilita la connessione viene aggiorna l'interfaccia e notifica
 * {@link MainActivity} per rendere disponibile la connessione alle altre sezioni
 * dell'app.
 * </p>
 */
public class HomeFragment extends Fragment {

    /**
     * Oggetto generato automaticamente tramite ViewBinding per accedere
     * ai componenti UI del fragment.
     */
    private FragmentHomeBinding binding;

    /**
     * Riferimento alla connessione con il server,
     * gestita da {@link ServerConnection}.
     */
    private ServerConnection serverConnection;

    /**
     * Metodo chiamato per creare e inizializzare la UI del fragment.
     * <p>
     * Qui vengono recuperati i riferimenti ai campi di input (IP e porta) e
     * viene configurato il pulsante di connessione. Alla pressione del pulsante,
     * viene avviato un thread separato per tentare la connessione al server.
     * </p>
     *
     * @param inflater  il {@link LayoutInflater} usato per gonfiare la vista
     * @param container il contenitore padre in cui la vista verrà inserita
     * @param savedInstanceState eventuale stato salvato del fragment
     * @return la {@link View} radice del fragment
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText editTextIp = root.findViewById(R.id.editTextIp);
        EditText editTextPort = root.findViewById(R.id.editTextPort);
        Button buttonConnect = root.findViewById(R.id.buttonConnect);
        TextView textHome = root.findViewById(R.id.text_home);

        buttonConnect.setOnClickListener(v -> {
            String ip = editTextIp.getText().toString().trim();
            String portStr = editTextPort.getText().toString().trim();

            if (ip.isEmpty() || portStr.isEmpty()) {
                Toast.makeText(getContext(), "Inserisci IP e porta", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int port = Integer.parseInt(portStr);

                new Thread(() -> {
                    serverConnection = new ServerConnection();
                    MainActivity.setServerConnection(serverConnection); // porto il socket in main per usi futuri
                    Log.d("ServerDebug", "IP: " + ip + ", Port: " + port);
                    boolean connected = serverConnection.connectToServer(ip, port);

                    requireActivity().runOnUiThread(() -> {
                        if (connected) {
                            textHome.setText("Connessione riuscita a " + ip + ":" + port);
                            MainActivity.setConnected(true);
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Connesso al server", Toast.LENGTH_SHORT).show()
                            );
                        } else {
                            textHome.setText("Connessione fallita. Controlla IP/porta.");
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Connessione fallita", Toast.LENGTH_SHORT).show()
                            );
                        }
                    });
                }).start();

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Porta non valida", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    /**
     * Metodo chiamato quando la vista del fragment viene distrutta.
     * <p>
     * Qui viene liberata la risorsa {@link FragmentHomeBinding} per evitare memory leaks.
     * </p>
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Restituisce la connessione al server creata da questo fragment.
     *
     * @return un'istanza di {@link ServerConnection}, oppure {@code null}
     *         se la connessione non è stata ancora stabilita
     */
    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
