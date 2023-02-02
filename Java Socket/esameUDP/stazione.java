import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class stazione {
    public static void main(String[] args) throws IOException{
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

            InetSocketAddress sServer = new InetSocketAddress(nome_host, porta);

            while(true){
                InputStreamReader tastiera = new InputStreamReader (System.in);
                BufferedReader br = new BufferedReader (tastiera);
                String frase = br.readLine();
                byte[] buffer = frase.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                dp.setSocketAddress(sServer);
                sClient.send(dp);
                if(frase == "."){
                    break;
                }

                int dim_buffer = 100;
                byte[] bf = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(bf, dim_buffer);
                sClient.receive(dpin);
                String stringa = new String(bf, 0, dpin.getLength());
                System.out.println(stringa);
            }
            sClient.close();
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }
}
