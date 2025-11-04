package services;

import clients.Cliente;
import clients.Conta;
import clients.ContaCorrente;
import clients.ContaPoupanca;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClienteOutPutStream extends FilterOutputStream {

    private DataOutputStream dos;

    public ClienteOutPutStream(OutputStream out) {
        super(out);
        this.dos = new DataOutputStream(out);
    }

    public void writeConta(Conta c) throws IOException {
        
        // Identificador de Tipo (String)
        dos.writeUTF(c.getTipoConta());

        // Dados da Superclasse Conta (Comuns)
        dos.writeUTF(c.getNumeroConta());
        dos.writeDouble(c.getSaldo()); 

        // Dados do Cliente (Titular)
        Cliente titular = c.getTitular(); // Usa o getter da sua classe Conta
        dos.writeUTF(titular.getNome());
        dos.writeUTF(titular.getCpf());
        dos.writeInt(titular.getIdade());

        // Dados Específicos de cada tipo
        if (c instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) c;
            dos.writeDouble(cc.getLimiteChequeEspecial()); 
        } else if (c instanceof ContaPoupanca) {
            ContaPoupanca cp = (ContaPoupanca) c;
            dos.writeInt(cp.getVariacao());
        }
    }

    /**
     * array de Contas.
     */
    public void writeContas(Conta[] contas, int numContas) throws IOException {
        dos.writeInt(numContas);
        for (int i = 0; i < numContas; i++) {
            writeConta(contas[i]);
        }
        dos.flush(); // força o dos a enviar quaisquer bytes que esteja guardado no buffer
    }
}