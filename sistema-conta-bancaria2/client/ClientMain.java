package client;

import rmi.RmiInfrastructure;
import java.nio.charset.StandardCharsets;

public class ClientMain {

    private static RmiInfrastructure rmi = new RmiInfrastructure();

    public static void main(String[] args) {

        System.out.println("===================================");
        System.out.println("--- CLIENTE RMI AUTOMÁTICO INICIADO ---");
        System.out.println("===================================");

        executarTestesAutomaticos();
    }

    private static void executarTestesAutomaticos() {

        // A: Criar conta
        String dadosContaA = "CC;111-22;Gaby Costa;999.999.999-99;25;500.0";
        System.out.println("\n > Teste A: Criando Conta CC 111-22...");
        byte[] responseA = rmi.doOperation("BancoService", 1, dadosContaA.getBytes(StandardCharsets.UTF_8));
        System.out.println("   - Resposta Servidor: " + new String(responseA, StandardCharsets.UTF_8));

        // B: Depositar
        String dadosDepositoB = "111-22;250.00";
        System.out.println(" > Teste B: Depositando 250.00 em 111-22...");
        byte[] responseB = rmi.doOperation("BancoService", 2, dadosDepositoB.getBytes(StandardCharsets.UTF_8));
        System.out.println("   - Resposta Servidor: " + new String(responseB, StandardCharsets.UTF_8));

        // C: Consultar saldo
        System.out.println(" > Teste C: Consultando Conta 111-22...");
        byte[] responseC = rmi.doOperation("BancoService", 3, "111-22".getBytes(StandardCharsets.UTF_8));
        System.out.println("   - Resposta Servidor (JSON): " + new String(responseC, StandardCharsets.UTF_8));

        // D: Sacar
        System.out.println(" > Teste D: Sacando 100.00 da conta 111-22...");
        byte[] responseD = rmi.doOperation("BancoService", 4, "111-22;100.00".getBytes(StandardCharsets.UTF_8));
        String respD = new String(responseD, StandardCharsets.UTF_8);

        if (respD.startsWith("SAQUE_OK")) {
            String novoSaldo = respD.split(";")[1];
            System.out.println("   - Saque realizado com sucesso! Novo saldo: " + novoSaldo);
        } else {
            System.out.println("   - Erro ao sacar: Saldo insuficiente.");
        }
    }
}
