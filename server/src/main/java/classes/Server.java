package classes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    public static void initConnection(int port) throws IOException, Exception {
        ServerSocket server = new ServerSocket(port);
        server.setReuseAddress(true);
        
        while (true) {            
            System.out.println("Aguardando conexão");
            try(Socket conn = server.accept();){
                System.out.println("Conectado com: " + conn.getInetAddress().getHostAddress());
                OutputStream out = conn.getOutputStream();
                String[] infos = out.toString().split(";");
                
                Pessoa pessoa = new Pessoa(infos[1], infos[2], infos[3]);
                
                FactoryComando factory = new FactoryComando();
                Comando comando = factory.getComando(infos[0]);
                comando.execute(pessoa);
                String msg = comando.returnMessage();
                               
                out.write(msg.getBytes());
            }
        }
        
    }
    
}