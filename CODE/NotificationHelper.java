package com.example.geocilento;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Random;

/**
 * Classe di supporto per gestire la creazione e l'invio di notifiche
 * (estende ContextWrapper in modo da avere il contesto d'interesse all'interno della classe)
 */
public class NotificationHelper extends ContextWrapper {
    /**
     *  • CHANNEL_NAME = nome del notification channel
     *  • CHANNEL_ID = id univoco del notification channel
     */
    private static final String CHANNEL_NAME = "GeoCilento";
    private static final String CHANNEL_ID = "com.example.geoCilento" + CHANNEL_NAME;

    /**
     * Costruttore che istanzia la classe. In particolare crea il canale
     * necessario per le notifiche solo per versioni maggiori o uguali ad Android Oreo
     * @param base contesto per l'inizializzazzione della classe.
     */
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    /**
     * Metodo per la creazione del notification channel. Richiede le API al livello di Oreo. In particolare viene creato con IMPORTANCE_HIGH
     * in modo tale che l'utente veda subito la notifica. La descrizione del canale sarà visibile nelle
     * impostazioni dell'applicazione per descrivere il canale stesso. Definito il notification channel, attraverso il notification manager
     * lo creo nell'effettivo.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("App per la Geolocalizzazione finalizzata alla promozione del territorio");
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }

    /**
     * Metodo che crea la notifica e la invia. La priorità della notifica è comunque high dato che
     * per API più basse il channel non viene creato e noi vogliamo comunque un comportamento
     * ad alta priorità. Una volta definita la notifica, viene postata attraverso il NotificationManagerCompat
     * che è più efficiente rispetto al NotificationManager. Ad esso passiamo quindi l'ID della notifica
     * (random in modo da non sovrascrivere le notifiche qualore avessero stesso id) e la notifica stessa da postare.
     *
     * @param title Titolo della notifica da inviare.
     * @param body Corpo della notifica da inviare.
     */
    public void sendHighPriorityNotification(String title, String body) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notifica)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
    }
}