package commands;

abstract public class Comando {
    
    public static final String INSERT = "INSERT";
    public static final String DELETE = "DELETE";
    public static final String GET    = "GET";
    public static final String UPDATE = "UPDATE";
    public static final String LIST = "LIST";
    
    protected boolean success;
    
    public abstract void execute(String[] args);
    
    public abstract String returnMessage();

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
}