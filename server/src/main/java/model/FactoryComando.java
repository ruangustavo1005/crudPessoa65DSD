package model;

public class FactoryComando {
    
    public Comando getComando(String comando) throws Exception {
        switch(comando) {
            case Comando.INSERT:
                return new Insert();
            case Comando.DELETE: 
                return new Delete();
            case Comando.UPDATE:
                return new Update();
            default: 
                throw new Exception("Comando não identificado.");
        }
    }
    
}
