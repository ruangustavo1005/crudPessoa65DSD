package model;

import java.util.ArrayList;

public class Dao {
    
    private static Dao instance;
    
    protected ArrayList<Pessoa> pessoas;
    
    public Dao() {
        this.pessoas = new ArrayList<>();
        this.pessoas.add(new Pessoa("teste", "123", "casa"));
        this.pessoas.add(new Pessoa("testa", "456", "casa 2"));
    }

    public static Dao getInstance() {
        if(instance == null) {
            instance = new Dao();
        }
        return instance;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }
    
}