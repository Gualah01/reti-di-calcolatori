import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class utente{
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

            //ELABORAZIONE
            InputStreamReader tastiera = new InputStreamReader (System.in);
            BufferedReader br = new BufferedReader (tastiera);

            String nome = new String();
            boolean valido = false;
            while (valido == false) {
                System.out.print("Inserisci il nome: ");
                nome = br.readLine();
                nome = nome.replace("\n", "").replace("\r", "");
                String frase = "register@" + nome;
                byte[] buffer = frase.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                dp.setSocketAddress(sServer);
                sClient.send(dp);

                int dim_buffer = 100;
                byte[] bf = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(bf, dim_buffer);
                sClient.receive(dpin);
                String stringa = new String(bf, 0, dpin.getLength());
                //System.out.println(stringa);

                if(stringa.trim().equals("ACK")){
                    System.out.println("Registrazione avvenuta con successo");
                    valido = true;
                }else{
                    System.out.println("Registrazione fallita, nome non valido, riprovare");
                }
            }

            while(true){
                System.out.print("Premi qualsiasi tasto per aggiornare il contenuto...");
                br.readLine();
                String frase = "update@" + nome;
                byte[] buffer = frase.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                dp.setSocketAddress(sServer);
                sClient.send(dp);

                int dim_buffer = 100;
                byte[] bf = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(bf, dim_buffer);
                sClient.receive(dpin);
                String stringa = new String(bf, 0, dpin.getLength());
                //System.out.println("Server: " + stringa);

                String[] s = stringa.split("@");
                if(s[0].trim().equals("NIL")){
                    System.out.println("Contenuto ancora vuoto");
                }else if(s[0].trim().equals("Contenuto in modifica, riprovare dopo")){
                    continue;
                }else if(s.length == 2){
                    System.out.println("Contenuto attuale modificato da " + s[1] +": " + s[0]);
                }

                System.out.println("Inserisci il contenuto da aggiornare: ");
                frase = "content@" + nome + "@" + br.readLine();
                buffer = frase.getBytes();
                dp = new DatagramPacket(buffer, buffer.length);
                dp.setSocketAddress(sServer);
                sClient.send(dp);

                bf = new byte[dim_buffer];
                dpin = new DatagramPacket(bf, dim_buffer);
                sClient.receive(dpin);
                stringa = new String(bf, 0, dpin.getLength());
                System.out.println("Server: " + stringa);
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }
    }
}