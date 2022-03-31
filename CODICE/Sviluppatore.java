package com.example.geocilento;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  Classe che rappresenta l'activity mediante la quale vengono mostrate informazioni inerenti allo sviluppatore.
 */
public class Sviluppatore extends AppCompatActivity {
    /**
     * • nome_sviluppatore = attributo utilizzato per mostrare il nome dello sviluppatore.
     * • foto = attributo utilizzato per mostrare l'immagine dello sviluppatore.
     * • infoIo = attributo utilizzato per mostrare informazioni inerenti lo sviluppatore.
     * • contatti = attributo utilizzato per mostrare l'e-mail dello sviluppatore.
     * • actionBar = attributo che rappresenta la classe ActionBar utilizzato per il settaggio dei colori.
     */
    private TextView nome_sviluppatore;
    private ImageView foto;
    private TextView infoIo;
    private TextView contatti;
    private ActionBar actionBar;

    /**
     * Metodo di creazione dell'activity, in cui è implementata anche la logica di visualizzazione delle infromazioni inerenti lo sviluppatore.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività. Al primo avvio dunque è null.
     *                           Se non null, questa activity viene ricostruita da uno stato salvato precedentemente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sviluppatore);
        nome_sviluppatore = findViewById(R.id.nome_sviluppatore);
        foto = findViewById(R.id.foto);
        infoIo = findViewById(R.id.infoIo);
        contatti = findViewById(R.id.contatti);
        actionBar =getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0099CC")));
        setTitle("Sviluppatore");
    }
}