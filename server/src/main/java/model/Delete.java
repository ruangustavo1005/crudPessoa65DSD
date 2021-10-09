package model;

public class Delete extends Comando {

    @Override
    protected void execute(Pessoa pessoa) {
        this.success = false;
        for(Pessoa p : Dao.getInstance().getPessoas()) {
            if(pessoa.getCpf().equals(p.getCpf())) {
                this.success = true;
                Dao.getInstance().getPessoas().remove(p);
            }
        }
    }

    @Override
    protected String returnMessage() {
        if(Dao.getInstance().getPessoas().isEmpty()) {
            return "Sem pessoas cadastradas";
        }
        if(this.success) {
            return "Pessoa removida com sucesso";
        }
        return "Pessoa não encontrada";
    }
    
}