package model;


public class ContaPoupanca extends Conta {

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
        super(numeroConta, titular);
        this.variacao = variacao;
    }

    @Override
    public String getTipoConta() {
        return "ContaPoupanca";
    }

    public int getVariacao() {
        return variacao;
    }

    @Override
    public String toString() {
        return super.toString() + 
               ", variacao=" + variacao + 
               '}';
    }
}