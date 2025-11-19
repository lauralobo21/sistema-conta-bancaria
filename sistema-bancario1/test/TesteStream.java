package test;

// Imports das suas classes de modelo (pacote clients)
import clients.Cliente;
import clients.Conta;
import clients.ContaCorrente;
import clients.ContaPoupanca;
import services.ClienteInputStream;
import services.ClienteOutPutStream;

// Imports de Rede e IO
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.FileOutputStream; 
import java.io.FileInputStream;  
import java.util.Arrays;         

public class TesteStream {

    public static void main(String[] args) {

        System.out.println("Criando objetos de teste...");
        // Cria os objetos Cliente
        Cliente cli1 = new Cliente("Gaby Costa", "111.111.111-11", 20);
        Cliente cli2 = new Cliente("Carlos Moura", "222.222.222-22", 30);
        Cliente cli3 = new Cliente("Daniela Reis", "333.333.333-33", 40);

        Conta[] contasParaEnviar = new Conta[3];
        
        // Preenche o array com diferentes tipos de conta
        contasParaEnviar[0] = new ContaCorrente("001-CC", cli1, 1000.0);
        contasParaEnviar[1] = new ContaPoupanca("002-PP", cli2, 51); 
        contasParaEnviar[2] = new ContaCorrente("003-CC", cli3, 500.0);

        // Define o saldo da primeira conta para testar
        contasParaEnviar[0].depositar(500.0);
        System.out.println("Dados de teste criados.");
        
        String nomeArquivo = "contas.dat"; //2.b.ii
        System.out.println("\n--- Testando escrita em arquivo (" + nomeArquivo + ") ---");
        try (FileOutputStream fos = new FileOutputStream(nomeArquivo);
             ClienteOutPutStream outFile = new ClienteOutPutStream(fos)) { //passa o stream de arquivo para o cliente
            
            outFile.writeContas(contasParaEnviar, contasParaEnviar.length); 
            System.out.println("Escrita em arquivo concluída.");
        
        } catch (IOException e) { e.printStackTrace(); }

        
        System.out.println("\n--- Testando leitura de arquivo (" + nomeArquivo + ") ---");
        try (FileInputStream fis = new FileInputStream(nomeArquivo); //3.c
             ClienteInputStream inFile = new ClienteInputStream(fis)) {
            
            Conta[] contasLidas = inFile.readContas(); 
            System.out.println("Leitura do arquivo concluída.");
            System.out.println("Contas lidas do arquivo:");
            for(Conta c : contasLidas) {
                // Usa o toString() que definimos na classe Conta
                System.out.println("  > " + c.toString()); 
            }
        
        } catch (IOException e) { e.printStackTrace(); }

        System.out.println("\n--- Testando escrita no Console (pode gerar 'lixo' binário) ---");
        try (ClienteOutPutStream outStd = new ClienteOutPutStream(System.out)) { //2.b.i
            outStd.writeContas(contasParaEnviar, contasParaEnviar.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n(Escrita no console concluída)");

        String host = "localhost"; //2.b.iii
        int porta = 12345;
        System.out.println("\n--- Testando envio via Rede (Socket) para " + host + ":" + porta + " ---");

        try (
            Socket socket = new Socket(host, porta);
            ClienteOutPutStream outSocket = new ClienteOutPutStream(socket.getOutputStream()); //obtem o stream de conexão
        ) {
            
            System.out.println("CLIENTE: Conexão estabelecida. Enviando dados...");

            outSocket.writeContas(contasParaEnviar, contasParaEnviar.length); //empacota e envia para a rede
            
            System.out.println("CLIENTE: Dados enviados com sucesso!");

        } catch (UnknownHostException e) {
            System.err.println("Erro: Host desconhecido (" + host + ")");
        } catch (IOException e) {
            System.err.println("Erro de E/S ao conectar ou enviar dados: " + e.getMessage());
        }
    }
}