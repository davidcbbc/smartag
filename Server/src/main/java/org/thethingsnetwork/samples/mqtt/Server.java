package org.thethingsnetwork.samples.mqtt;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.*;
import org.thethingsnetwork.data.mqtt.Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import org.thethingsnetwork.data.mqtt.Client;



public class Server {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";



    /**
     * Global instance of the scopes required by this quickstart. If modifying these
     * scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception, IOException {
        // Load client secrets.
        InputStream in = Server.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public boolean existe_evento_agora(Calendar service) throws IOException {
        //procura se existe um evento de momento
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("smartag04@gmail.com").setMaxResults(1).setTimeMin(now).setOrderBy("startTime")
                .setSingleEvents(true).execute();
        List<Event> items = events.getItems();
        if (!items.isEmpty()) return false;
        return true;
    }




    public static void main(String... args) throws IOException, GeneralSecurityException, Exception {
        // Dá build a uma API Autorizada para o cliente
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();



        // instaciar calendario
        Server calendario = new Server();
        Ttn ttn = new Ttn();

        Client client = new Client(ttn.getRegion(),ttn.getAppId(),ttn.getAcessKey());

        client.onError((Throwable _error) ->
                System.out.println("error: " + _error.getMessage()));



        client.onConnected((Connection _client) -> {
                    System.out.println("Conexão ao The Things Network bem sucedido !");
                }
        );


        client.onMessage((String devId, DataMessage data) -> {
            //mensagem recebida
            data = (UplinkMessage) data;
            int port = ((UplinkMessage) data).getPort();
            try{
                if(port == 4)           //porta 4 é do sinal do botao primido
                if(calendario.existe_evento_agora(service)){
                    Evento evt = new Evento(
                            new Event()
                                    .setSummary("Reunião")
                                    .setLocation("Critical Software")
                                    .setDescription("Uma nova reunião foi criada.")
                            ,
                            null,
                            service
                    );
                    evt.setEvento(service.events().insert(evt.getCalendarID(),evt.getEvento()).execute());
                    System.out.println("Evento criado: " + evt.getEvento().getHtmlLink() );
                    System.out.println("Não existe nenhuma reunião de momento, a marcar reunião até as " + evt.time_stamp(evt.getFim().getDateTime().getValue()));
                    DownlinkMessage dm = new DownlinkMessage(1,new byte[]{0x01});
                    client.send("nas04",dm);            //envia sinal positivo (1)
                } else  {
                    System.out.println("Está a decorrer uma reunião de momento , por favor marque quando acabar.");
                    DownlinkMessage dm = new DownlinkMessage(1,new byte[]{0x00});
                    client.send("nas04",dm);            //envia sinal negativo (0)
                }
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Apanhei excepcão ao calendário");
            }
        });



        client.onActivation((String _devId, ActivationMessage _data) -> System.out.println("Activation: " + _devId + ", data: " + _data));




        System.out.println("A ligar ao TTN ...");
        client.start();             // começar conexão ao TTN


    }
}