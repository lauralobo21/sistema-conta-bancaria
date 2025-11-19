package server;

import model.Banco;
import model.Cliente;
import model.Conta;
import model.ContaCorrente;
import model.ContaPoupanca;
import rmi.RmiInfrastructure; 
import rmi.Message;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerMain {

    private static Banco banco = new Banco("Banco Central");
    private static RmiInfrastructure rmi = new RmiInfrastructure();

    public static void main(String[] args) {

        int port = 12345;
        System.out.println("Servidor RMI iniciado na porta " + port);

        try (DatagramSocket socket = new DatagramSocket(port)) {

            while (true) {

                Message request = rmi.getRequest(socket);
                System.out.println("Requisição recebida para método ID: " + request.methodId);

                // Extrair informações do remetente
                String[] metadata = request.objectReference.split(":");
                InetAddress clientIP = InetAddress.getByName(metadata[1]);
                int clientPort = Integer.parseInt(metadata[2]);

                //processamento da requisição RMI no servidor.
                String argsStr = new String(request.arguments, StandardCharsets.UTF_8).trim();
                String responseStr = "";

                // ID 1: Criar Conta
                if (request.methodId == 1) {

                    String[] parts = argsStr.split(";");
                    Cliente cli = new Cliente(parts[2], parts[3], Integer.parseInt(parts[4]));

                    if (parts[0].equals("CC")) {
                        banco.adicionarConta(new ContaCorrente(
                                parts[1],
                                cli,
                                Double.parseDouble(parts[5])
                        ));
                    } else {
                        banco.adicionarConta(new ContaPoupanca(
                                parts[1],
                                cli,
                                Integer.parseInt(parts[5])
                        ));
                    }

                    responseStr = "Conta criada com sucesso!";
                }
  
                // ID 2: Depositar
                else if (request.methodId == 2) {

                    String[] parts = argsStr.split(";");

                    Conta c = banco.buscarConta(parts[0]);
                    if (c != null) {

                        c.depositar(Double.parseDouble(parts[1]));

                        responseStr = "Deposito realizado. Novo saldo: " + c.getSaldo();
                    } else {
                        responseStr = "Conta nao encontrada.";
                    }
                }

                // ID 3: Consultar saldo (JSON)
                else if (request.methodId == 3) {

                    Conta c = banco.buscarConta(argsStr);
                    if (c != null) {

                        responseStr =
                                "{ \"tipo\": \"" + c.getTipoConta() +
                                        "\", \"saldo\": " + c.getSaldo() + " }";

                    } else {
                        responseStr = "{}";
                    }
                }

                // ID 4: Sacar
                else if (request.methodId == 4) {

                    String[] parts = argsStr.split(";");

                    String numeroConta = parts[0];
                    double valor = Double.parseDouble(parts[1].trim());

                    Conta c = banco.buscarConta(numeroConta);

                    if (c != null) {

                        boolean ok = c.sacar(valor);

                        if (ok) {
                            responseStr = "SAQUE_OK;" + c.getSaldo();
                        } else {
                            responseStr = "SAQUE_ERRO";
                        }

                    } else {
                        responseStr = "SAQUE_ERRO";
                    }
                }

                rmi.sendReply(responseStr.getBytes(StandardCharsets.UTF_8), clientIP, clientPort);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
