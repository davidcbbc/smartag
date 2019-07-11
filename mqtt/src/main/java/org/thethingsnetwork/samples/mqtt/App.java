package org.thethingsnetwork.samples.mqtt;
import org.json.JSONObject;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import org.thethingsnetwork.data.common.messages.DownlinkMessage;
import org.thethingsnetwork.data.common.messages.RawMessage;


/**
 * Hello world!
 *
 */


public class App 
{
    public static void main( String[] args ) throws Exception
    {
        Ttn ttn = new Ttn();

        Client client = new Client(ttn.getRegion(),ttn.getAppId(),ttn.getAcessKey());



        client.onError((Throwable _error) ->
        System.out.println("error: " + _error.getMessage()));



        client.onConnected((Connection _client) -> {
                    System.out.println("Conexão ao The Things Network sucedido !");
                }
                    );


        client.onMessage((String devId, DataMessage data) -> {
                    //message
            data = (UplinkMessage) data;
            int port = ((UplinkMessage) data).getPort();
            System.out.println(port);
            System.out.println("Message: " + devId + " " + ((UplinkMessage) data).getCounter());
            System.out.println("Modulaçao:"  + ((UplinkMessage) data).getMetadata().getModulation());
            System.out.println("Fields:" +  ((UplinkMessage) data).getPayloadFields().toString());
        });



        client.onActivation((String _devId, ActivationMessage _data) -> System.out.println("Activation: " + _devId + ", data: " + _data));





        // começar conexão ao servidor
        client.start();

        System.out.println("A começar conexão ao TTN ....");

        //client.end();
    }
}
