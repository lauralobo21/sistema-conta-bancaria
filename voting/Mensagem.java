package voting;

public class Mensagem {
    String tipo;
    String payload; // O JSON do objeto real (ex: um Voto, um Candidato)

    // Construtor vazio
    public Mensagem() {
    }

    // Construtor para facilitar
    public Mensagem(String tipo, String payload) {
        this.tipo = tipo;
        this.payload = payload;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public String getPayload() {
        return payload;
    }

    // Setters
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}