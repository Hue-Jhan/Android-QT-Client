package com.example.client2.ui;

import java.io.Serializable;

/**
 * Eccezione personalizzata per la gestione degli errori relativi al server.
 * <p>
 * Questa classe estende {@link Exception} ed è utilizzata per rappresentare
 * situazioni di errore specifiche durante la comunicazione con il server
 * o la gestione della connessione.
 * </p>
 *
 * <p>
 * Implementa inoltre {@link Serializable} per consentire la serializzazione
 * dell'oggetto in caso di necessità di trasmissione o memorizzazione.
 * </p>
 */
public class ServerException extends Exception implements Serializable {

    /**
     * Identificatore univoco per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore che crea una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell'errore.
     */
    public ServerException(String message) {
        super(message);
    }
}
