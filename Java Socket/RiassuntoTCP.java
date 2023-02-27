//CLIENT TCP

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


//SERVER TCP MUTLI-THREAD
    ServerSocket sSrv;
    Socket toClient;
    try {
        sSrv = new ServerSocket(0);
        System.out.println("Indirizzo: " + sSrv.getInetAddress() + "; porta: " + sSrv.getLocalPort());

        while (true) {
            toClient = sSrv.accept();
            System.out.println("Indirizzo client: " + toClient.getInetAddress() + "; porta: " + toClient.getPort());
            Thread t = new erogaServizio(toClient);
            t.start();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

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


