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

public class FileFragment extends Fragment {

    private FragmentFileBinding binding;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}