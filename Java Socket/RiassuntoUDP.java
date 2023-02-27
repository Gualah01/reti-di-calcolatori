//SERVER UDP

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
                String risposta = new String();
                switch(command){}

                //INVIO
                InetSocketAddress sClient = new InetSocketAddress(ia.getHostAddress() , porta);
                byte[] bf = risposta.getBytes();
                DatagramPacket dp = new DatagramPacket(bf, bf.length);
                dp.setSocketAddress(sClient);
                sSrv.send(dp);
            }
        {catch(Exception e){
            System.out.println("Errore: " + e.getMessage());
        }


//CLIENT UDP
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
            while(true){
                //send
                System.out.print("Inserisci il nome: ");
                nome = br.readLine();
                nome = nome.replace("\n", "").replace("\r", "");
                String frase = "register@" + nome;
                byte[] buffer = frase.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                dp.setSocketAddress(sServer);
                sClient.send(dp);

                //receive
                int dim_buffer = 100;
                byte[] bf = new byte[dim_buffer];
                DatagramPacket dpin = new DatagramPacket(bf, dim_buffer);
                sClient.receive(dpin);
                String stringa = new String(bf, 0, dpin.getLength());
                System.out.println(stringa);
            }



