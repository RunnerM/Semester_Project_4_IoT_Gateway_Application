import java.net.http.WebSocket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.EventListener;

public class main {
    //private WebSocketClientForLoRa client;

    public static void main(String[] args)
    {
        WebSocketClientForLoRa client= new WebSocketClientForLoRa("wss://iotnet.cibicom.dk/app?token=vnoTwQAAABFpb3RuZXQuY2liaWNvbS5ka4qP2md7b3WkazgKPPLNzYo=");
        while(true){}
    }
}
