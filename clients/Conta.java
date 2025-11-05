package clients;

public abstract class Conta {
    protected String numeroConta;
    protected Cliente titular;
    protected double saldo;

    public Conta(String numeroConta, Cliente titular) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = 0.0;
    }

    public abstract String getTipoConta();

    public String getNumeroConta() { 
        return numeroConta; 
    }
    public Cliente getTitular() { 
        return titular; 
    }
    public double getSaldo() { 
        return saldo; 
    }
    public void setSaldo(double saldo){ 
        this.saldo = saldo;
    }

    // Serviços básicos
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getTipoConta() + "{" +
                "numero='" + numeroConta + '\'' +
                ", titular=" + titular.getNome() +
                ", saldo=" + saldo +
                '}';
    }
}