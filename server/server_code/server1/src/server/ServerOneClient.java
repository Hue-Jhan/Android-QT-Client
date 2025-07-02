package server;

import data.Data;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.ClusteringRadiusException;
import mining.EmptyDatasetException;
import mining.QTMiner;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QTMiner kmeans;
    private String tableName;

    public ServerOneClient(Socket s) throws IOException {
        this.socket = s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
        this.start(); // avvia il thread
    }

    @Override
    public void run() {
        System.out.println("Client Connesso");
        try {
            while (true) {
                Object request = in.readObject();

                if (request instanceof Integer) {
                    int command = (Integer) request;

                    switch (command) {
                        case 0:
                            handleStoreTableFromDb();
                            break;
                        case 1:
                            handleLearningFromDbTable();
                            break;
                        case 2:
                            handleStoreClusterInFile();
                            break;
                        case 3:
                            handleLearningFromFile();
                            break;
                        default:
                            out.writeObject("Comando non valido.");
                            out.flush();
                    }
                } else {
                    out.writeObject("Richiesta non valida.");
                    out.flush();
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnesso.");
        } catch (Exception e) {
            try {
                out.writeObject("Errore: " + e.getMessage());
                out.flush();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del socket.");
            }
        }
    }

    private void handleStoreTableFromDb() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (obj instanceof String) {
            tableName = (String) obj; // salva il nome della tabella
            out.writeObject("OK");
            try {
                Data data = new Data<>(tableName);
                out.writeObject(data.toString());  // invia la stringa dei dati
            } catch (Exception e) {
                out.writeObject("Errore durante il caricamento dati: " + e.getMessage());
            }

        } else {
            out.writeObject("Errore: nome tabella non valido.");
        }
        out.flush();
    }


    private void handleLearningFromDbTable() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (!(obj instanceof Double)) {
            out.writeObject("Errore: raggio non valido.");
            out.flush();
            return;
        }
        double radius = (Double) obj;

        if (tableName == null) {
            out.writeObject("Errore: nessuna tabella specificata.");
            out.flush();
            return;
        }

        try {
            Data data = new Data<>(tableName);
            System.out.println("Dati caricati");
            // out.writeObject("DATI");
            // out.writeObject(data.toString());

            this.kmeans = new QTMiner(radius);
            int numIter = kmeans.compute(data);

            out.writeObject("OK");
            out.writeObject(numIter);
            out.writeObject(kmeans.getC().toString(data));

        } catch (EmptyDatasetException | ClusteringRadiusException | IOException e) {
            out.writeObject("Errore clustering: " + e.getMessage());
        } catch (DatabaseConnectionException | SQLException | EmptySetException | NoValueException e) {
            out.writeObject("Errore durante il caricamento dei dati: " + e.getMessage());
        }
        out.flush();
    }

    private void handleStoreClusterInFile() throws IOException, ClassNotFoundException {
        Object obj = in.readObject(); // Leggi il nome file dal client
        System.out.println("Tabella ricevuta: '" + tableName + "'");

        if (!(obj instanceof String)) {
            out.writeObject("Errore: nome file non valido.");
            out.flush();
            return;
        }
        String filename = (String) obj;

        if (kmeans == null) {
            out.writeObject("Errore: nessun cluster da salvare.");
            out.flush();
            return;
        }

        try {
            kmeans.salva(filename);
            out.writeObject("OK");
        } catch (IOException e) {
            out.writeObject("Errore salvataggio cluster: " + e.getMessage());
        }
        out.flush();
    }

    private void handleLearningFromFile() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        System.out.println("Avvio clustering con tabella: '" + tableName + "'");

        if (!(obj instanceof String)) {
            out.writeObject("Errore: nome file non valido.");
            out.flush();
            return;
        }
        String filename = (String) obj;

        try {
            kmeans = new QTMiner(filename);
            if (kmeans.getC() != null) {
                out.writeObject("OK");
                out.writeObject(kmeans.getC().toString());
            } else {
                out.writeObject("Attenzione: dati non caricati, impossibile mostrare i cluster.");
            }
        } catch (FileNotFoundException e) {
            out.writeObject("Errore: file cluster non trovato.");
        } catch (IOException | ClassNotFoundException e) {
            out.writeObject("Errore caricamento cluster: " + e.getMessage());
        }
        out.flush();
    }
}