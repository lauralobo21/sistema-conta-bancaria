package clients;


public class ContaPoupanca extends Conta {

    // Atributo específico desta classe (exemplo)
    private int variacao; // Ex: 51, 01, etc.

    /**
     * Construtor da ContaPoupanca.
     * /**
     * 
     * @param numeroConta O número da conta (vem da superclasse)
     * @param titular O objeto Cliente (vem da superclasse)
     * @param variacao variacao desta conta
     */
     
    public ContaPoupanca(String numeroConta, Cliente titular, int variacao) {
        // 1. Chama o construtor da classe "mãe" (Conta)
        super(numeroConta, titular);
        
        // 2. Define o atributo específico
        this.variacao = variacao;
    }

    /**
     * Implementação OBRIGATÓRIA.
     * Note como o identificador é diferente.
     */
    @Override
    public String getTipoConta() {
        return "ContaPoupanca"; // Identificador único
    }

    // Getter para o atributo específico
    public int getVariacao() {
        return variacao;
    }

    // Opcional: Sobrescrever o 'toString' para incluir a variação
    @Override
    public String toString() {
        return super.toString() + 
               ", variacao=" + variacao + 
               '}';
    }
}