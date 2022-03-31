package com.example.geocilento;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.geocilento.database.Converter;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;

/**
 * Classe che rappresenta l'activity mediante la quale è possibile visualizzare meglio le immagini
 * attraverso operazioni di pinch-in e pinch-out.
 */
public class Gallery extends AppCompatActivity {
    /**
     * • immagine = attributo che rappresenta la classe SubsamplingScaleImageView, utilizzata per la visualizzazione delle immagini.
     * • actionBar = attributo che rappresenta la classe ActionBar utilizzato per il settaggio dei colori.
     */
    private SubsamplingScaleImageView immagine;
    private ActionBar actionBar;

    /**
     * Metodo di creazione dell'activity, in cui è implementata anche la logica di visualizzazione dell'immagine della località.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività. Al primo avvio dunque è null.
     *                           Se non null, questa activity viene ricostruita da uno stato salvato precedentemente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        immagine = (SubsamplingScaleImageView ) findViewById(R.id.image_gallery);
        actionBar =getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0099CC")));
        if(getIntent()!=null) {
            LuogoDao operation = Database.getDatabase(getBaseContext()).getLuogoDao();
            Luogo l = operation.findLuogoById((int) getIntent().getExtras().get("Id_Luogo"));
            immagine.setImage(ImageSource.bitmap(Converter.getBitmap(l.getImage_type())));
        }
        setTitle("Gallery");
    }
}