package voting;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    public static void main(String[] args) {
        int portaTCP = 9090; // Porta para o TCP
        
        try {
            // 1. Cria UMA ÚNICA instância do "cérebro"
            GerenciadorVotacao gerenciador = new GerenciadorVotacao();
            
            ServerSocket serverSocket = new ServerSocket(portaTCP);
            System.out.println("Servidor TCP aguardando na porta " + portaTCP);

            // 2. Loop infinito para aceitar clientes
            while (true) {
                // 3. Trava aqui até um cliente se conectar
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                
                // 4. Cria um "Cuidador" (Handler) para este cliente
                Handler handler = new Handler(clientSocket, gerenciador);
                
                // 5. Inicia o "Cuidador" em uma NOVA THREAD 
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}