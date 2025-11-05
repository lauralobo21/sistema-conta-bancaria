package voting;
public class Candidato {
    int id;
    String nome;
    
    public Candidato() {
    }

    // Construtor para facilitar a criação
    public Candidato(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

