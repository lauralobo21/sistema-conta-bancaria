package services;

import clients.Cliente;
import clients.Conta;
import clients.ContaCorrente;
import clients.ContaPoupanca;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClienteInputStream extends FilterInputStream {

    private DataInputStream dis;

    public ClienteInputStream(InputStream in) {
        super(in);
        this.dis = new DataInputStream(in); //ajuda a ler os dados a partir dos bytes
    }

    public Conta readConta() throws IOException {
        
        // 1. Identificador de Tipo (String)
        String tipoConta = dis.readUTF();

        // 2. Dados da Superclasse Conta (Comuns)
        String numeroConta = dis.readUTF();
        double saldo = dis.readDouble(); // <-- ADICIONADO

        // 3. Dados do Cliente (Titular)
        String nome = dis.readUTF();
        String cpf = dis.readUTF();
        int idade = dis.readInt();
        // Recria o objeto Cliente
        Cliente titular = new Cliente(nome, cpf, idade); 

        // 4. Dados Específicos e Criação do Objeto
        Conta contaRecebida = null;

        if (tipoConta.equals("ContaCorrente")) {
            double limite = dis.readDouble();
            // Cria a ContaCorrente
            contaRecebida = new ContaCorrente(numeroConta, titular, limite);
            
        } else if (tipoConta.equals("ContaPoupanca")) {
            int variacao = dis.readInt();
            // Cria a ContaPoupanca
            contaRecebida = new ContaPoupanca(numeroConta, titular, variacao);
        }

        // (O construtor da Conta define o saldo como 0.0,
        // então precisamos definir o saldo real que veio pela rede)
        if (contaRecebida != null) {
            contaRecebida.setSaldo(saldo);
        }

        return contaRecebida;
    }

    /**
     * Lê um array de Contas do stream.
     */
    public Conta[] readContas() throws IOException {
        int numContas = dis.readInt();
        Conta[] contas = new Conta[numContas];
        for (int i = 0; i < numContas; i++) {
            contas[i] = readConta();
        }
        return contas;
    }
}