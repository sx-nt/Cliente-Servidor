package esd;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

interface Comunicacao{
    void enviarMensagem(String mensagem) throws IOException;
    String recebimentoMensagem() throws IOException;
    void fechar() throws IOException;
}

class Servidor implements Comunicacao  {

        private Socket socket;
        private ServerSocket serverSocket;
        private PrintWriter saida;
        private BufferedReader entrada;


        public Servidor(int porta) throws IOException{
            serverSocket = new ServerSocket(porta);
        }

        @Override
        public void enviarMensagem(String mensagem){
            saida.println(mensagem);
        }

        @Override
        public String recebimentoMensagem() throws IOException{
            return entrada.readLine();
        }

        public void iniciar() throws IOException {
            this.serverSocket.accept();
        }

        public void comunicar() throws IOException {
            String mensagem;
            while((mensagem = recebimentoMensagem()) != null){
                System.out.println("Cliente: " + mensagem);

                if(mensagem.equalsIgnoreCase("sair")){
                    enviarMensagem("Conexão encerrada pelo cliente. ");
                    break;
                }

                enviarMensagem("Cliente: " + mensagem);

            }

        }

        public void fechar() throws IOException{
            socket.close();
            serverSocket.close();
        }
}

class Cliente implements Comunicacao{
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private BufferedReader teclado;

    public void conectar(String host, int porta) throws IOException{
        socket = new Socket(host, porta);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        saida = new PrintWriter(socket.getOutputStream());
        teclado = new BufferedReader(new InputStreamReader(System.in));
    }

    public void comunicar() throws IOException {
        String mensagem;
        boolean conectado = true;

        while(conectado){
            System.out.println("> ");

            mensagem = teclado.readLine();
            enviarMensagem(mensagem);

            String resposta = recebimentoMensagem();
            System.out.println(resposta);

            if(mensagem.equalsIgnoreCase("sair")){
                conectado = false;
            }

        }

    }


    @Override
    public void enviarMensagem(String mensagem){
        saida.println(mensagem);
    }


    @Override
    public String recebimentoMensagem() throws IOException{
        return entrada.readLine();
    }

    @Override
    public void fechar() throws IOException{
        if(entrada != null) entrada.close();
        if(saida != null) saida.close();
        if(socket != null )socket.close();
        if(teclado != null)teclado.close();
    }
}

class ServidorMain{
    public static void main(String[] args) {
        try{
            Servidor servidor = new Servidor(8080);
            servidor.iniciar();
            servidor.comunicar();
            servidor.fechar();

        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Erro no servidor");

        }
    }
}

class ClienteMain{
    public static void main(String[] args) {
        try{
            Cliente cliente = new Cliente();
            cliente.conectar("localhost",8080 );
            cliente.comunicar();
            cliente.fechar();

        }catch (IOException e){
            System.out.println("Erro no cliente");
            System.out.println(e.getMessage());

        }
    }
}



