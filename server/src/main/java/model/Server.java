package model;

import commands.Comando;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.TelaServer;

public class Server extends Thread  {
    
    private ServerSocket server;
    private InputStream input;
    private OutputStream out;
    
    public void initConnection(int port) throws IOException, Exception {
        this.server = new ServerSocket(port);
        server.setReuseAddress(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {            
            TelaServer.getInstance().addLog("Aguardando conexão");
            try(Socket conn = server.accept();){
                TelaServer.getInstance().addLog("Conectado com: " + conn.getInetAddress().getHostAddress());
                this.out = conn.getOutputStream();
                this.input = conn.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                int dadosLidos = input.read(dadosBrutos);
                if(dadosLidos >= 0)  {
                    String dadosStr = new String(dadosBrutos, 0, dadosLidos);
                    TelaServer.getInstance().addLog(dadosStr);
                    String[] infos = dadosStr.split(";");
                    FactoryComando factory = new FactoryComando();
                    Comando comando = factory.getComando(infos[0], infos[1]);
                    comando.execute(infos);
                    this.throwMessage(comando.returnMessage());
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void throwMessage(String message) throws IOException {
        out.write(message.getBytes());
        TelaServer.getInstance().addLog(message);
    }
    
}