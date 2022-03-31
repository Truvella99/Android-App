package com.example.geocilento;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.geocilento.database.Converter;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;

/**
 * Classe che rappresenta l'activity mediante la quale è possibile visualizzare le informazioni sulla singola località.
 */
public class Info_Luogo extends AppCompatActivity {
    /**
     * • titolo = attributo utilizzato per mostrare il nome della località.
     * • immagine = attributo utilizzato per mostrare l'immagine della località.
     * • descrizione = attributo utilizzato per mostrare la descrizione della località.
     * • rate = attributo utilizzato per mostrare il rating della località, modificabile dall'utente.
     * • actionBar = attributo che rappresenta la classe ActionBar utilizzato per il settaggio dei colori.
     */
    private TextView titolo;
    private ImageView immagine;
    private TextView descrizione;
    private RatingBar rate;
    private ActionBar actionBar;

    /**
     * Metodo di creazione dell'activity, in cui è implementata anche la logica di visualizzazione delle informazioni inerenti la località.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività. Al primo avvio dunque è null.
     *                           Se non null, questa activity viene ricostruita da uno stato salvato precedentemente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_luogo);
        titolo = findViewById(R.id.titolo);
        immagine = findViewById(R.id.immagine);
        descrizione = findViewById(R.id.descrizione);
        rate = findViewById(R.id.ratingBar2);
        actionBar =getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0099CC")));
        if(getIntent() != null) {
            LuogoDao operation = Database.getDatabase(getBaseContext()).getLuogoDao();
            Luogo l = operation.findLuogoById((int) getIntent().getExtras().get("Id_Luogo"));
            titolo.setText(l.getName());
            immagine.setImageBitmap(Converter.getBitmap(l.getImage_type()));
            descrizione.setText(l.getDescription());
            rate.setRating(l.getRating());
            rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    l.setRating(rating);
                    operation.updateLuogo(l);
                }
            });
        }
        setTitle("Dettagli Luogo");
    }

}