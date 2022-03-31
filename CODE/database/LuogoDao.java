package com.example.geocilento.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaccia che contiene la definizione dei metodi per accedere al database ed effettuare le
 * operazioni concrete sul database (Create,Update,Delete...).
 */
@Dao
public interface LuogoDao {
    /**
     * Metodo che effettua l'inserimento di una lista di luoghi nel database. In caso di conflitto
     * l'inserimento viene abortito.
     * @param list lista con cui popolare il Database.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertAll(List<Luogo> list);

    /**
     * Metodo che effettua l'inserimento di un luogo nel database. In caso di conflitto
     * l'inserimento viene abortito.
     * @param luogo luogo da inserire.
     * @return id della riga inserita.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public Long insertLuogo(Luogo luogo);

    /**
     * Metodo che effettua l'aggiornamento di un luogo nel database.
     * @param luogo luogo da aggiornare.
     */
    @Update
    public void updateLuogo(Luogo luogo);

    /**
     * Metodo che effettua la cancellazione di un luogo dal database.
     * @param luogo luogo da cancellare.
     */
    @Delete
    public void deleteLuogo(Luogo luogo);

    /**
     * Metodo che restuisce il luogo corrispondente all'id inserito.
     * @param idLuogo id del luogo desiderato.
     * @return luogo desiderato.
     */
    @Query("SELECT * FROM luogo WHERE id=:idLuogo")
    public Luogo findLuogoById(int idLuogo);

    /**
     * Metodo che restituisce tutti gli elementi che popolano il database.
     * @return la lista di luoghi che popolano il database.
     */
    @Query("SELECT * FROM luogo")
    public List<Luogo> getAll();

    /**
     * Metodo che cancella tutti i luoghi presenti all'interno del database.
     */
    @Query("DELETE FROM luogo")
    public void deleteAll();

}
