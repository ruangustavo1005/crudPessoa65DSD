package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import util.ConfigUtils;
import util.Connection;
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
        this.addActionListenerDelete();
        this.addActionListenerGet();
        this.addActionListenerInsert();
        this.addActionListenerList();
        this.addActionListenerSaveIP();
        this.addActionListenerTestConnection();
        this.addActionListenerUpdate();
    }
    
    private void addActionListenerDelete() {
        this.getView().getButtonDelete().addActionListener((ActionEvent actionEvent) -> {
            
        });
    }
    
    private void addActionListenerGet() {
        this.getView().getButtonGet().addActionListener((ActionEvent actionEvent) -> {
            
        });
    }
    
    private void addActionListenerInsert() {
        this.getView().getButtonInsert().addActionListener((ActionEvent actionEvent) -> {
            
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
                Socket socket = (new Connection(timeout)).getInstanceSocket();
                JOptionPane.showMessageDialog(this.getView(), "Conexão OK", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getView(), "Conexão recusada (expirado tempo de espera de " + timeout + " milissegundo(s))", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void addActionListenerUpdate() {
        this.getView().getButtonUpdate().addActionListener((ActionEvent actionEvent) -> {
            
        });
    }
    
}