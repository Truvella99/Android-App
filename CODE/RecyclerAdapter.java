package com.example.geocilento;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geocilento.database.Converter;
import com.example.geocilento.database.Luogo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Classe che implementa la visualizzazione a lista degli elementi nel gallery fragment.
 * Estende un Adapter, i quali forniscono un collegamento da un set di dati specifico dell'app alle viste visualizzate all'interno di una Recyclerview.
 */
public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    /**
     * • CilentoList = lista degli elementi presenti nel database.
     */
    private List<Luogo> CilentoList;

    /**
     * Costruttore della classe.
     * @param CilentoList lista degli elementi presenti nel database.
     */
    public RecyclerAdapter(List<Luogo> CilentoList) {
        this.CilentoList = CilentoList;
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Metodo chiamato dalla Recyclerview per visualizzare i dati nella posizione specificata.
     * Il metodo prende la view e inserisce i dati nella view.
     * @param holder ViewHolder per rappresentare l'elemento.
     * @param position posizione dell'elemento i-esimo da rappresentare.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textView2.setText(CilentoList.get(position).getName());
        holder.imageView.setImageBitmap(Converter.getBitmap(CilentoList.get(position).getImage()));
        holder.rate.setRating(CilentoList.get(position).getRating());
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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /**
         * • imageView = immagine del singolo elemento.
         * • textView2 = testo del singolo elemento.
         * • rate = votazione relativa al singolo elemento.
         */
        ImageView imageView;
        TextView textView2;
        RatingBar rate;

        /**
         * Costruttore della classe.
         * @param itemView vista del singolo elemento mediante la quale istanziare imageView, textView e rating bar.
         */
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            textView2 = itemView.findViewById(R.id.textView2);
            rate = itemView.findViewById(R.id.ratingBar);

            // click listener per ogni elemento della lista
            itemView.setOnClickListener(this);
        }

        /**
         * Metodo che gestisce le operazioni da effettuare al click di un elemento,
         * avviando un'activity che mostra le informazioni della località.
         * @param v vista relativa all'elemento.
         */
        @Override
        public void onClick(View v) {
            // Qui avvio un'altra activity che mostra i dati nel dettaglio dell'iesimo luogo
            Intent i = new Intent(v.getContext(), Info_Luogo.class);
            i.putExtra("Id_Luogo",CilentoList.get(getAbsoluteAdapterPosition()).getId());
            v.getContext().startActivity(i);
        }
    }
}
