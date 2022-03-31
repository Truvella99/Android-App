package com.example.geocilento.ui.gallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geocilento.R;
import com.example.geocilento.RecyclerAdapter;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;
import com.example.geocilento.databinding.FragmentGalleryBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe rappresentante il fragment che mostra per ogni località il nome, se è di mare o montagna e il suo rating.
 */
public class GalleryFragment extends Fragment {
    /**
     * L'associazione delle viste è una funzione che consente di scrivere più facilmente codice che interagisce con le viste.
     * Una volta abilitato il binding della vista in un modulo, esso genera una classe vincolante per ogni file di layout XML presente in quel modulo.
     * Un'istanza di una classe vincolante contiene riferimenti diretti a tutte le viste che hanno un ID nel layout corrispondente.
     *
     * • binding = attributo che rappresenta la classe FragmentGalleryBinding che contiene riferimenti diretti a tutte le viste che hanno un ID nel layout corrispondente,
     * in questo caso fragment_gallery.xml.
     *
     * Recyclerview semplifica la visualizzazione efficiente di grandi insiemi di dati. Si forniscono i dati e si definisce come ogni elemento appare,
     * e la libreria Recyclerview crea dinamicamente gli elementi quando sono necessari.
     * Come suggerisce il nome, Recyclerview ricicla i singoli elementi. Quando un elemento scorre fuori dallo schermo,
     * Recyclerview non distrugge la sua vista. Invece, Recyclerview riutilizza la vista per i nuovi elementi che vengono visualizzati sullo schermo.
     * Questo riutilizzo migliora notevolmente le prestazioni, migliorando la reattività della tua app e riducendo il consumo energetico.
     *
     * • recyclerView = attributo che rappresenta la classe RecyclerView.
     * • recyclerAdapter = attributo che rappresenta la classe recyclerAdapter.
     * • operation = attributo che rappresenta la classe astratta LuogoDao per effettuare le operazioni nel database.
     * • CilentoList = lista rappresentante i luoghi.
     */
    private FragmentGalleryBinding binding;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private LuogoDao operation;
    private List<Luogo> CilentoList = new ArrayList<>();

    /**
     * inflating = file XML che rappresenta un layout viene renderizzato creando un oggetto view in memoria.
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

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);

        operation= Database.getDatabase(requireContext()).getLuogoDao();
        CilentoList = operation.getAll();

        recyclerAdapter = new RecyclerAdapter(CilentoList);

        recyclerView.setAdapter(recyclerAdapter);

        //Divisore fra le varie righe
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    /**
     * Metodo invocato quando l'utente, dopo aver messo in secondo piano l'activity, ritorna sulla stessa.
     * In questo metodo viene effettuato il refresh dei dati in caso l'utente modifichi il rating del luogo.
     */
    @Override
    public void onResume() {
        super.onResume();
        CilentoList.clear();
        CilentoList.addAll(operation.getAll());
        recyclerAdapter.notifyDataSetChanged();
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