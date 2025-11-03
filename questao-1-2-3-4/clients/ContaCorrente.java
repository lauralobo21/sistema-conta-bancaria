package clients;

public class ContaCorrente extends Conta {

    // Atributo específico desta classe
    private double limiteChequeEspecial;

    /**
     * Construtor da ContaCorrente.
     * @param numeroConta O número da conta (vem da superclasse)
     * @param titular O objeto Cliente (vem da superclasse)
     * @param limite O limite específico desta conta
     */
    public ContaCorrente(String numeroConta, Cliente titular, double limite) {
        // 1. Chama o construtor da classe "mãe" (Conta)
        super(numeroConta, titular);
        
        // 2. Define o atributo específico desta classe
        this.limiteChequeEspecial = limite;
    }

    /**
     * Implementação OBRIGATÓRIA do método abstrato.
     * Retorna o identificador de tipo.
     */
    @Override
    public String getTipoConta() {
        return "ContaCorrente"; // Identificador único
    }

    // Getter para o atributo específico
    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    // Opcional: Sobrescrever o 'sacar' para usar o limite
    @Override
    public boolean sacar(double valor) {
        if (valor > 0 && (this.saldo + this.limiteChequeEspecial) >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    // Opcional: Sobrescrever o 'toString' para incluir o limite
    @Override
    public String toString() {
        return super.toString() + 
               ", limite=" + limiteChequeEspecial + 
               '}';
    }
}