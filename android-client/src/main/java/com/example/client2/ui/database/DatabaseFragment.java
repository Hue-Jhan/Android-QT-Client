package com.example.client2.ui.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client2.MainActivity;
import com.example.client2.databinding.FragmentDatabaseBinding;
import com.example.client2.ui.ServerException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DatabaseFragment extends Fragment {

    private FragmentDatabaseBinding binding;
    private boolean tabellaCaricata = false;
    private boolean clusterCalcolati = false;
    private String nomeTabellaCaricata = "";
    private String fileName = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (!MainActivity.isConnectedToServer()) {
            Toast.makeText(getContext(), "Connettiti prima al server", Toast.LENGTH_SHORT).show();
            return new View(requireContext()); // ritorna una View vuota per bloccare il fragment
        }


        DatabaseViewModel databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        binding = FragmentDatabaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //binding.editTextRadius.setEnabled(false);
        //binding.buttonSendRadius.setEnabled(false);
        binding.buttonLoadTable.setOnClickListener(v -> {
            String tableName = binding.editTextTableName.getText().toString().trim();

            if (tableName.isEmpty()) {
                Toast.makeText(getContext(), "Inserisci il nome della tabella", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    ObjectOutputStream out = MainActivity.getServerConnection().getOutputStream();
                    ObjectInputStream in = MainActivity.getServerConnection().getInputStream();
                    out.writeObject(0); // comando: nome tabella
                    out.writeObject(tableName);
                    out.flush();
                    String response = (String) in.readObject();

                    if (!"OK".equals(response)) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Tabella inesistente", Toast.LENGTH_SHORT).show()
                        );
                        throw new ServerException(response);
                    } else {
                        String tableData = (String) in.readObject();  // Leggo i dati della tabella
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Tabella caricata con successo", Toast.LENGTH_SHORT).show();
                            binding.editTextRadius.setEnabled(true);
                            binding.editTextRadius.setVisibility(View.VISIBLE);
                            binding.buttonSendRadius.setEnabled(true);
                            binding.textViewResult.setText("Tabella caricata: " + tableName + "\n\n" + tableData);
                            tabellaCaricata = true;
                            nomeTabellaCaricata = tableName;
                        });
                    }

                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        // Pulsante per inviare il raggio e ottenere i cluster
        binding.buttonSendRadius.setOnClickListener(v -> {
            if (!tabellaCaricata) {
                Toast.makeText(getContext(), "Carica prima una tabella", Toast.LENGTH_SHORT).show();
                return;
            }

            String radiusStr = binding.editTextRadius.getText().toString().trim();
            double radius;

            try {
                radius = Double.parseDouble(radiusStr);
                if (radius <= 0) {
                    Toast.makeText(getContext(), "Il raggio deve essere > 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Raggio non valido", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    ObjectOutputStream out = MainActivity.getServerConnection().getOutputStream();
                    ObjectInputStream in = MainActivity.getServerConnection().getInputStream();
                    out.writeObject(1); // comando: clustering
                    out.writeObject(radius);
                    out.flush();
                    String response = (String) in.readObject();

                    if (!"OK".equals(response)) {
                        throw new Exception(response);
                    } else {
                        Integer numClusters = (Integer) in.readObject();
                        String clustersText = (String) in.readObject();
                        requireActivity().runOnUiThread(() -> { // print a schermo dei cluster
                            binding.textViewResult.setText("Cluster trovati: " + numClusters + "\n" + clustersText);
                        });
                        clusterCalcolati = true;
                        fileName = nomeTabellaCaricata+radiusStr+".dmp";

                    }

                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });

        binding.buttonSaveCLuster.setOnClickListener(v -> {
            if (!clusterCalcolati) {
                Toast.makeText(getContext(), "Cluster non ancora calcolati!", Toast.LENGTH_SHORT).show();
                return;
            }

            String tempfileName = binding.editTextFileName.getText().toString().trim();
            if (tempfileName.isEmpty()) {
                tempfileName = fileName;
            }

            String finalfileName = tempfileName;
            new Thread(() -> {
                try {
                    ObjectOutputStream out = MainActivity.getServerConnection().getOutputStream();
                    ObjectInputStream in = MainActivity.getServerConnection().getInputStream();
                    out.writeObject(2); // comando: salva cluster in file
                    out.writeObject(finalfileName);
                    out.flush();
                    String response = (String) in.readObject();

                    if (!"OK".equals(response)) {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Errore nel salvataggio", Toast.LENGTH_SHORT).show());
                        throw new ServerException(response);
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Cluster salvati in " + finalfileName, Toast.LENGTH_SHORT).show());
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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}