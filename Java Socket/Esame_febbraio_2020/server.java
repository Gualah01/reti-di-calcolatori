import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class server {
    public static void main(String[] args) {
        ArrayList<contenuto> contenuti = new ArrayList<contenuto>();
        contenuti.add(new contenuto("",new user("A"),new user("B")));
        contenuti.add(new contenuto("",new user("C"),new user("D")));
        contenuti.add(new contenuto("",new user("E"),new user("F")));
        
        DatagramSocket sSrv;
        try{
            sSrv = new DatagramSocket();
            System.out.println("Indirizzo: " + sSrv.getLocalAddress() + ";  porta: " + sSrv.getLocalPort());
            System.out.println("In attesa di richieste...");

            int dim_buffer = 100;
            while(true){
                //RICEZIONE
                byte[] buffer = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(buffer, dim_buffer);
                sSrv.receive(dpin);
                String stringa = new String(buffer, 0, dpin.getLength());
                InetAddress ia = dpin.getAddress();
                //System.out.println("Ricevuto: " + stringa + " Indirizzo: " + ia.getHostAddress() + "; porta:" + dpin.getPort());

                //ELABORAZIONE
                String[] s = stringa.split("@");
                String command = s[0];
                String name = s[1];
                int porta = dpin.getPort();
                boolean valido = false;
                String risposta = new String();
                switch (command) {
                    case "register":
                        for (contenuto c : contenuti) {
                            if(c.u1.nome.equals(name) && c.u1.porta == -1){
                                c.u1.porta = porta;
                                valido = true;
                                break;
                            }else if(c.u2.nome.equals(name) && c.u2.porta == -1){
                                c.u2.porta = porta;
                                valido = true;
                                break;
                            }
                        }
                        if(valido){
                            risposta = "ACK";
                        }else{
                            risposta = "NACK";
                        }
                        break;
                    
                    case "update":
                        for (contenuto c : contenuti) {
                            if(c.u1.nome.equals(name) && c.u1.porta == porta){
                                valido = true;
                                if(c.inModifica){
                                    risposta = "Contenuto in modifica, riprovare più tardi";
                                }else{
                                    c.inModifica = true;
                                    if(c.ultimoModificatore != null){
                                        risposta = c.contenuto + "@" + c.ultimoModificatore.nome;
                                    }else{
                                        risposta = "NIL";
                                    }
                                }
                                break;
                            }else if(c.u2.nome.equals(name) && c.u2.porta == porta){
                                valido = true;
                                if(c.inModifica){
                                    risposta = "Contenuto in modifica, riprovare dopo";
                                }else{
                                    c.inModifica = true;
                                    if(c.ultimoModificatore != null){
                                        risposta = c.contenuto + "@" + c.ultimoModificatore.nome;
                                    }else{
                                        risposta = "NIL";
                                    }
                                }
                            }
                        }
                        if(!valido){
                            risposta = "Nome non valido";
                        }
                        break;
                    
                    case "content":
                        String content = s[2];
                        for (contenuto c : contenuti) {
                            if(c.u1.nome.equals(name) && c.u1.porta == porta){
                                valido = true;
                                c.contenuto = content;
                                c.ultimoModificatore = c.u1;
                                c.inModifica = false;
                                break;
                            }else if(c.u2.nome.equals(name) && c.u2.porta == porta){
                                valido = true;
                                c.contenuto = content;
                                c.ultimoModificatore = c.u2;
                                c.inModifica = false;
                                break;
                            }
                        }
                        if(valido){
                            risposta = "Contenuto aggiornato";
                        }else{
                            risposta = "Nome non valido";
                        }
                        break;
                }

                //INVIO
                InetSocketAddress sClient = new InetSocketAddress(ia.getHostAddress() , porta);
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

class contenuto{
    public String contenuto;
    public boolean inModifica;
    public user u1;
    public user u2;
    public user ultimoModificatore;

    public contenuto(String contenuto, user u1, user u2){
        this.contenuto = contenuto;
        inModifica = false;
        this.u1 = u1;
        this.u2 = u2;
        ultimoModificatore = null;
    }
}

class user{
    public String nome;
    public int porta; // -1 non registrato 0+ registrato

    public user(String nome){
        this.nome = nome;
        porta = -1;
    }
}