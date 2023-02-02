import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class server{
    public static void main(String[] args) {
        HashMap<Integer,String> treni = new HashMap<Integer,String>();
        DatagramSocket sSrv;
        try{
            sSrv = new DatagramSocket();
            System.out.println("Indirizzo: " + sSrv.getLocalAddress() + "; porta:" + sSrv.getLocalPort());
            
            while(true){
                int dim_buffer = 100;
                byte[] buffer = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(buffer, dim_buffer);
                sSrv.receive(dpin);
                String stringa = new String(buffer, 0, dpin.getLength());
                InetAddress ia = dpin.getAddress();
                //System.out.println("Ricevuto: " + stringa + " Indirizzo: " + ia.getHostAddress() + "; porta:" + dpin.getPort());

                String[] s = stringa.trim().split(" ", 2);
                int treno = Integer.parseInt(s[0]);
                int stazione = dpin.getPort();
                String risposta;
                if(s.length == 1){ //utente
                    System.out.println("Utente richiede la situazione del treno : " + s[0]);
                    if(treni.containsKey(treno)){
                        String[] r = treni.get(treno).split(" ");
                        risposta = "Treno " + treno + " alla stazione " + r[0] + ", in ritardo di " + r[1] + " minuti"; 
                    }else{
                        risposta = "Nessuna informazione";
                    }
                }else{ //stazione
                    int ritardo = Integer.parseInt(s[1]);
                    System.out.println("Stazione " + dpin.getPort() + ": treno " + s[0] + " in ritardo di " + s[1] + " minuti");
                    treni.put(treno, stazione + " " + ritardo);
                    risposta = "Messaggio ricevuto";
                }

                InetSocketAddress sClient = new InetSocketAddress(ia.getHostAddress() , stazione);
                byte[] bf = risposta.getBytes();
                DatagramPacket dp = new DatagramPacket(bf, bf.length);
                dp.setSocketAddress(sClient);
                sSrv.send(dp);

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
