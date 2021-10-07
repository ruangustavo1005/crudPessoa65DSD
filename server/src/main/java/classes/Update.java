package classes;

public class Update extends Comando {

    @Override
    protected void execute(Pessoa pessoa) {
        for(Pessoa p : Dao.getInstance().getPessoas()) {
            if(p.getCpf().equals(pessoa.getCpf())) {
                p.setNome(pessoa.getNome());
                p.setEndereco(pessoa.getEndereco());
            }
        }
    }

    @Override
    protected String returnMessage() {
        return "Pessoa atualizada com sucesso";
    }
    
}
