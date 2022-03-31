package com.example.geocilento.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocilento.GridAdapter;
import com.example.geocilento.MainActivity;
import com.example.geocilento.R;
import com.example.geocilento.RecyclerAdapter;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;
import com.example.geocilento.databinding.ActivityMainBinding;
import com.example.geocilento.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe rappresentante il fragment che mostra le immagini e il nome delle località.
 */
public class SlideshowFragment extends Fragment {
    /**
     *
     * • binding = attributo che rappresenta la classe FragmentSlideshowBinding che contiene riferimenti diretti a tutte le viste che hanno un ID nel layout corrispondente,
     * in questo caso fragment_slideshow.xml.
     * • recyclerView = attributo che rappresenta la classe RecyclerView.
     * • GridAdapter = attributo che rappresenta la classe GridAdapter.
     * • operation = attributo che rappresenta la classe astratta LuogoDao per effettuare le operazioni nel database.
     * • CilentoList = lista rappresentante i luoghi.
     */
    private FragmentSlideshowBinding binding;
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    private LuogoDao operation;
    private List<Luogo> CilentoList = new ArrayList<>();

    /**
     * Metodo per la creazione della vista, in cui vi è anche la logica di istanziazione dei vari attributi.
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

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.gridView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        operation= Database.getDatabase(requireContext()).getLuogoDao();
        CilentoList = operation.getAll();

        gridAdapter = new GridAdapter(CilentoList);

        recyclerView.setAdapter(gridAdapter);

        recyclerView.setHasFixedSize(true);

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