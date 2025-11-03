package server;


import services.ClienteInputStream;

// Importa os modelos do pacote 'clients'
import clients.Cliente;
import clients.Conta;
import clients.ContaCorrente;
import clients.ContaPoupanca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Este é o programa SERVIDOR atualizado (Questão 2.b.iii e 3.d).
 * Ele agora "ouve" uma conexão e usa o 'ClienteInputStream' 
 * para ler um array de OBJETOS CONTA (ContaCorrente, ContaPoupanca, etc.).
 */
public class Servidor {

    public static void main(String[] args) {
        // Define a porta que o servidor vai "ouvir"
        int porta = 12345;

        try (
            // 1. Cria o "Serviço de Atendimento" na porta 12345
            ServerSocket serverSocket = new ServerSocket(porta)
        ) {
            
            System.out.println("===================================");
            System.out.println("SERVIDOR INICIADO NA PORTA " + porta);
            System.out.println("Aguardando conexão do Cliente...");
            System.out.println("===================================");

            // 2. Fica "travado" aqui até um cliente se conectar (o seu TesteStream)
            Socket clientSocket = serverSocket.accept();
            
            System.out.println("\n>>> Cliente conectado! (" + clientSocket.getInetAddress().getHostAddress() + ")");

            // 3. Usa o CLIENTE INPUT STREAM para ler os dados das CONTAS
            //    (O nome da classe continua ClienteInputStream, mas agora ela lê Contas)
            ClienteInputStream leitorDeContas = new ClienteInputStream(clientSocket.getInputStream());

            // 4. Chama o método que lê o array de Contas
            System.out.println("Lendo dados das Contas do cliente...");
            
            // *** MUDANÇA PRINCIPAL AQUI ***
            // Chama o novo método readContas()
            Conta[] contasRecebidas = leitorDeContas.readContas();

            // 5. Mostra os dados recebidos para provar que funcionou!
            System.out.println(">>> DADOS RECEBIDOS COM SUCESSO <<<");
            
            // Itera sobre as contas que acabaram de chegar pela rede
            for (Conta c : contasRecebidas) {
                if (c != null) {
                    // Pega o titular usando o getter da sua classe Conta
                    Cliente titular = c.getTitular(); 
                    
                    System.out.println("  ---------------------------------");
                    // Mostra o tipo da conta (ContaCorrente, ContaPoupanca, etc.)
                    System.out.println("  - Tipo da Conta: " + c.getTipoConta());
                    System.out.println("  - Numero: " + c.getNumeroConta());
                    System.out.println("  - Saldo Recebido: " + c.getSaldo()); // <-- NOVO
                    System.out.println("  - Titular: " + titular.getNome() + 
                                       " (CPF: " + titular.getCpf() + 
                                       ", Idade: " + titular.getIdade() + ")");

                    // Verifica se é ContaCorrente para mostrar o dado específico
                    if (c instanceof ContaCorrente) {
                        ContaCorrente cc = (ContaCorrente) c;
                        System.out.println("  - Limite C. Especial: " + cc.getLimiteChequeEspecial());
                    }
                    // Verifica se é ContaPoupanca para mostrar o dado específico
                    else if (c instanceof ContaPoupanca) {
                        ContaPoupanca cp = (ContaPoupanca) c;
                        System.out.println("  - Variacao: " + cp.getVariacao());
                    }
                }
            }
            System.out.println("  ---------------------------------");


            // 6. Fecha tudo
            leitorDeContas.close();
            clientSocket.close();
            System.out.println("\nConexão com o cliente encerrada.");

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}