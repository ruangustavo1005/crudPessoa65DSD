package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import message.InsertMessageParser;
import message.MessageParser;
import message.UpdateMessageParser;
import model.Empresa;
import util.Connection;
import util.DateUtils;
import util.MessageDialog;
import util.Response;
import view.ViewFormEmpresa;

/**
 * @author Ruan
 */
public class ControllerFormEmpresa extends Controller {

    private ViewFormEmpresa view;
    private Empresa model;
    private boolean modelAlreadyExists = false;
    private boolean readOnly = false;

    public ControllerFormEmpresa(Controller caller, Empresa model, boolean readOnly) {
        this(caller, model);
        this.readOnly = readOnly;
    }
    
    public ControllerFormEmpresa(Controller caller, Empresa model) {
        this(caller);
        this.model = model;
        this.modelAlreadyExists = !model.getCnpj().isEmpty();
    }

    public ControllerFormEmpresa(Controller caller) {
        super(caller);
        this.view = new ViewFormEmpresa();
        this.addActionListeners();
    }

    public void abreTela() {
        this.beanView();
        if (this.readOnly) {
            this.disableFields();
        }
        if (this.getModel().getCnpj()!= null) {
            this.getView().getFieldCnpj().setEnabled(false);
        }
        this.getView().setVisible(true);
    }

    private void beanView() {
        this.getView().getFieldCnpj().setText(this.getModel().getCnpj());
        this.getView().getFieldRazao().setText(this.getModel().getRazao());
        this.getView().getFieldDataCriacao().setText(this.getModel().getDataCriacaoAsString());
    }
    
    private void beanModel() {
        this.getModel().setCnpj(this.getView().getFieldCnpj().getText())
                       .setRazao(this.getView().getFieldRazao().getText())
                       .setDataCriacao(DateUtils.stringToDate(this.getView().getFieldDataCriacao().getText()));
    }
    
    private void disableFields() {
        this.getView().getFieldCnpj().setEnabled(false);
        this.getView().getFieldRazao().setEnabled(false);
        this.getView().getFieldDataCriacao().setEnabled(false);
        this.getView().getButtonConfirmar().setEnabled(false);
    }

    public ViewFormEmpresa getView() {
        return view;
    }

    public Empresa getModel() {
        if (this.model == null) {
            this.model = new Empresa();
        }
        return model;
    }

    public void setModel(Empresa model) {
        this.model = model;
    }

    private void addActionListeners() {
        this.addActionListenerButtonConfirmar();
        this.addActionListenerButtonCancelar();
    }

    private void addActionListenerButtonConfirmar() {
        this.getView().getButtonConfirmar().addActionListener((ActionEvent actionEvent) -> {
            this.beanModel();
            
            Empresa empresa = this.getModel();
            int i = 0;
            if (this.getCaller() instanceof ControllerIndex) {
                i = ((ControllerGridEmpresa) this.getCaller()).getView().getTableModelEmpresa().getData().indexOf(empresa);
            }
            
            Response retorno = this.modelAlreadyExists ? this.update() : this.insert();
            
            MessageDialog.show(this.getView(), retorno);
            
            if (this.getCaller() instanceof ControllerIndex) {
                if (this.modelAlreadyExists) {
                    ((ControllerGridEmpresa) this.getCaller()).getView().getTableModelEmpresa().updateData(i, empresa);
                }
                else {
                    ((ControllerGridEmpresa) this.getCaller()).getView().getTableModelEmpresa().addData(empresa);
                }
            }
            
            this.getView().dispose();
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
            Socket socket = (new Connection()).getInstanceSocket();
            
            MessageParser<Empresa> messageParser = new UpdateMessageParser<>(this.getModel());
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

    private Response insert() {
        Response retorno;
        try {
            Socket socket = (new Connection()).getInstanceSocket();
            
            MessageParser<Empresa> messageParser = new InsertMessageParser<>(this.getModel());
            socket.getOutputStream().write(messageParser.getMessageBytes());
            
            retorno = new Response(true, "Empresa inserida com sucesso");
        }
        catch (IOException ex) {
            retorno = new Response(false, "Houve um erro ao tentar conectar com o servidor");
        }
        return retorno;
    }

}
