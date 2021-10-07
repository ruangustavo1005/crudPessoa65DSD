package classes;

public class Insert extends Comando {

    @Override
    protected void execute(Pessoa pessoa) {
        Dao.getInstance().getPessoas().add(pessoa);
    }
    
    @Override
    protected String returnMessage() {
        return "";
    }
    
}