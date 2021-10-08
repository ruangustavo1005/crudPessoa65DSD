package classes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import screens.TelaServer;

public class Server extends Thread  {
    
    private ServerSocket server;

    @Override
    public void run() {
        while (true) {            
            TelaServer.getInstance().addLog("Aguardando conexão");
            try(Socket conn = server.accept();){
                TelaServer.getInstance().addLog("Conectado com: " + conn.getInetAddress().getHostAddress());
                OutputStream out = conn.getOutputStream();
                String[] infos = out.toString().split(";");
                if(infos[0].equals(Comando.GET)) {
                    out.write("Oi eu sou o goku".getBytes());
                } else {
                    Pessoa pessoa = new Pessoa(infos[1].trim(), infos[2], infos[3]);
                    FactoryComando factory = new FactoryComando();
                    Comando comando = factory.getComando(infos[0]);
                    comando.execute(pessoa);
                    out.write(comando.returnMessage().getBytes());
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void initConnection(int port) throws IOException, Exception {
        this.server = new ServerSocket(port);
        server.setReuseAddress(true);
        this.start();
    }
    
}