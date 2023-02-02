import java.net.SocketException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

// codice client per servizio connection-less (UDP)

public class clientUDP
{
    public static void main(String[] args) throws IOException
    {
        DatagramSocket sClient;
        try {
            //parametri default server
            String nome_host = "localhost";
            int porta = 7000;

            //acquisizione parametri server
            if (args.length != 2) {
                throw new IllegalArgumentException("num.parametri non corretto");
            }
            nome_host = args[0];
            porta = Integer.parseInt(args[1]);
            if (porta <= 0){
                throw new IllegalArgumentException("porta non valida");
            }

            sClient = new DatagramSocket();
            System.out.println("Indirizzo: " + sClient.getLocalAddress()
            +"; porta: " + sClient.getLocalPort());

            InetSocketAddress isa = new InetSocketAddress(nome_host, porta);
            InputStreamReader tastiera = new InputStreamReader (System.in);
            BufferedReader br = new BufferedReader (tastiera);
            String frase = br.readLine();
            byte[] buffer = frase.getBytes();
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            dp.setSocketAddress(isa);
            sClient.send(dp);

        } catch (SocketException se) {
            se.printStackTrace();
        }
    }
}
    
