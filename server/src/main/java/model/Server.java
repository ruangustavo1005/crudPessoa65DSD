package model;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.TelaServer;

public class Server extends Thread  {
    
    private ServerSocket server;
    private OutputStream out;

    @Override
    public void run() {
        while (true) {            
            TelaServer.getInstance().addLog("Aguardando conexão");
            try(Socket conn = server.accept();){
                TelaServer.getInstance().addLog("Conectado com: " + conn.getInetAddress().getHostAddress());
                this.out = conn.getOutputStream();
                String[] infos = out.toString().split(";");
                if(infos[0].equals(Comando.GET)) {
                    this.trataComandoGet(infos[1]);
                } else if(infos[0].equals(Comando.LIST)) {
                    this.trataComandoList();
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
    
    private void trataComandoGet(String cpf) throws IOException{
        if(Dao.getInstance().getPessoas().isEmpty()) {
            this.throwMessage("Sem pessoas cadastradas");
            return;
        }
        for(Pessoa p : Dao.getInstance().getPessoas()) {
            if(p.getCpf().equals(cpf)) {
                this.throwMessage(p.toString());
                return;
            }
        }
        this.throwMessage("Pessoa não encontrada");
    }
    
    private void trataComandoList() throws IOException {
        String returnMsg = ""+Dao.getInstance().getPessoas().size()+"\n";
        Dao.getInstance().getPessoas().forEach(p -> {
            returnMsg.concat(p.toString() + "\n");
        });
        this.throwMessage(returnMsg);
    }
    
    private void throwMessage(String message) throws IOException {
        out.write(message.getBytes());
        TelaServer.getInstance().addLog(message);
    }
    
}