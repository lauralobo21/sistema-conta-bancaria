package voting;

public class NotaInformativa {
    String mensagem;

    public NotaInformativa() {
    }

    public NotaInformativa(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}