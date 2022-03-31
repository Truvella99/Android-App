package com.example.geocilento.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

/**
 * Classe che implementa un convertitore per gestire il caricamento bel database
 * e la visualizzazione delle immagini stesse.
 */
public class Converter {
    /**
     * Metodo statico che converte un oggetto Bitmap in ingresso in un byte array per salvarlo
     * all'interno del database.
     * @param bmp oggetto Bitmap in ingresso.
     * @param type tipologia di bitmap (immagine di tipo PNG o JPEG).
     * @return byte array che verrà utilizzato per popolare il database.
     */
    public static byte[] getByteArray(Bitmap bmp, String type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (type.equals("PNG")) {
            bmp.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        } else {
            bmp.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        }
        byte[] imagem_img = outputStream.toByteArray();
        return imagem_img;
    }

    /**
     * Metodo statico che converte un byte array, prelevato dal database, in un Bitmap da passare all'image view
     * per mostrarlo all'interno dell'app.
     * @param byteArray byte array da convertire.
     * @return l'oggetto Bitmap risultato.
     */
    public static Bitmap getBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
