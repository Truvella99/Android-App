package com.example.geocilento;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geocilento.database.Converter;
import com.example.geocilento.database.Luogo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Classe che implementa la visualizzazione a griglia degli elementi nello slideshow fragment.
 * Estende un Adapter, i quali forniscono un collegamento da un set di dati specifico dell'app alle viste visualizzate all'interno di una Recyclerview.
 */
public class GridAdapter extends  RecyclerView.Adapter<GridAdapter.ViewHolder>{
    /**
     * • CilentoList = lista degli elementi presenti nel database.
     */
    private List<Luogo> CilentoList;

    /**
     * Costruttore della classe.
     * @param cilentoList lista degli elementi presenti nel database.
     */
    public GridAdapter(List<Luogo> cilentoList) {
        CilentoList = cilentoList;
    }

    /**
     * Metodo richiamato quando la Recyclerview ha bisogno di un nuovo Recyclerview.Viewholder del tipo dato per rappresentare un elemento.
     * @param parent Il Viewgroup in cui verrà aggiunta la nuova View dopo che questa sarà vincolata ad una posizione dell'adapter.
     * @param viewType il viewType della nuova view.
     * @return il ViewHolder per rappresentare l'elemento.
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Metodo chiamato dalla Recyclerview per visualizzare i dati nella posizione specificata.
     * @param holder ViewHolder per rappresentare l'elemento.
     * @param position posizione dell'elemento i-esimo da rappresentare.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(Converter.getBitmap(CilentoList.get(position).getImage_type()));
        holder.textView.setText(CilentoList.get(position).getName());
    }

    /**
     * Metodo che restituisce il numero totale di elementi nel set di dati detenuti dall'adapter.
     * @return il numero degli elementi.
     */
    @Override
    public int getItemCount() {
        return CilentoList.size();
    }

    /**
     * Classe innestata che serve a definire la vista di elementi e i loro dati all'interno della Recyclerview, estendendo il ViewHolder.
     * Implementa anche un listener sulla view per definire un comportamento nel caso in cui un elemento viene cliccato.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /**
         * • imageView = immagine del singolo elemento.
         * • textView = testo del singolo elemento.
         */
        ImageView imageView;
        TextView textView;

        /**
         * Costruttore della classe.
         * @param itemView vista del singolo elemento mediante la quale istanziare imageView e textView.
         */
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.grid_image);
            textView=itemView.findViewById(R.id.grid_text);

            // click listener per ogni elemento della lista
            itemView.setOnClickListener(this);
        }

        /**
         * Metodo che gestisce le operazioni da effettuare al click di un elemento,
         * avviando un'activity che mostra l'immagine della località.
         * @param v vista relativa all'elemento.
         */
        @Override
        public void onClick(View v) {
            // Qui avvio un'altra activity che mostra l'immagine dell'iesimo luogo
            Intent i = new Intent(v.getContext(), Gallery.class);
            i.putExtra("Id_Luogo",CilentoList.get(getAbsoluteAdapterPosition()).getId());
            v.getContext().startActivity(i);
        }
    }
}

