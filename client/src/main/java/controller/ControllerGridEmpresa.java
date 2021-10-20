package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.event.ListSelectionEvent;
import message.DeleteMessageParser;
import message.GetMessageParser;
import message.ListMessageParser;
import message.MessageParser;
import model.Empresa;
import util.Connection;
import util.DateUtils;
import util.MessageDialog;
import util.Response;
import view.ViewGridEmpresa;

/**
 * @author Ruan
 */
public class ControllerGridEmpresa extends Controller {

    private ViewGridEmpresa view;
    
    public ControllerGridEmpresa(Controller caller) {
        super(caller);
        this.addActionListeners();
        this.addTableListeners();
        this.onSelectRow();
    }
    
    public void abreTela() {
        this.getView().setVisible(true);
    }

    public ViewGridEmpresa getView() {
        return view;
    }
    
    private void addActionListeners() {
        this.addActionListenerInsert();
        this.addActionListenerUpdate();
        this.addActionListenerDelete();
        this.addActionListenerGet();
        this.addActionListenerList();
    }
    
    private void addTableListeners() {
        this.getView().getTable().getSelectionModel().addListSelectionListener((ListSelectionEvent listSelectionEvent) -> {
            this.onSelectRow();
        });
    }
    
    private void onSelectRow() {
        boolean enable = this.getView().getTable().getSelectedRowCount() == 1;
        this.getView().getButtonUpdate().setEnabled(enable);
        this.getView().getButtonDelete().setEnabled(enable);
        this.getView().getButtonGet().setEnabled(enable);
    }
    
    private void addActionListenerInsert() {
        this.getView().getButtonInsert().addActionListener((ActionEvent actionEvent) -> {
            (new ControllerFormEmpresa(this)).abreTela();
        });
    }
    
    private void addActionListenerUpdate() {
        this.getView().getButtonUpdate().addActionListener((ActionEvent actionEvent) -> {
            (new ControllerFormEmpresa(this, this.getSelectedModel())).abreTela();
        });
    }
    
    private void addActionListenerDelete() {
        this.getView().getButtonDelete().addActionListener((ActionEvent actionEvent) -> {
            Empresa empresa = this.getSelectedModel();
            
            Response retorno = this.delete();
            
            MessageDialog.show(this.getView(), retorno);
            
            if (retorno.isSuccess()) {
                this.getView().getTableModelEmpresa().deleteData(empresa);
            }
        });
    }
    
    private void addActionListenerGet() {
        this.getView().getButtonGet().addActionListener((ActionEvent actionEvent) -> {
            Response retorno = this.get();
            
            if (retorno.isSuccess()) {
                String[] dados = retorno.getMessage().split(";");
                (new ControllerFormEmpresa(this, new Empresa(dados[0], dados[1], DateUtils.stringToDate(dados[2])), true)).abreTela();
            }
            else {
                MessageDialog.show(this.getView(), retorno);
            }
        });
    }
    
    private void addActionListenerList() {
        this.getView().getButtonList().addActionListener((ActionEvent actionEvent) -> {
            Response retorno = this.list();
            
            if (retorno.isSuccess()) {
                this.getView().getTableModelEmpresa().clearData();
                
                String[] lines = retorno.getMessage().split("\n");
                
                int quantidade = Integer.valueOf(lines[0]);
                if (quantidade > 0) {
                    for (int i = 1; i < lines.length; i++) {
                        String[] dados = lines[i].split(";");
                        this.getView().getTableModelEmpresa().addData(new Empresa(dados[0], dados[1], DateUtils.stringToDate(dados[2])));
                    }
                }
            }
            else {
                MessageDialog.show(this.getView(), retorno);
            }
        });
    }
    
    private Empresa getSelectedModel() {
        Empresa retorno = null;
        if (this.getView().getTable().getSelectedRowCount() == 1) {
            retorno = this.getView().getTableModelEmpresa().getData().get(this.getView().getTable().getSelectedRow());
        }
        return retorno;
    }
    
    private Response delete() {
        Response retorno;
        try {
            Socket socket = (new Connection()).getInstanceSocket();
            
            MessageParser<Empresa> messageParser = new DeleteMessageParser<>(this.getSelectedModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            
            InputStream inputStream = socket.getInputStream();
            byte[] dadosBrutos = new byte[1024];
            retorno = new Response(true, new String(dadosBrutos, 0, inputStream.read(dadosBrutos)));
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }
    
    private Response get() {
        Response retorno;
        try {
            Socket socket = (new Connection()).getInstanceSocket();
            
            MessageParser<Empresa> messageParser = new GetMessageParser<>(this.getSelectedModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            
            InputStream inputStream = socket.getInputStream();
            byte[] dadosBrutos = new byte[1024];
            String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
            retorno = new Response(response.split(";").length == 3, response);
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }
    
    private Response list() {
        Response retorno;
        try {
            Socket socket = (new Connection()).getInstanceSocket();
            
            MessageParser<Empresa> messageParser = new ListMessageParser<>(Empresa.class);
            socket.getOutputStream().write(messageParser.getMessageBytes());
            
            InputStream inputStream = socket.getInputStream();
            
            byte[] dadosBrutos = new byte[1024];
            int qtdBytesLidos = inputStream.read(dadosBrutos);
            String dados = null;
            while (qtdBytesLidos >= 0) {
                dados = new String(dadosBrutos, 0, qtdBytesLidos);
                qtdBytesLidos = inputStream.read(dadosBrutos);
            }
            
            retorno = new Response(true, dados);
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        } catch (InstantiationException | IllegalAccessException ex) {
            retorno = new Response(false, "Houve um erro interno");
        }
        return retorno;
    }

}