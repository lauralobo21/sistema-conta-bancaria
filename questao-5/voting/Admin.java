package voting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;
import voting.*; // Importa os nossos POJOs

public class Admin {

    public static void main(String[] args) {
        String host = "localhost";
        int portaTCP = 9090; // A mesma porta do ServidorTCP
        Gson gson = new Gson();

        try (
            Socket socket = new Socket(host, portaTCP);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("--- Painel de Administrador ---");
            System.out.println("O que você deseja fazer?");
            System.out.println("1: Adicionar Candidato");
            System.out.println("2: Enviar Nota Informativa");
            System.out.println("3: Remover Candidato");
            String escolha = scanner.nextLine();

            Mensagem msgEnvio = null;

            if (escolha.equals("1")) {
                // --- LÓGICA DE ADICIONAR CANDIDATO ---
                System.out.print("Digite o NOME do novo candidato: ");
                String nome = scanner.nextLine();
                System.out.print("Digite o ID (número) do novo candidato: ");
                int id = Integer.parseInt(scanner.nextLine());

                Candidato novoCandidato = new Candidato(id, nome);
                // 2. Converte o POJO para um payload JSON
                String payload = gson.toJson(novoCandidato);
                // 3. Cria o "envelope" da mensagem
                msgEnvio = new Mensagem("ADD_CANDIDATO", payload);

            } else if (escolha.equals("2")) {
                // --- LÓGICA DE ENVIAR NOTA ---
                System.out.print("Digite a nota informativa a ser enviada (UDP): ");
                String textoNota = scanner.nextLine();

                // 1. Cria o POJO da nota
                NotaInformativa nota = new NotaInformativa(textoNota);
                String payload = gson.toJson(nota);
                msgEnvio = new Mensagem("ENVIAR_NOTA", payload);

            } else if (escolha.equals("3")) {
                System.out.print("Digite o ID (número) do candidato a ser REMOVIDO: ");
                int idRemover = Integer.parseInt(scanner.nextLine());

                Voto votoParaRemover = new Voto(idRemover);
                String payload = gson.toJson(votoParaRemover);
                msgEnvio = new Mensagem("REM_CANDIDATO", payload);
            } else {
                System.out.println("Escolha inválida.");
                socket.close();
                return;
            }

            // 4. Converte o envelope para JSON e envia
            out.println(gson.toJson(msgEnvio));

            // 5. Recebe e imprime a resposta do servidor
            Mensagem resposta = gson.fromJson(in.readLine(), Mensagem.class);
            System.out.println("Resposta do Servidor: " + resposta.getTipo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}