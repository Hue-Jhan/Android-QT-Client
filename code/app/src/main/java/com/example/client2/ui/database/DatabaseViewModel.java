package com.example.client2.ui.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel per il {@link DatabaseFragment}.
 * <p>
 * Questa classe segue l'architettura MVVM (Model-View-ViewModel) e ha il compito
 * di fornire dati osservabili al fragment. In questo caso gestisce un semplice
 * testo esposto come {@link LiveData}.
 * </p>
 *
 * <p>
 * L'utilizzo di {@link LiveData} permette al fragment di osservare i cambiamenti
 * e aggiornare automaticamente la UI quando i dati vengono modificati.
 * </p>
 */
public class DatabaseViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Costruttore che inizializza il testo con un valore predefinito.
     */
    public DatabaseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    /**
     * Restituisce il testo come {@link LiveData}.
     * <p>
     * In questo modo la UI può osservare il dato senza modificarlo direttamente,
     * garantendo l'incapsulamento e il rispetto dell'architettura MVVM.
     * </p>
     *
     * @return il testo osservabile.
     */
    public LiveData<String> getText() {
        return mText;
    }
}
