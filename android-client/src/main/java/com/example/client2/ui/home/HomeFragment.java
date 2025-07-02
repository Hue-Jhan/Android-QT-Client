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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ServerConnection serverConnection;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
