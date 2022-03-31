package com.example.geocilento.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.geocilento.databinding.FragmentHomeBinding;

/**
 * Classe rappresentante il fragment che mostra la mappa con le varie località.
 */
public class HomeFragment extends Fragment {
    /**
     * • binding = attributo che rappresenta la classe FragmentHomeBinding che contiene riferimenti diretti a tutte le viste che hanno un ID nel layout corrispondente,
     * in questo caso fragment_home.xml.
     */
    private FragmentHomeBinding binding;

    /**
     * Metodo per la creazione della vista.
     * @param inflater oggetto LayoutInflater che può essere utilizzato per effettuare l'inflating di qualsiasi vista nel fragment.
     * @param container Se non null, questa è la vista genitore a cui dovrebbe essere collegata l'interfaccia utente del frammento.
     *                  Il frammento non dovrebbe aggiungere la vista in sé, ma il container viene usato per generare i parametri per il layout della vista.
     *                  Questo valore può essere nullo.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività a cui il fragment è legato.
     *                           Al primo avvio dunque è null. Se non nullo, questo fragment viene ricostruito da uno stato salvato precedentemente.
     * @return la view creata.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    /**
     * Metodo invocato alla distruzione della view, quando non è più visibile.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}