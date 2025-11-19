package voting;

public class Mensagem {
    String tipo;
    String payload; // O JSON do objeto real (ex: um Voto, um Candidato)

    public Mensagem() {
    }

    public Mensagem(String tipo, String payload) {
        this.tipo = tipo;
        this.payload = payload;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPayload() {
        return payload;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}