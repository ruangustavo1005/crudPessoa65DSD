package classes;

public class Update extends Comando {

    @Override
    protected void execute(Pessoa pessoa) {
        boolean achou = false;
        for(Pessoa p : Dao.getInstance().getPessoas()) {
            if(p.getCpf().equals(pessoa.getCpf())) {
                achou = true;
                p.setNome(pessoa.getNome());
                p.setEndereco(pessoa.getEndereco());
                this.setSuccess(true);
            }
        }
        if(!achou) {
            this.setSuccess(false);
        }
    }

    @Override
    protected String returnMessage() {
        if(Dao.getInstance().getPessoas().isEmpty()) {
            return "Sem pessoas cadastradas";
        }
        if(this.success) {
            return "Pessoa atualizada com sucesso";
        } 
        return "Pessoa não encontrada";
    }
    
}
