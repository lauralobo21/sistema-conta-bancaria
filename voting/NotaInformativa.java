package voting;

public class NotaInformativa {
    String mensagem;

    // Construtor vazio
    public NotaInformativa() {
    }

    // Construtor para facilitar
    public NotaInformativa(String mensagem) {
        this.mensagem = mensagem;
    }

    // Getter
    public String getMensagem() {
        return mensagem;
    }

    // Setter
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}