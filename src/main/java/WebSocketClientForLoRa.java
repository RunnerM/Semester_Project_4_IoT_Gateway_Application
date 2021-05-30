import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import com.google.gson.Gson;

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
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        String indented = null;
        try{
            indented = (new JSONObject(data.toString())).toString(4);
            System.out.println(indented);
            webSocket.request(1);
        }catch(JSONException e){
            System.out.println("Json Error");
            return null;
        }
        //maybe inject gson, idk
        Gson gson = new Gson();
        Message message = gson.fromJson(indented, Message.class);
        //idk what rx or cmd mean
        if (message.getCmd().equals("rx"))
        {
            //hex values of each piece of data
            String humhex = message.getData().substring(2,4);
            String temphex = message.getData().substring(6,8);
            String co2hex = message.getData().substring(8,12);
            String luxhex = message.getData().substring(12,16);
            //time from message
            Time t = new Time(message.ts);
            Time t1 = new Time(t.getHours(), t.getMinutes(), 0);
            Date date = new Date(message.ts);
            //hex to int
            int humidity = Integer.parseInt(humhex,16);
            int temperature = Integer.parseInt(temphex,16);
            //not sure if its correct for co2 and lux
            int co2 = Integer.parseInt(co2hex, 16);
            int lux = Integer.parseInt(luxhex,16);
            Measurement measurement = new Measurement(0,t1,date,temperature,humidity,co2,lux);
            //send stuff to db under this
            //AMOGUS
            //also call the senCommand method after putting the data in the db

        }
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    };
    private void sendCommand()
    {
        //byte 0 : 0 = servo position 100, 1 = servo position -100
        //byte 1 : 0 = led off, 1 = led on
        String command = null;
        //replace the db stuff tomorrow
        //surround the following with try catch maybe, idk
        if(amogusDB.getServoState())
        {
            command = "01";
        }
        else
        {
            command = "00";
        }
        if(amogusDB.getLightState())
        {
            command += "01";
        }
        else
        {
            command += "00";
        }
        //do we need a confirmed field in the downlink message? and do we need to set it to false here?
        DownlinkMessage msg = new DownlinkMessage("tx","0004A30B00251001",1, command);
        //refer to line 76
        Gson gson = new Gson();
        //surround with try catch maybe
        sendDownLink(gson.toJson(msg));

    }

}