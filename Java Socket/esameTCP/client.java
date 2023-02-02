import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class client {
    public static void main(String[] args)
    {
        Socket sClient;
        InetAddress ia; // IP address CLIENT e SERVER
        InetSocketAddress MYisa; // socket address CLIENT
        InetSocketAddress isa; // socket address SERVER
        String frase;

        sClient = new Socket ();
        try {
            ia = InetAddress.getLocalHost ();
            MYisa = new InetSocketAddress(ia, 0); // porta effimera
            sClient.bind(MYisa); // bind esplicita
            System.out.println("Porta locale: " + sClient.getLocalPort());

            // INDIRIZZO SERVER: INSERIRE PORTA STAMPATA DA PROPRIO SERVER!
            // e poi ricompilare...
            isa = new InetSocketAddress(ia, 11154);

            sClient.connect (isa) ;
            System.out.println("Ind. Server: " + sClient.getInetAddress()
            +"; porta: " + sClient.getPort());

            InputStreamReader tastiera = new InputStreamReader (System.in);
            BufferedReader br = new BufferedReader (tastiera) ;
            frase = br.readLine();
            // frase += "\r\n";
            OutputStream toSrv = sClient.getOutputStream();
            toSrv.write(frase.getBytes(), 0, frase.length());
        } catch(Exception e) {
            e.printStackTrace(); 
        }
        finally{
            try {
                sClient.close();
            } catch (Exception e) {
                System.err.println("Client error");
                e.printStackTrace();
            }
        }
    }
}
