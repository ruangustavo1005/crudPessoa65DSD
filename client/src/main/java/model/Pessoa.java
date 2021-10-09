package model;

import interfaces.Transmissible;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruan
 */
public class Pessoa implements Transmissible {

    private String cpf;
    private String nome;
    private String endereco;

    public Pessoa() {
        
    }

    public Pessoa(String cpf, String nome, String endereco) {
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public List<String> getInfoInsert() {
        List<String> info = new ArrayList<>();
        info.add(this.getCpf());
        info.add(this.getNome());
        info.add(this.getEndereco());
        return info;
    }

    @Override
    public List<String> getInfoUpdate() {
        List<String> info = new ArrayList<>();
        info.add(this.getCpf());
        info.add(this.getNome());
        info.add(this.getEndereco());
        return info;
    }

    @Override
    public List<String> getInfoGet() {
        List<String> info = new ArrayList<>();
        info.add(this.getCpf());
        return info;
    }

    @Override
    public List<String> getInfoDelete() {
        List<String> info = new ArrayList<>();
        info.add(this.getCpf());
        return info;
    }

    @Override
    public List<String> getInfoList() {
        List<String> info = new ArrayList<>();
        return info;
    }
    
}