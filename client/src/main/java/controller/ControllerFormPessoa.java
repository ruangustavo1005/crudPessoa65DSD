package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import message.InsertMessageParser;
import message.MessageParser;
import message.UpdateMessageParser;
import model.Pessoa;
import util.Connection;
import util.MessageDialog;
import util.Response;
import view.ViewFormPessoa;

/**
 * @author Ruan
 */
public class ControllerFormPessoa {

    private ViewFormPessoa view;
    private Pessoa model;
    private boolean modelAlreadyExists = false;
    private boolean readOnly = false;

    public ControllerFormPessoa(Pessoa model, boolean readOnly) {
        this(model);
        this.readOnly = readOnly;
    }
    
    public ControllerFormPessoa(Pessoa model) {
        this();
        this.model = model;
        this.modelAlreadyExists = !model.getCpf().isEmpty();
    }

    public ControllerFormPessoa() {
        this.view = new ViewFormPessoa();
        this.addActionListeners();
    }

    public void abreTela() {
        this.beanView();
        if (this.readOnly) {
            this.disableFields();
        }
        this.getView().setVisible(true);
    }

    private void beanView() {
        this.getView().getFieldCpf().setText(this.getModel().getCpf());
        this.getView().getFieldNome().setText(this.getModel().getNome());
        this.getView().getFieldEndereco().setText(this.getModel().getEndereco());
    }
    
    private void disableFields() {
        this.getView().getFieldCpf().setEnabled(false);
        this.getView().getFieldNome().setEnabled(false);
        this.getView().getFieldEndereco().setEnabled(false);
        this.getView().getButtonConfirmar().setEnabled(false);
    }

    public ViewFormPessoa getView() {
        return view;
    }

    public Pessoa getModel() {
        if (this.model == null) {
            this.model = new Pessoa();
        }
        return model;
    }

    public void setModel(Pessoa model) {
        this.model = model;
    }

    private void addActionListeners() {
        this.addActionListenerButtonConfirmar();
        this.addActionListenerButtonCancelar();
    }

    private void addActionListenerButtonConfirmar() {
        this.getView().getButtonConfirmar().addActionListener((ActionEvent actionEvent) -> {
            Response retorno = this.modelAlreadyExists ? this.update() : this.insert();
            
            MessageDialog.show(this.getView(), retorno);
        });
    }

    private void addActionListenerButtonCancelar() {
        this.getView().getButtonCancelar().addActionListener((ActionEvent actionEvent) -> {
            this.getView().dispose();
        });
    }

    private Response update() {
        Response retorno;
        try {
            Socket socket = (new Connection(5000)).getInstanceSocket();
            MessageParser<Pessoa> messageParser = new UpdateMessageParser<>(this.getModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            retorno = new Response(true, "");
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }

    private Response insert() {
        Response retorno;
        try {
            Socket socket = (new Connection(5000)).getInstanceSocket();
            MessageParser<Pessoa> messageParser = new InsertMessageParser<>(this.getModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            retorno = new Response(true, "Pessoa inserida com sucesso”");
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }

}
