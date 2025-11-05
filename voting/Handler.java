package voting;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import voting.*; // Importa Candidato, Mensagem, LoginRequest, Voto, NotaInformativa

public class Handler implements Runnable {
    private Socket clientSocket;
    private GerenciadorVotacao gerenciador;
    private Gson gson = new Gson();
    private String cpfEleitorLogado = null; // Para saber quem é este cliente

    public Handler(Socket socket, GerenciadorVotacao gerenciador) {
        this.clientSocket = socket;
        this.gerenciador = gerenciador;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String linhaJson;
            while ((linhaJson = in.readLine()) != null) {
                
                Mensagem msgRecebida = null;
                Mensagem msgResposta = null;

                try {
                    // 1. Converte o JSON recebido na classe "envelope"
                    msgRecebida = gson.fromJson(linhaJson, Mensagem.class);
                } catch (Exception e) {
                    System.err.println("Erro ao decodificar JSON: " + linhaJson);
                    msgResposta = new Mensagem("ERRO: JSON inválido", "");
                    out.println(gson.toJson(msgResposta));
                    continue; // Pula para a próxima iteração do loop
                }

                // 2. Decide o que fazer baseado no 'tipo'
                switch (msgRecebida.getTipo()) {
                    
                    case "LOGIN":
                        LoginRequest login = gson.fromJson(msgRecebida.getPayload(), LoginRequest.class);
                        String statusLogin = gerenciador.tentarLogin(login.getCpf());
                        
                        String payloadLogin = "";
                        if (statusLogin.equals("OK")) {
                            this.cpfEleitorLogado = login.getCpf(); // Guarda quem logou
                            // Converte a lista de candidatos para JSON
                            payloadLogin = gson.toJson(gerenciador.getCandidatos());
                        }
                        msgResposta = new Mensagem(statusLogin, payloadLogin);
                        break;
                    
                    case "VOTO":
                        if (this.cpfEleitorLogado == null) {
                            msgResposta = new Mensagem("ERRO: Não logado", "");
                        } else {
                            Voto v = gson.fromJson(msgRecebida.getPayload(), Voto.class);
                            String statusVoto = gerenciador.registrarVoto(this.cpfEleitorLogado, v.getIdCandidato());
                            msgResposta = new Mensagem(statusVoto, "");
                        }
                        break;
                    
                    // --- Casos do Administrador ---
                    case "ADD_CANDIDATO":
                        Candidato c = gson.fromJson(msgRecebida.getPayload(), Candidato.class);
                        String statusAdd = gerenciador.addCandidato(c.getNome(), c.getId());
                        msgResposta = new Mensagem(statusAdd, "");
                        break;

                    case "REM_CANDIDATO":
                        Voto votoParaRemover = gson.fromJson(msgRecebida.getPayload(), Voto.class);
                        String statusRemocao = gerenciador.removerCandidato(votoParaRemover.getIdCandidato());
                        msgResposta = new Mensagem(statusRemocao, "");
                        break;

                    case "ENVIAR_NOTA":
                        NotaInformativa nota = gson.fromJson(msgRecebida.getPayload(), NotaInformativa.class);
                        gerenciador.enviarNotaMulticast(nota.getMensagem());
                        msgResposta = new Mensagem("OK: Nota enviada", "");
                        break;
                        
                    default:
                        msgResposta = new Mensagem("ERRO: Tipo de mensagem desconhecido", "");
                        break;
                }
                
                out.println(gson.toJson(msgResposta));
            }
        } catch (Exception e) {
            System.out.println("Cliente desconectou: " + clientSocket.getInetAddress());
        }
    }
}