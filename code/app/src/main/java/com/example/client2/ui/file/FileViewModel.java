package com.example.client2.ui.file;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * {@code FileViewModel} è una classe che estende {@link ViewModel} e fornisce i dati
 * osservabili relativi al fragment "File".
 * <p>
 * Utilizza {@link MutableLiveData} per mantenere e aggiornare il testo mostrato
 * nell'interfaccia utente. In questo caso, viene inizializzato con un valore
 * predefinito che indica la sezione delle notifiche.
 * </p>
 */
public class FileViewModel extends ViewModel {

    /**
     * Dato osservabile che contiene il testo da mostrare nell'interfaccia.
     */
    private final MutableLiveData<String> mText;

    /**
     * Costruttore della classe.
     * <p>
     * Inizializza il {@link MutableLiveData} con un valore predefinito.
     * <p>
     */
    public FileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    /**
     * Metodo che restituisce il testo osservabile che può essere monitorato dal fragment.
     *
     * @return un {@link LiveData} contenente il testo attuale
     */
    public LiveData<String> getText() {
        return mText;
    }
}
