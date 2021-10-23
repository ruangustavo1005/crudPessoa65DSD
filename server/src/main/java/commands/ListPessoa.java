package commands;

import model.Dao;
import model.Pessoa;

public class ListPessoa extends Comando {

    @Override
    public void execute(String[] args) {}

    @Override
    public String returnMessage() {
        String returnMsg = "" + Dao.getInstance().getPessoas().size() + "\n";
        for(Pessoa p : Dao.getInstance().getPessoas()) {
            returnMsg = returnMsg + p.toString() + "\n";
        }
        return returnMsg;
    }
    
}