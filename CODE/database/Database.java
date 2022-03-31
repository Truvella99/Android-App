package com.example.geocilento.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Classe astratta che rappresenta il nostro database e funge da punto di accesso principale
 * per la connessione ai dati. Specifico in particolare a quale entities il database è legato, la versione
 * e se esportare lo schema o meno.
 */
@androidx.room.Database(
        entities = {Luogo.class},
        version = 1,
        exportSchema = false
)
// classe che ci consente di accedere al dao dell' entity luogo e quindi ai metodi

public abstract class Database extends RoomDatabase {
    /**
     * Attributo statico che rappresenta il Database.
     */
    private static Database database;

    /**
     * Metodo astratto che ci restituisce il Dao, dunque i vari metodi per accedere
     * effettivamente ai dati.
     * @return L'interfaccia LuogoDao con i vari metodi per gestire i dati.
     */
    public abstract LuogoDao getLuogoDao();

    /**
     * Metodo statico che ci consente di creare il database istanziandolo una sola volta, evitando così di aprire e chiudere i file
     * sottostanti più volte, garantendo così prestazioni maggiori. Synchronized per evitare conflitti al primo avvio dell'applicativo
     * con il thread che popola il Database.
     * @param context contesto necessario per istanziare il Database.
     * @return istanza dal Database.
     */
    public synchronized static Database getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),Database.class,"luoghi_turistici.db")
                    .allowMainThreadQueries().build();
        }
        return database;
    }

}
