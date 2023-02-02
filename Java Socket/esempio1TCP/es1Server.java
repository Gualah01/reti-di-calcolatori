import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;

public class es1Server {
    public static void main(String[] args){
        ServerSocket sSrv;
        Socket toClient;
        int dim_buffer = 100;
        int letti;

        try{
            sSrv = new ServerSocket(0);
            System.out.println("Ind. Client: " + sSrv.getInetAddress() + "; porta: " + sSrv.getLocalPort());
            //NUMERO PORTA DA INSERIRE NEL CODICE CLIENT
            toClient = sSrv.accept();
            System.out.println("Ind. Client: " + toClient.getInetAddress() + "; porta: " + toClient.getPort());
            InputStream fromC1 = toClient.getInputStream();
            byte[] buffer = new byte[dim_buffer];
            letti = fromC1.read(buffer);
            if(letti > 0){
                String stampa = new String(buffer, 0, letti);
                System.out.println("Ricevuta stringa: " + stampa + " di " + letti + " byte da " + toClient.getInetAddress() + "; " + toClient.getPort());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
