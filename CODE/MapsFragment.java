package com.example.geocilento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * Classe rappresentante il fragment che mostra le località mediante google maps.
 */
public class MapsFragment extends Fragment {
    /**
     * • TAG = stringa utilizzata per il log.
     * • mappa = attributo che rappresenta l'oggetto googleMap.
     * • geofencingClient = attributo che rappresenta il geofencingClient.
     * • geofenceHelper = attributo che rappresenta il geofenceHelper.
     * • FINE_LOCATION_ACCESS_REQUEST_CODE = codice per la richiesta dei permessi di fine location.
     * • BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = codice per la richiesta dei permessi di background location.
     * • GEOFENCE_RADIUS = raggio in metri dei geofences da creare.
     */
    private static final String TAG = "MapsActivity";
    private GoogleMap mappa;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 1;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 2;
    private final float GEOFENCE_RADIUS = 150;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         *
         * Metodo richiamato non appena la mappa risulta essere disponibile. Qui istanzio i punti relativi ai 3 paesi del comune,
         * effettuo la richiesta dei permessi e, in caso questi vengano concessi, l'inserimento dei geofences andrà a buon fine,
         * altrimenti verranno richiesti al momento.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mappa = googleMap;
            LatLng sgap = new LatLng(40.05113365430598, 15.44834222139601);
            mappa.addMarker(new MarkerOptions().position(sgap).title("San Giovanni a Piro").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mappa.moveCamera(CameraUpdateFactory.newLatLngZoom(sgap, 15));
            LatLng scario = new LatLng(40.05459691515396, 15.490779493108136);
            mappa.addMarker(new MarkerOptions().position(scario).title("Scario").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            LatLng bosco = new LatLng(40.070582191557186, 15.45679766914614);
            mappa.addMarker(new MarkerOptions().position(bosco).title("Bosco").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mappa.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    if (Build.VERSION.SDK_INT >= 29) {
                        //Controllo i permessi
                        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                                //Mostro una finestra di dialogo e chiedo i permessi
                                ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                            } else {
                                ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                            }
                        }
                    }
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && !mappa.isMyLocationEnabled()) {
                        mappa.setMyLocationEnabled(true);
                    }
                }
            });
            enableUserLocation();
            try_add_Geofences();
        }

        /**
         * Metodo che implementa l'aggiunta dei geofences nella mappa,
         * tramite un ciclo for each sulla lista che contiene le informazioni degli elementi del database.
         */
        private void try_add_Geofences() {
            LuogoDao operation= Database.getDatabase(requireContext()).getLuogoDao();
            List<Luogo> list= operation.getAll();
            for (Luogo app: list) {
                LatLng ap = app.getLatLng();
                addMarker(ap,app.getName());
                addCircle(ap,GEOFENCE_RADIUS);
                addGeofence(Integer.toString(app.getId()),ap,GEOFENCE_RADIUS);
            }
        }

        /**
         * Metodo per richiedere i permessi di fine location.
         */
        private void enableUserLocation() {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mappa.setMyLocationEnabled(true);
            } else {
                //Controllo i permessi
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Mostro una finestra di dialogo e chiedo i permessi
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);

                } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);

                }
            }
        }

        /**
         * Metodo per aggiungere un marker sulla mappa, data la sua posizione.
         * @param latLng Posizione (latitudine e longitudine) del marker.
         * @param title Descrizione disponibile cliccando sul marker.
         */
        private void addMarker(LatLng latLng, String title) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            markerOptions.title(title);
            mappa.addMarker(markerOptions);
        }

        /**
         * Metodo per aggiungere un cerchio visivo, rappresentante il geofence istanziato, sulla mappa.
         * @param latLng Centro (latitudine e longitudine) del geofence.
         * @param radius Raggio del geofence.
         */
        private void addCircle(LatLng latLng, float radius) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.radius(radius);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(64, 255, 0, 0));
            circleOptions.strokeWidth(4);
            mappa.addCircle(circleOptions);
        }

        /**
         * Metodo che consente l'aggiunta di geofence, gestendo l'aggiunta con successo o meno con stampe sul log.
         * @param ID Id univoco del geofence da istanziare.
         * @param latLng Posizione (latitudine e longitudine) del geofence.
         * @param radius Raggio del geofence.
         */
        private void addGeofence(String ID, LatLng latLng, float radius) {
            Geofence geofence = geofenceHelper.getGeofence(ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL);
            GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
            PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: Geofence Added");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String errorMessage = geofenceHelper.getErrorString(e);
                    Log.d(TAG, "onFailure: " + errorMessage);
                }
            });
        }
    };

    /**
     * Metodo per la creazione della vista, in cui vi è anche la logica di istanziazione dei vari attributi.
     * @param inflater oggetto LayoutInflater che può essere utilizzato per effettuare l'inflating di qualsiasi vista nel fragment.
     * @param container Se non null, questa è la vista genitore a cui dovrebbe essere collegata l'interfaccia utente del frammento.
     *                  Il frammento non dovrebbe aggiungere la vista in sé, ma il container viene usato per generare i parametri per il layout della vista.
     *                  Questo valore può essere nullo.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività a cui il fragment è legato.
     *                           Al primo avvio dunque è null. Se non nullo, questo fragment viene ricostruito da uno stato salvato precedentemente.
     * @return la view creata.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Istanzio il geofencing Client e il geofenceHelper
        geofencingClient = LocationServices.getGeofencingClient(requireActivity());
        geofenceHelper = new GeofenceHelper(getContext());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /**
     * Metodo chiamato non appena è terminato il metodo onCreateView. Qui viene recuperata tramite SupportMapFragment l'oggetto mappa e definito il callback.
     * @param view la view restituita da onCreateView.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività a cui il fragment è legato.
     *                           Al primo avvio dunque è null. Se non nullo, questo fragment viene ricostruito da uno stato salvato precedentemente.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}