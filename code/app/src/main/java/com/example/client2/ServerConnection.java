package com.example.client2;

import java.io.*;
import java.net.*;

/**
 * Gestisce la connessione tra il client e un server remoto tramite socket TCP.
 * <p>
 * Questa classe fornisce metodi per stabilire la connessione, verificare lo stato,
 * ottenere i flussi di input/output e chiudere correttamente la comunicazione.
 * </p>
 *
 * <p>
 * L'implementazione utilizza {@link Socket}, {@link ObjectInputStream} e
 * {@link ObjectOutputStream} per consentire la trasmissione di oggetti
 * tra client e server.
 * </p>
 */
public class ServerConnection {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    /**
     * Tenta di connettersi a un server remoto tramite indirizzo IP e porta.
     *
     * @param ip   indirizzo IP del server (ad esempio "127.0.0.1").
     * @param port numero di porta su cui è in ascolto il server.
     * @return {@code true} se la connessione è avvenuta con successo,
     *         {@code false} in caso di eventuali errori sopraggiunti.
     */
    public boolean connectToServer(String ip, int port) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            socket = new Socket(addr, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se il client è attualmente connesso al server.
     *
     * @return {@code true} se il socket è valido, connesso e non chiuso,
     *         {@code false} altrimenti.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Restituisce il flusso di output verso il server.
     *
     * @return istanza di {@link ObjectOutputStream}, oppure {@code null} se non connesso.
     */
    public ObjectOutputStream getOutputStream() {
        return out;
    }

    /**
     * Restituisce il flusso di input dal server.
     *
     * @return istanza di {@link ObjectInputStream}, oppure {@code null} se non connesso.
     */
    public ObjectInputStream getInputStream() {
        return in;
    }

    /**
     * Chiude la connessione al server e rilascia le risorse associate
     * (flussi e socket).
     */
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
