package client;

import rmi.RmiInfrastructure;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientMainMenu {

    private static RmiInfrastructure rmi = new RmiInfrastructure();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("===================================");
        System.out.println("--- CLIENTE RMI INTERATIVO (2) INICIADO ---");
        System.out.println("===================================");

        menuInterativo();

        scanner.close();
    }

    private static void menuInterativo() {

        try {
            // --- 1. CADASTRAR NOVA CONTA ---
            System.out.println("\n--- 1. CADASTRAR NOVA CONTA ---");
            System.out.print("Tipo (CC/CP): ");
            String tipo = scanner.nextLine().toUpperCase();
            System.out.print("Número: ");
            String numero = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Idade: ");
            String idade = scanner.nextLine();

            String dadosAdicionais = "";
            if (tipo.equals("CC")) {
                System.out.print("Limite do Cheque Especial: ");
                dadosAdicionais = scanner.nextLine();
            } else if (tipo.equals("CP")) {
                System.out.print("Variação (ex: 51): ");
                dadosAdicionais = scanner.nextLine();
            } else {
                System.out.println("Tipo inválido. Cancelando cadastro.");
                return;
            }

            // Monta os dados e envia
            String dadosConta = String.join(";", tipo, numero, nome, cpf, idade, dadosAdicionais);
            byte[] response1 = rmi.doOperation("BancoService", 1, dadosConta.getBytes(StandardCharsets.UTF_8));
            System.out.println("-> Resposta Servidor: " + new String(response1, StandardCharsets.UTF_8));


            // --- 2. DEPOSITAR ---
            System.out.println("\n--- 2. DEPOSITAR ---");
            System.out.print("Valor para depositar na conta " + numero + ": ");
            String valorDeposito = scanner.nextLine();

            String dadosDeposito = String.join(";", numero, valorDeposito);
            byte[] response2 = rmi.doOperation("BancoService", 2, dadosDeposito.getBytes(StandardCharsets.UTF_8));
            System.out.println("-> Resposta Servidor: " + new String(response2, StandardCharsets.UTF_8));


            // --- 3. PERGUNTA SE QUER VER SALDO ---
            System.out.print("\nDeseja consultar o saldo agora? (s/n): ");
            String verSaldo = scanner.nextLine().trim().toLowerCase();

            if (verSaldo.equals("s")) {
                byte[] response3 = rmi.doOperation("BancoService", 3, numero.getBytes(StandardCharsets.UTF_8));
                System.out.println("-> Saldo Atual (JSON): " + new String(response3, StandardCharsets.UTF_8));
            }


            // --- 4. SACAR ---
            System.out.println("\n--- 4. SACAR ---");
            System.out.print("Valor para sacar da conta " + numero + ": ");
            String valorSaque = scanner.nextLine();

            String dadosSaque = String.join(";", numero, valorSaque);
            byte[] response4 = rmi.doOperation("BancoService", 4, dadosSaque.getBytes(StandardCharsets.UTF_8));
            String resp4 = new String(response4, StandardCharsets.UTF_8);

            if (resp4.startsWith("SAQUE_OK")) {
                String novoSaldo = resp4.split(";")[1];
                System.out.println("-> Saque realizado com sucesso! Novo saldo: " + novoSaldo);
            } else {
                System.out.println("-> ERRO: Saldo insuficiente.");
            }


            // --- 5. PERGUNTA SE QUER VER O SALDO FINAL ---
            System.out.print("\nDeseja consultar o saldo após o saque? (s/n): ");
            String verSaldoFinal = scanner.nextLine().trim().toLowerCase();

            if (verSaldoFinal.equals("s")) {
                byte[] responseFinal = rmi.doOperation("BancoService", 3, numero.getBytes(StandardCharsets.UTF_8));
                System.out.println("-> Saldo Atual (JSON): " + new String(responseFinal, StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a operação: Verifique se o Servidor está rodando. " + e.getMessage());
        }
    }
}
