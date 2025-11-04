package voting;

import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList; // Importe esta
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import voting.Candidato;
import voting.NotaInformativa;

public class GerenciadorVotacao {
    
    private Map<String, Integer> votosRecebidos;
    private List<Candidato> candidatos;
    private boolean votacaoAberta = true;
    private Gson gson = new Gson();

   public GerenciadorVotacao() {
        this.votosRecebidos = new ConcurrentHashMap<>();
        
        this.candidatos = new ArrayList<>();
        this.candidatos.add(new Candidato(1, "Ana"));
        this.candidatos.add(new Candidato(2, "Bruno"));
        this.candidatos.add(new Candidato(3, "Carla"));
        this.candidatos.add(new Candidato(4, "Diego"));
        this.candidatos.add(new Candidato(5, "Elisa"));
        
        long prazoEmMinutos = 3;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(this::encerrarVotacao, prazoEmMinutos, TimeUnit.MINUTES);
        System.out.println("Votação iniciada. Término em " + prazoEmMinutos + " minutos.");
    }

    // Método 'synchronized' para thread-safety
    public synchronized String tentarLogin(String cpf) {
        if (!votacaoAberta) {
            return "ERRO: Votação encerrada";}
        if (votosRecebidos.containsKey(cpf)) {
            return "ERRO: Eleitor já votou";}

        return "OK";
    }

    public synchronized String registrarVoto(String cpf, int idCandidato) {
        if (!votacaoAberta) {
            return "ERRO: Votação encerrada";}

        if (votosRecebidos.containsKey(cpf)) {
            return "ERRO: Eleitor já votou";}
        
        // Verifica se o candidato existe
        boolean candidatoValido = false;
        for (Candidato c : candidatos) {
            if (c.getId() == idCandidato) {
                candidatoValido = true;
                break;
            }
        }
        if (!candidatoValido) {
            return "ERRO: Candidato inexistente";
        }

        votosRecebidos.put(cpf, idCandidato);
        System.out.println("Voto registrado: " + cpf + " -> " + idCandidato);
        return "OK: Voto computado";
    }

    public synchronized List<Candidato> getCandidatos() {
        return this.candidatos;
    }
    
    public synchronized String addCandidato(String nome, int id) {
        this.candidatos.add(new Candidato(id, nome));
        System.out.println("Candidato adicionado: " + nome);
        return "OK: Candidato adicionado";
    }

    // Envio do Multicast (UDP)
    public void enviarNotaMulticast(String textoNota) {
        try (DatagramSocket socket = new DatagramSocket()) {
            NotaInformativa nota = new NotaInformativa(textoNota);
            String jsonNota = gson.toJson(nota);
            byte[] buffer = jsonNota.getBytes();
            
            InetAddress group = InetAddress.getByName("230.0.0.0");
            int port = 6789;
            
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
            socket.send(packet);
            System.out.println("Nota informativa enviada via Multicast.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public synchronized void encerrarVotacao() {
        this.votacaoAberta = false;
        System.out.println("--- VOTAÇÃO ENCERRADA ---");
        // Lógica para calcular e imprimir os resultados
        Map<Integer, Integer> contagem = new ConcurrentHashMap<>();
        for (Candidato c : candidatos) {
            contagem.put(c.getId(), 0);
        }
        
        for (int idVotado : votosRecebidos.values()) {
            contagem.put(idVotado, contagem.get(idVotado) + 1);
        }
        
        System.out.println("Resultados:");
        contagem.forEach((id, total) -> {
            System.out.println("Candidato ID " + id + ": " + total + " votos");
        });
    }
    public synchronized String removerCandidato(int idCandidato) {
        boolean removido = this.candidatos.removeIf(candidato -> candidato.getId() == idCandidato);

        if (removido) {
            System.out.println("Candidato removido: ID " + idCandidato);
            return "OK: Candidato removido";
        } else {
            return "ERRO: Candidato com ID " + idCandidato + " não foi encontrado";
        }
    }
}