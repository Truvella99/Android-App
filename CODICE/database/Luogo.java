package com.example.geocilento.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.android.gms.maps.model.LatLng;

/**
 * Classe che rappresenta una tabella all’interno del database.
 */
// nome della tabella e definisco come indice per l'ottimizzazione l'id e come unique lat e lng, name
@Entity(tableName = "luogo", indices= {@Index("id"),@Index(value = {"lat","lng"},unique=true),@Index(value = {"name"},unique=true)})
public class Luogo {
    /**
     * • id = identificativo univoco della località considerata, che si autoincrementa all'inserimento.
     * • lat = latitudine.
     * • lng = longitudine.
     * • name = nome della località considerata.
     * • description = descrizione della località considerata.
     * • image = immagine che raffigura la tipologia della località, se di mare o di montagna.
     * • image_type = immagine che raffigura la località considerata.
     * • rating = preferenza espressa sulla località considerata.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    private double lat;
    private double lng;
    private String name;
    private String description;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte [] image;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte [] image_type;
    private float rating;

    /**
     * Costruttori della classe, getter e setter.
     */
    public Luogo() {
    }

    public Luogo(double lat, double lng, String name, String description, byte [] image, byte [] image_type) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.description = description;
        this.image = image;
        this.image_type = image_type;
    }

    public Luogo(double lat, double lng, String name, String description, byte[] image, byte[] image_type, float rating) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.description = description;
        this.image = image;
        this.image_type = image_type;
        this.rating = rating;
    }

    public LatLng getLatLng () {
        return new LatLng(lat,lng);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage_type() {
        return image_type;
    }

    public void setImage_type(byte[] image_type) {
        this.image_type = image_type;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
