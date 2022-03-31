package com.example.geocilento;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.LuogoDao;
import com.example.geocilento.ui.gallery.GalleryFragment;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import java.util.List;

/**
 * Classe che riceve e gestisce delle operazioni di broacast relative al geofencing, inizializzato mediante
 * un pendingIntent nella classe GeofenceHelper.
 * In molti casi, un Broadcastreceiver è un buon modo per gestire una transizione geofence.
 * Un Broadcastreceiver riceve aggiornamenti quando si verifica un evento, come una transizione da o verso un geofence, e può iniziare un lavoro di background di lunga durata.
 */
public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    /**
     * • TAG = tag utlizzato per mostrare messaggi di log in caso di errore.
     * • ID = stringa utilizzata per memorizzare l'id di creazione del geofence che è stato triggerato.
     */
    private static final String TAG = "GeofenceBroadcastRec";
    private String ID;

    /**
     * Metodo invocato quando il Broadcastreceiver riceve una trasmissione da parte di un Intent.
     * Qui viene gestita la logica di invio delle notifiche attraverso il notificationHelper,
     * prelevando tutti i geofences triggerati con un ciclo for each e, a seconda del tipo di transizione nel geofence,
     * viene inviata la notifica corrispondente.
     * @param context Il contesto in cui il ricevitore è in esecuzione.
     * @param intent L'Intent che è stato ricevuto.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            ID=geofence.getRequestId();
            Log.d(TAG, "onReceive: " + ID);
        }

        int transitionType = geofencingEvent.getGeofenceTransition();
        LuogoDao operation = Database.getDatabase(context).getLuogoDao();
        int id = Integer.parseInt(ID);
        String zone = operation.findLuogoById(id).getName();
        switch(transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification(zone, "Benvenuto");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                notificationHelper.sendHighPriorityNotification(zone, "Buona Permanenza");
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification(zone, "Arrivederci");
                break;
        }
    }
}