package com.example.client2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel associato all'HomeFragment.
 * <p>
 * Espone un {@link LiveData} contenente un semplice testo dimostrativo,
 * che può essere osservato dall'interfaccia utente per aggiornare i componenti
 * grafici in maniera reattiva.
 * </p>
 */
public class HomeViewModel extends ViewModel {

    /**
     * Testo osservabile esposto all'interfaccia utente.
     */
    private final MutableLiveData<String> mText;

    /**
     * Costruttore: inizializza il testo con un valore di default.
     */
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    /**
     * Restituisce il testo come {@link LiveData} per permettere
     * all'interfaccia di osservarne le modifiche.
     *
     * @return il testo osservabile
     */
    public LiveData<String> getText() {
        return mText;
    }
}
