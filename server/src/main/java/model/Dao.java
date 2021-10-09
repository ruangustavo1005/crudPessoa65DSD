package model;

import java.util.ArrayList;

public class Dao {
    
    private static Dao instance;
    
    protected ArrayList<Pessoa> pessoas;
    
    public Dao() {
        this.pessoas = new ArrayList<>();
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