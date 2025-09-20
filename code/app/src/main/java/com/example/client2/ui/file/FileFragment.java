package com.example.client2.ui.file;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client2.MainActivity;
import com.example.client2.databinding.FragmentFileBinding;
import com.example.client2.ui.ServerException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Fragment per la gestione del caricamento dei cluster da file.
 * <p>
 * Questo fragment consente all'utente di specificare un file precedentemente salvato
 * (contenente cluster calcolati lato server) e di visualizzarne i dati.
 * </p>
 *
 * <p>
 * La comunicazione con il server avviene tramite {@link ObjectOutputStream} e
 * {@link ObjectInputStream}, ottenuti da {@link com.example.client2.ServerConnection}
 * fornito da {@link MainActivity}.
 * </p>
 *
 * <p>
 * Se l'app non è connessa a un server (verifica tramite
 * {@link MainActivity#isConnectedToServer()}), il fragment mostra un messaggio
 * di errore e non viene caricato.
 * </p>
 */
public class FileFragment extends Fragment {

    /**
     * Binding per accedere agli elementi del layout del fragment.
     */
    private FragmentFileBinding binding;

    /**
     * Inizializza la vista del fragment e imposta i listener per i pulsanti.
     *
     * @param inflater           inflater per creare la vista.
     * @param container          contenitore della vista.
     * @param savedInstanceState eventuale stato salvato.
     * @return la {@link View} radice del fragment, oppure una {@link View} vuota se non connesso al server.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (!MainActivity.isConnectedToServer()) {
            Toast.makeText(getContext(), "Connettiti prima al server", Toast.LENGTH_SHORT).show();
            return new View(requireContext()); // ritorna una View vuota per bloccare il fragment
        }

        FileViewModel fileViewModel = new ViewModelProvider(this).get(FileViewModel.class);
        binding = FragmentFileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonSendFile.setOnClickListener(v -> {
            String fileName = binding.editTextFileName.getText().toString().trim();

            if (fileName.isEmpty()) {
                Toast.makeText(getContext(), "Nome file mancante", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    ObjectOutputStream out = MainActivity.getServerConnection().getOutputStream();
                    ObjectInputStream in = MainActivity.getServerConnection().getInputStream();
                    out.writeObject(3); // comando: caricare cluster da file
                    out.writeObject(fileName);
                    out.flush();
                    String response = (String) in.readObject();

                    if (!"OK".equals(response)) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "File inesistente", Toast.LENGTH_SHORT).show()
                        );
                        throw new ServerException(response);
                    } else {
                        String fileData = (String) in.readObject();
                        requireActivity().runOnUiThread(() -> {
                            binding.textViewResult2.setText("Cluster caricati dal file: " + fileName + "\n\n" + fileData);
                        });
                    }

                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });


        return root;
    }

    /**
     * Metodo di lifecycle chiamato quando la vista del fragment viene distrutta.
     * <p>
     * Viene impostato {@code binding = null} per evitare memory leak.
     * </p>
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}