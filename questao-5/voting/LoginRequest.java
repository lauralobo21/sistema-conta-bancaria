package voting;

public class LoginRequest {
    String cpf;
    String senha;

    // Construtor vazio
    public LoginRequest() {
    }

    // Construtor para facilitar
    public LoginRequest(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    // Getters
    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    // Setters
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}