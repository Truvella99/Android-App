package com.example.geocilento;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import com.example.geocilento.database.Converter;
import com.example.geocilento.database.Database;
import com.example.geocilento.database.Luogo;
import com.example.geocilento.database.LuogoDao;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.geocilento.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta l'activity principale, che viene creata all'avvio dell'applicazione.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * • mAppBarConfiguration = attributo per configurare il drawer menù.
     * • binding = attributo che rappresenta la classe ActivityMainBinding che contiene riferimenti diretti a tutte le viste che hanno un ID nel layout corrispondente,
     * in questo caso activity_main.xml.
     * • notificationHelper = attributo per l'invio delle notifiche.
     */
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NotificationHelper notificationHelper;

    /**
     * Metodo per la creazione della main activity principale. Qui viene implementata anche la logica di inizializzazione dei vari elementi grafici,
     * del notificationHelper e della popolazione del database (gestita con logica multi-threading onde evitare freeze della GUI),
     * qualora è il primo avvio dell'applicazione. Una volta che il database è stato popolato esso non è più soggetto a modifiche se non per i rating delle località.
     * @param savedInstanceState Consente la conservazione e il ripristino dello stato dell'interfaccia utente dell'attività a cui il fragment è legato.
     *                           Al primo avvio dunque è null. Se non nullo, questo fragment viene ricostruito da uno stato salvato precedentemente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database.getDatabase(getBaseContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        notificationHelper = new NotificationHelper(getApplicationContext());

        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isfirstrun",true);
        if (isFirstRun) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notificationHelper.sendHighPriorityNotification("Inizio","Popolo Il Database");
                                }
                            });
                            populate();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notificationHelper.sendHighPriorityNotification("Fine","Popolazione Database Completata");
                                }
                            });
                            getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isfirstrun",false).commit();
                            // refresh
                            finish();
                            startActivity(getIntent());
                        }
                    }).start();
        }
    }

    /**
     * Metodo che effettua l'inflating del menu; questo aggiunge elementi alla barra delle azioni se è presente.
     * @param menu menu di cui effettuare l'inflating.
     * @return valore booleano (true per visualizzare il menu; se restituisci false non verrà mostrato).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Questo metodo viene chiamato ogni volta che un elemento nel menu delle opzioni è selezionato.
     * L'implementazione predefinita restituisce semplicemente false per avere la normale elaborazione.
     * @param item La voce di menu selezionata. Questo valore non può essere nullo.
     * @return valore booleano (false per consentire la normale elaborazione del menu per procedere, vero per consumarlo qui).
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Sviluppatore.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo chiamato ogni volta che l'utente sceglie di navigare all'interno della gerarchia delle attività dell'applicazione dalla barra delle azioni.
     * @return valore booleano (vero se la navigazione è stata completata con successo e questa Attività è stata terminata, falso altrimenti).
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Metodo che gestisce la popolazione del database al privo avvio dell'applicazione.
     */
    public void populate() {
        LuogoDao operation = Database.getDatabase(getBaseContext()).getLuogoDao();
        List<Luogo> list = new ArrayList<>();
        list.add(new Luogo(40.05705735068635, 15.450709780031293, "Cenobio Basiliano", "Nel X secolo monaci italo-greci " +
                "di rito bizantino, detti anche basiliani perché ispirati alla regola di San Basilio Magno, si insediarono nel territorio del monte Bulgheria. " +
                "Il Cenobio fu diretto da illustri personalità come il cardinale Bessarione e il celebre umanista Teodoro Gaza. Intorno all’abbadia crebbe il " +
                "villaggio di S. Giovanni a Piro, traendone anche il toponimo: “Ab Epiro” indicherebbe, infatti, la provenienza dei monaci dall’Epiro.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.cenobio),"JPEG")));
        list.add(new Luogo(40.0501055794127, 15.452745298129875, "Chiesa San Pietro Apostolo", "Le prime notizie" +
                " certe sull’esistenza della chiesa parrocchiale di San Pietro Apostolo risalgono al XV secolo. La facciata della Chiesa è decorata" +
                " con il giglio, emblema del regno borbonico. All'interno, di particolare pregio, si possono ammirare: il settecentesco altare e una" +
                " bellissima acquasantiera in pietra locale di epoca medioevale. In fondo alla navata si apre la cappella della Congrega di Santa Maria della Pietà.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.chiesa_san_pietro_apostolo),"JPEG")));
        list.add(new Luogo(40.051462742262444, 15.449314755801527, "Chiesa San Gaetano da Thiene", "Edificata nel 1660," +
                " fu dapprima dedicata a Santa Rosalia in segno di ringraziamento da parte degli scampati alla peste del 1656 e, successivamente, nel 1888," +
                " a San Gaetano. Un bel portale di pietra del 1813 introduce nell’unica navata della chiesa. Bellissime maioliche del’700 decorano il pavimento," +
                " mentre sul soffitto un dipinto di Gaetano D’Angelo raffigura San Gaetano che accoglie il Bambino.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.chiesa_san_gaetano),"JPEG")));
        list.add(new Luogo(40.04760508370306, 15.462425726965659, "Santuario Maria SS. Di Pietrasanta", "I monaci basiliani," +
                " intorno al 1200, eressero la prima piccola cappella su uno sperone di roccia sulla quale sarà poi scolpita la statua della Madonna di Pietrasanta." +
                " La navata, il cui sviluppo disallineato rispetto al presbiterio fu imposto dalla conformazione del terreno circostante, risale al XVIII secolo." +
                " Sulle volte della navata tutti i dipinti furono realizzati da Gaetano D’Angelo durante i lavori di restauro che seguirono all’incendio che danneggiò" +
                " il Santuario il 3 gennaio del 1953.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.santuario),"JPEG")));
        list.add(new Luogo(40.04083585951943, 15.457192369293905, "Pianoro di Ciolandrea", "Conosciuto come " +
                "”Belvedere delle quattro regioni”, il pianoro di Ciolandrea è poco distante dal Santuario di Pietrasanta. Offre un meraviglioso" +
                " panorama del golfo di Policastro, della Masseta, della costa lucana, della costa calabra e, nelle giornate più terse, dello Stromboli" +
                " nelle isole Eolie. Lungo il sentiero che cammina lungo la cresta del vicino monte Paccuma, un bellissimo percorso fitness e piazzole" +
                " per il picnic attrezzate con panche e tavoli.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.ciolandrea),"JPEG")));
        list.add(new Luogo(40.0712326507287, 15.456651871145333, "Murale", "Nella piccola frazione di Bosco," +
                " nella piazzetta intitolata ai martiri dei moti cilentani del 1828, si erge un grande murale in ceramica. Opera di José Ortega del" +
                " 1980, rievoca il triste epilogo della rivolta conclusasi con l’incendio del piccolo borgo cilentano.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.murale),"JPEG")));
        list.add(new Luogo(40.07327667884421, 15.457310098130426, "Museo Casa Ortega", "Il palazzo ottocentesco," +
                " dal 2011 ospita una mostra permanente dei lavori del pittore spagnolo José Ortega, allievo di Picasso. Costretto all'esilio dal 1960 si trasferì" +
                " prima a Parigi, poi in Italia, a Matera e a Bosco. Nel piccolo borgo cilentano trascorse gli ultimi vent’anni, prima di morire a Parigi.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.ortega),"JPEG")));
        list.add(new Luogo(40.07343606635805, 15.45789304301765, "Chiesa di San Nicola", "Fondata nei primi del 1200" +
                " dai monaci bizantini, era confederata con altre quattro badie nei vicini casali della valle del Mingardo: San Pietro di Licusati, " +
                "San Nazario di Cuccaro, San Nicola di Centola e Santa Cecilia di Eremiti. Da segnalare il battistero del 1545 e l’acquasantiera del 1650. " +
                "Il prezioso pavimento in maiolica è del XIX secolo.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.chiesa_di_san_nicola),"JPEG")));
        list.add(new Luogo(40.073523319189974, 15.45654742006858, "Chiesa di San Rocco", "Non esiste alcun documento" +
                " che ci aiuti a risalire alla data della sua fondazione. Potrebbe essere stata eretta in tempo di peste, dopo il 1656, in quanto il culto" +
                " di San Rocco era associato a quello di Santa Rosalia. Nel presbiterio si trovano due altari in marmo, uno rivolto al popolo, semplice," +
                " l’altro in marmi policromi sovrastato dalla statua di San Rocco.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.montagna),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.chiesa_di_san_rocco),"JPEG")));
        list.add(new Luogo(40.05439118597425, 15.49250201359297, "Chiesa dell' Immacolata", "La Chiesa dell’Immacolata" +
                " si cominciò a costruire nel 1880. Nel 1912 il pittore lucano Pasquale Iannotti affrescò la volta dell’abside e dipinse il soffitto ligneo" +
                " della chiesa con al centro l’immagine dell’ Immacolata. Gli originali arredi liturgici in legno sono stati realizzati dall'artigiano locale" +
                " Vito Colicigno su disegni di Monica Hannasch, artista tedesca, scariota d'adozione.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.chiesa_dell_immacolata),"JPEG")));
        list.add(new Luogo(40.05015138606867, 15.49085292565216, "Cappella di Sant'Anna", "Alla fine del lungomare di Scario," +
                " nel suo angolo più caratteristico, troviamo la deliziosa cappella di Sant’Anna. Costruita nel 1883 dalla famiglia Bellotti, una delle più ricche" +
                " e potenti dell’epoca, che ne detiene tuttora la proprietà, mentre la gestione del culto è affidata alla parrocchia dell’ Immacolata. All’interno" +
                " la bellissima statua lignea della Santa che il 26 luglio viene portata in processione per le vie del paese.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.cappella_di_sant_anna),"JPEG")));
        list.add(new Luogo(40.041834979414034, 15.482576998129733, "Torri Saracene", "Le torri saracene arricchiscono il " +
                "tratto di costa tra Scario e Punta Infreschi. Furono volute nel 1532, dal vicerè di Napoli, per la difesa della costa dalle incursioni dei pirati." +
                " Tra le più belle la Torre Spinosa, sull’omonima punta e Torre del Morice o del Trarro, raggiungibili entrambe da comodi sentieri.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.torri_saracene),"JPEG")));
        list.add(new Luogo(40.04764529670023, 15.48884845580149, "Spiaggia della Tragara", "Andando verso nord, lungo" +
                " la costa di Scario, troviamo la Spiaggia della Tragara. La spiaggia della Tragara non è molto grande ed è raggiungibile con l’auto a poche" +
                " centinaia di metri dai pressi del centro abitato di Scario.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.tragara),"JPEG")));
        list.add(new Luogo(40.019379853886896, 15.445278504465563, "Spiaggia di Marcellino", "La spiaggia del Marcellino" +
                " o spiaggia dei Francesi è una delle più belle spiagge del Cilento e tra le spiagge di Scario più conosciute. L’arenile è largo un centinaio" +
                " di metri ed è composto da piccoli ciottoli e un’acqua stupenda. Per giungere sulla spiaggia del Marcellino è necessario utilizzare un’imbarcazione" +
                " oppure seguire il sentiero che scende dal pianoro di Ciolandrea.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.marcellino),"JPEG")));
        list.add(new Luogo(40.025657641886546, 15.453756635010507, "Spiaggia dei Gabbiani", "La spiaggia dei Gabbiani è una delle" +
                " più piccole spiagge di Scario ed è situata sulla Costa della Masseta. L’arenile è composto da ciottoli e per raggiungere la spiaggia è necessario" +
                " percorrere la via marina.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.gabbiani),"JPEG")));
        list.add(new Luogo(40.02121227670211, 15.44686599801868, "Spiaggia della Risima", "Dall’estate 2020 è stata riaperta" +
                " una delle perle della Costa della Masseta, la spiaggia della Risima. La spiaggia, divisa da un costone roccioso dalla spiaggia dei Francesi, " +
                "è caratterizzata da ciottoli bianchi e un’acqua spettacolare.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.risima),"JPEG")));
        list.add(new Luogo(40.022750171801206, 15.448768926965139, "Spiaggia della Sciabica", "La spiaggia della Sciabica" +
                " è una delle spiagge che compongono l’area marina protetta della Masseta. Essa fa parte di un gruppo di molte cale della diverse dimensioni," +
                " per la maggior parte raggiungibili solo via mare.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.sciabica),"JPEG")));
        list.add(new Luogo(40.028578664484286, 15.4564006917806, "Spiagge Gemelle", "Due spiagge dette Gemelle o Marinelle," +
                " di sabbia dorata e fine che sono letteralmente incastonate in una cornice di scogliere rocciose ricoperte di pini marittimi ed ulivi secolari." +
                " Attrezzate ma con zone libere.",
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.mare),"PNG"),
                Converter.getByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.spiagge_gemelle),"JPEG")));
        operation.insertAll(list);
    }
}