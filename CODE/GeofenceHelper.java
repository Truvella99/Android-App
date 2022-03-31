package com.example.geocilento;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

/**
 * Classe che implementa la logica di gestione dei geofences
 * (estende ContextWrapper in modo da avere il contesto d'interesse all'interno della classe)
 */
public class GeofenceHelper extends ContextWrapper {
    /**
     * pending intent = Descrizione di un Intent (descrizione astratta di un'operazione da eseguire) e di un'azione mirata da eseguire con esso.
     * • pendingIntent = attributo utilizzato per eseguire le operazioni di broadcast che la Classe GeofenceBroadcastReceiver catturerà.
     */
    private PendingIntent pendingIntent;

    /**
     * Costruttore della Classe.
     * @param base contesto per l'inizializzazzione della classe.
     */
    public GeofenceHelper(Context base) {
        super(base);
    }

    /**
     * Metodo che restituisce una GeofencingRequest, classe per specificare i geofences da monitorare e
     * per impostare come vengono attivati i relativi eventi.
     * @param geofence geofence da monitorare.
     * @return il GeofencingRequest relativo al geofence passato.
     */
    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    /**
     * Metodo che consente di costruire un geofence, impostando la latitudine, longitudine, la tipologia
     * di transizione, il tempo di risposta fra ingresso e permanenza nel geofence (5 minuti miglior trade-off per risparmio energetico),
     * la durata della geofence ed il tempo di risposta relativo alla notifica.
     * @param ID id col quale istanziare il geofence (univoco per ogni geofence).
     * @param latLng posizione geografica del geofence (latitudine e longitudine).
     * @param radius raggio del geofence da creare.
     * @param transitionTypes tipologia di transizione nel geofence da gestire (Enter, Exit e Dwell).
     * @return il geofence creato.
     */
    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(300000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setNotificationResponsiveness(5000)
                .build();
    }

    /**
     * Metodo che restituisce il pendingIntent mediante il quale viene avviato il
     * GeofenceBroadcastReceiver, che rimane in ascolto per eventi sui geofences.
     * @return il pendingIntent creato.
     */
    public PendingIntent getPendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this,GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    /**
     * Metodo utilizzato per gestire il caso in cui le API relative al geofencingClient
     * all' aggiunta di un geofence restituiscano un errore.
     * @param e Eccezione generata.
     * @return stringa che descrive l'oggetto dell'eccezione.
     */
    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }
}
