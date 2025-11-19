package model;

public class ContaCorrente extends Conta {

    private double limiteChequeEspecial;

    /**
     * Construtor da ContaCorrente.
     * @param numeroConta 
     * @param titular 
     * @param limite 
     */
    public ContaCorrente(String numeroConta, Cliente titular, double limite) {
        super(numeroConta, titular);
        this.limiteChequeEspecial = limite;
    }

    @Override
    public String getTipoConta() {
        return "ContaCorrente"; // Identificador único
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public boolean sacar(double valor) {
        if (valor > 0 && (this.saldo + this.limiteChequeEspecial) >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + 
               ", limite=" + limiteChequeEspecial + 
               '}';
    }
}