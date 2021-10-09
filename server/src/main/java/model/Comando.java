package model;

import java.util.ArrayList;

abstract public class Comando {
    
    public static final String INSERT = "INSERT";
    public static final String DELETE = "DELETE";
    public static final String GET    = "GET";
    public static final String UPDATE = "UPDATE";
    
    protected ArrayList<Pessoa> pessoas;
    
    protected boolean success;
    
    public Comando() {
        this.pessoas = new ArrayList<>();
    }
    
    protected abstract void execute(Pessoa pessoa);
    
    protected abstract String returnMessage();

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
}