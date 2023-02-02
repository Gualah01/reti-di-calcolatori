import java.io.InputStream;
import java.net.Socket;

public class erogaServizio extends Thread {
    private Socket sock2Cl;

    public erogaServizio(Socket s) {
        sock2Cl = s;
    }

    public void run() {
        int dim_buffer = 100;
        byte[] buffer = new byte[dim_buffer];

        while (true) {
            try {
                InputStream fromCl = sock2Cl.getInputStream();
                int letti = fromCl.read(buffer);
                if (letti > 0) {
                    String stampa = new String(buffer, 0, letti);
                    System.out.println("Ricevuta stringa: " + stampa + " di " + letti + " byte da " + sock2Cl.getInetAddress() + "; " + sock2Cl.getPort());
                } else{
                    sock2Cl.close();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
