package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import message.DeleteMessageParser;
import message.GetMessageParser;
import message.MessageParser;
import model.Pessoa;
import util.ConfigUtils;
import util.Connection;
import util.MessageDialog;
import util.Response;
import view.ViewIndex;

/**
 * @author Ruan
 */
public class ControllerIndex {

    private ViewIndex view;

    public ControllerIndex() {
        this.view = new ViewIndex();
        this.addActionListeners();
    }
    
    public void abreTela() {
        this.setIPPortFromConfig();
        this.getView().setVisible(true);
    }

    private void setIPPortFromConfig() {
        try {
            String config = ConfigUtils.getConfig();
            
            if (config != null && !config.isEmpty()) {
                String[] configs = config.split(":");
                this.getView().getFieldIP().setText(configs[0]);
                this.getView().getFieldPort().setText(configs[1]);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Não foi possível ler o arquivo de configurações", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public ViewIndex getView() {
        return view;
    }
    
    private void addActionListeners() {
        this.addActionListenerInsert();
        this.addActionListenerUpdate();
        this.addActionListenerDelete();
        this.addActionListenerGet();
        this.addActionListenerList();
        this.addActionListenerSaveIP();
        this.addActionListenerTestConnection();
    }
    
    private void addActionListenerInsert() {
        this.getView().getButtonInsert().addActionListener((ActionEvent actionEvent) -> {
            (new ControllerFormPessoa()).abreTela();
        });
    }
    
    private void addActionListenerUpdate() {
        this.getView().getButtonUpdate().addActionListener((ActionEvent actionEvent) -> {
            (new ControllerFormPessoa(this.getSelectedModel())).abreTela();
        });
    }
    
    private void addActionListenerDelete() {
        this.getView().getButtonDelete().addActionListener((ActionEvent actionEvent) -> {
            Response retorno = this.delete();
            
            MessageDialog.show(this.getView(), retorno);
        });
    }
    
    private void addActionListenerGet() {
        this.getView().getButtonGet().addActionListener((ActionEvent actionEvent) -> {
//            esse precisa vir do servidor
//            (new ControllerFormPessoa(this.getSelectedModel(), true)).abreTela();
        });
    }
    
    private void addActionListenerList() {
        this.getView().getButtonList().addActionListener((ActionEvent actionEvent) -> {
            
        });
    }
    
    private void addActionListenerSaveIP() {
        this.getView().getButtonSaveIP().addActionListener((ActionEvent actionEvent) -> {
            try {
                String ip = this.getView().getFieldIP().getText();
                String port = this.getView().getFieldPort().getText();
                
                ConfigUtils.setConfig(ip.concat(":").concat(port));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getView(), "Não foi possível ler o arquivo de configurações", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void addActionListenerTestConnection() {
        this.getView().getButtonTestConnection().addActionListener((ActionEvent actionEvent) -> {
            int timeout = 1000;
            try {
                try (Socket socket = (new Connection(timeout)).getInstanceSocket()) {
                    JOptionPane.showMessageDialog(this.getView(), "Conexão OK", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getView(), "Conexão recusada (expirado tempo de espera de " + timeout + " milissegundo(s))", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private Pessoa getSelectedModel() {
        Pessoa retorno = null;
        if (this.getView().getTable().getRowCount() == 1) {
            retorno = this.getView().getTableModelPessoa().getData().get(this.getView().getTable().getSelectedRow());
        }
        return retorno;
    }
    
    private Response delete() {
        Response retorno;
        try {
            Socket socket = (new Connection(5000)).getInstanceSocket();
            MessageParser<Pessoa> messageParser = new DeleteMessageParser<>(this.getSelectedModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            retorno = new Response(true, "");
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }
 
}
