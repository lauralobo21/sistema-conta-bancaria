package voting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson; // <-- ADICIONE ESTA LINHA DE VOLTA
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import voting.*;

public class Votacao {
    public static void main(String[] args) {
        String host = "localhost";
        int portaTCP = 9090;
        Gson gson = new Gson(); // <-- ADICIONE ESTA LINHA DE VOLTA
        
        new Thread(new OuvinteMulticast()).start();

        try (
            Socket socket = new Socket(host, portaTCP);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("--- Sistema de Votação ---");
            
            System.out.print("Digite seu CPF para logar: ");
            String cpf = scanner.nextLine();
            
            LoginRequest login = new LoginRequest(cpf, "");
            String jsonPayloadLogin = gson.toJson(login); // <-- Precisa do 'gson'
            Mensagem msgLogin = new Mensagem("LOGIN", jsonPayloadLogin);
            String jsonMsgLogin = gson.toJson(msgLogin); // <-- Precisa do 'gson'
            
            out.println(jsonMsgLogin);
            
            Mensagem respostaLogin = gson.fromJson(in.readLine(), Mensagem.class); // <-- Precisa
            
            if (!respostaLogin.getTipo().equals("OK")) {
                System.out.println("Falha no login: " + respostaLogin.getTipo());
                socket.close();
                return;
            }
            
            System.out.println("Login com sucesso! Candidatos:");
            
            Type tipoListaCandidatos = new TypeToken<List<Candidato>>(){}.getType();
            List<Candidato> candidatos = gson.fromJson(respostaLogin.getPayload(), tipoListaCandidatos); // <-- Precisa
            
            for (Candidato c : candidatos) {
                System.out.println("  ID: " + c.getId() + " - Nome: " + c.getNome());
            }
            
            System.out.print("Digite o ID do candidato para votar: ");
            int idVoto = Integer.parseInt(scanner.nextLine());
            
            Voto voto = new Voto(idVoto);
            Mensagem msgVoto = new Mensagem("VOTO", gson.toJson(voto)); // <-- Precisa
            
            out.println(gson.toJson(msgVoto)); // <-- Precisa
            
            Mensagem respostaVoto = gson.fromJson(in.readLine(), Mensagem.class); // <-- Precisa
            System.out.println("Servidor: " + respostaVoto.getTipo());
            
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}