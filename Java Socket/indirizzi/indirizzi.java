import java.net.InetAddress;
import java.net.UnknownHostException;

public class indirizzi {
    public static void main(String[] args)
    {
        String nome = "www.google.com"; //da convertire in indirizzo ip

        //estrazione di tutti gli indirizzi associati ad un nome
        try {
            InetAddress[] iaa = InetAddress.getAllByName(nome);

            for (int i = 0; i < iaa.length; i++) {
                System.out.println("Indirizzo " + iaa[i].getHostName() + " --> " + iaa[i].getHostAddress());
            }
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        //confronto tra modalità di visualizzazione indirizzi
        try {
            InetAddress ia = InetAddress.getByName(nome);
            byte[] ndp = ia.getAddress();
            System.out.println("Indirizzo " + ndp);
            System.out.println("Indirizzo " + ndp[0] + "." + ndp[1] + "." + ndp[2] + "." + ndp[3]);
            System.out.println("Indirizzo " + (ndp[0] & 0xff) + "." + (ndp[1] & 0xff) + "." + (ndp[2] & 0xff) + "." + (ndp[3] & 0xff));
            String nst = ia.getHostAddress();
            System.out.println("Indirizzo da stringa " + nst);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();   
        }

        // significato metodo getLocalHost 
        try {
            InetAddress mia = InetAddress.getLocalHost(); 
            byte[] mya = mia.getAddress();
            System.out.println("Indirizzo locale " + (mya[0] & 0xff) + "." + (mya[1] & 0xff) + (mya[2] & 0xff)); 
            String lst = mia.getHostName(); 
            System.out.println("Indirizzo locale da stringa " + lst); 
        } catch (UnknownHostException uhe){
            uhe.printStackTrace(); 
        }
        
        // es. modalita' use getElyAddress 
        try { 
            byte[] cna = new byte[] { (byte)159, (byte)149, (byte)130, (byte)49 };
            InetAddress cnn = InetAddress.getByAddress(cna); 
            String cnm = cnn.getHostName();
            System.out.println("Indirizzo " + "159.149.130.49" + " --> " + cnm); 
        } catch (UnknownHostException uhe){ 
            uhe.printStackTrace(); 
        } // end main 
    }
}