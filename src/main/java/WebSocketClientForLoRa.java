import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import com.google.gson.Gson;
/**
 * <h3>WEBSOCKET CLIENT</h3>
 * It implements functionality to receive data from the
 * IoT board through the LoRaWAN network via websockets
 * and send commands back to the LoRaWAN network
 *
 * @author  Ali
 * @version 0.9.10
 */
public class WebSocketClientForLoRa implements WebSocket.Listener {
    private WebSocket server = null;

    // Send down-link message to device
    // Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    // E.g. url: "wss://iotnet.teracom.dk/app?token=??????????????????????????????????????????????="
    // Substitute ????????????????? with the token you have been given
    public WebSocketClientForLoRa(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);

        server = ws.join();
    }

    //onOpen()
    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
    }

    //onError()
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    };
    //onClose()
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    };
    //onPing()
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    };
    //onPong()
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    };
    //onText()
    /**
     * This method is invoked when a message from the LoRa network websocket is received
     * It deserlized the received message, extracts the relevant data and uses DB driver to save it to the DB
     * @param webSocket the websocket which we are connected to.
     * @param data the data recieved from the socket,as a sequnce of characters, will be tuned into a json
     * @return CompletionStage irrelevant.
     */
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String indented = null;
        try{
            indented = (new JSONObject(data.toString())).toString(4);
            System.out.println(indented);
        }catch(JSONException e){
            System.out.println("Json Error");
            return null;
        }
        Gson gson = new Gson();
        Message message = gson.fromJson(indented, Message.class);
        if (message.getCmd().equals("rx"))
        {
            //hex values of each piece of data
            String humhex = message.getData().substring(2,4);
            String temphex = message.getData().substring(6,8);
            String co2hex = message.getData().substring(8,12);
            String luxhex = message.getData().substring(12,16);
            //time from message
            Timestamp time = new Timestamp(message.ts);
            //hex to int
            int humidity = Integer.parseInt(humhex,16);
            int temperature = Integer.parseInt(temphex,16);
            //not sure if its correct for co2 and lux
            int co2 = Integer.parseInt(co2hex, 16);
            int lux = Integer.parseInt(luxhex,16);
            Measurement measurement = new Measurement(0,time,temperature,humidity,co2,lux);
            System.out.println(measurement);
            try
            {
                DBDriverManager db = DBDriverManager.getInstance();
                db.insertMeasurements(message.getEUI(), measurement);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            try
            {
                sendCommand(message.getEUI(), measurement.getTime());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            webSocket.request(1);
        }
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    };

    /**
     * Is the methods responsible for sending the commands retrieved from the database to the board.
     * @param EUI the EUI of the device that the command will be sent to, should be noted that this is only for identifying the board in the database, not in the LoRa network.
     * @param time the time which the initiating uplink message was sent to the LoRa network, is used to define the time window of relevant tasks.
     * @return Nothing.
     */
    private void sendCommand(String EUI,Timestamp time)
    {
        //byte 0 : 0 = servo position 100, 1 = servo position -100
        //byte 1 : 0 = led off, 1 = led on
        String command = null;
        //replace the db stuff tomorrow
        //surround the following with try catch maybe, idk
        try {
            DBDriverManager DB = DBDriverManager.getInstance();
            boolean[] results = DB.getActionStates(EUI, time);
            if(results[2])
            {
                if(results[0])
                {
                    command = "01";
                }
                else
                {
                    command = "00";
                }
                if(results[1])
                {
                    command += "01";
                }
                else
                {
                    command += "00";
                }

            //do we need a confirmed field in the downlink message? and do we need to set it to false here?
            DownlinkMessage msg = new DownlinkMessage("tx","0004A30B00251001",2, command);
            //refer to line 75
            Gson gson = new Gson();
            //surround with try catch maybe
            System.out.println(gson.toJson(msg));
            sendDownLink(gson.toJson(msg));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}