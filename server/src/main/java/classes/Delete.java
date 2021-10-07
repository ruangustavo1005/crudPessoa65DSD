package classes;

public class Delete extends Comando {

    @Override
    protected void execute(Pessoa pessoa) {
        Dao.getInstance().getPessoas().remove(pessoa);
    }

    @Override
    protected String returnMessage() {
        return "";
    }
    
}