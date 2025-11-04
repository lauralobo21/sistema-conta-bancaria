package voting;

public class Voto {
    int idCandidato;

    // Construtor vazio
    public Voto() {
    }

    // Construtor para facilitar
    public Voto(int idCandidato) {
        this.idCandidato = idCandidato;
    }

    // Getter
    public int getIdCandidato() {
        return idCandidato;
    }

    // Setter
    public void setIdCandidato(int idCandidato) {
        this.idCandidato = idCandidato;
    }
}