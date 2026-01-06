package model;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nome;
    private List<Conta> contas;

    public Banco(String nome) {
        this.nome = nome;
        this.contas = new ArrayList<>();
    }

    public void adicionarConta(Conta c) {
        this.contas.add(c);
    }

    public Conta buscarConta(String numero) {
        for (Conta c : contas) {
            if (c.getNumeroConta().equals(numero)) {
                return c;
            }
        }
        return null;
    }

    // Método para teste de listagem
    public List<Conta> getContas() {
        return contas;
    }
}