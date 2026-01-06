package server;

import io.javalin.Javalin;
import io.javalin.http.Context;
import model.*;
import java.util.Map;

public class ServerApi {

    private static Banco banco = new Banco("Banco API REST");

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(7000);

        System.out.println("--- API BANCO INICIADA NA PORTA 7000 ---");

        // Rotas
        app.post("/contas", ServerApi::criarConta);
        app.post("/operacoes/deposito", ServerApi::depositar);
        app.get("/contas/{numero}", ServerApi::consultar);
        app.post("/operacoes/saque", ServerApi::sacar);
    }

    private static void criarConta(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String tipo = body.get("tipo");
            String numero = body.get("numero");
            String nome = body.get("nome");
            
            // --- LOG NO SERVIDOR ---
            System.out.println("📝 [CRIAR] Recebendo pedido de nova conta...");
            System.out.println("   -> Cliente: " + nome + " | Conta: " + numero);

            Cliente cliente = new Cliente(nome, body.get("cpf"), Integer.parseInt(body.get("idade")));
            String extra = body.get("extra");

            if ("CC".equals(tipo)) {
                banco.adicionarConta(new ContaCorrente(numero, cliente, Double.parseDouble(extra)));
            } else {
                banco.adicionarConta(new ContaPoupanca(numero, cliente, Integer.parseInt(extra)));
            }
            
            System.out.println("   ✅ SUCESSO: Conta " + numero + " criada!");
            System.out.println("--------------------------------------------------");
            
            ctx.status(201).result("Conta criada com sucesso!");
        } catch (Exception e) {
            System.out.println("   ❌ ERRO AO CRIAR: " + e.getMessage());
            ctx.status(500).result("Erro ao criar conta: " + e.getMessage());
        }
    }

    private static void depositar(Context ctx) {
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String numero = body.get("numero");
        double valor = Double.parseDouble(body.get("valor"));

        // --- LOG NO SERVIDOR ---
        System.out.println("💰 [DEPOSITO] Conta: " + numero + " | Valor: R$" + valor);

        Conta c = banco.buscarConta(numero);
        if (c != null) {
            c.depositar(valor);
            System.out.println("   ✅ Novo Saldo: R$" + c.getSaldo());
            ctx.result("Deposito realizado. Novo saldo: " + c.getSaldo());
        } else {
            System.out.println("   ❌ Conta não encontrada!");
            ctx.status(404).result("Conta nao encontrada.");
        }
        System.out.println("--------------------------------------------------");
    }

    private static void consultar(Context ctx) {
        String numero = ctx.pathParam("numero");
        
        // --- LOG NO SERVIDOR ---
        System.out.println("🔎 [CONSULTA] Verificando saldo da conta: " + numero);

        Conta c = banco.buscarConta(numero);

        if (c != null) {
            System.out.println("   ✅ Conta encontrada. Retornando dados...");
            ctx.json(Map.of(
                "tipo", c.getTipoConta(),
                "saldo", c.getSaldo(),
                "titular", c.getTitular().getNome()
            ));
        } else {
            System.out.println("   ❌ Conta não encontrada!");
            ctx.status(404).json(Map.of("erro", "Conta nao encontrada"));
        }
        System.out.println("--------------------------------------------------");
    }

    private static void sacar(Context ctx) {
        Map<String, String> body = ctx.bodyAsClass(Map.class);
        String numero = body.get("numero");
        double valor = Double.parseDouble(body.get("valor"));

        // --- LOG NO SERVIDOR ---
        System.out.println("💸 [SAQUE] Conta: " + numero + " | Tentativa de saque: R$" + valor);

        Conta c = banco.buscarConta(numero);
        if (c != null) {
            boolean sucesso = c.sacar(valor);
            if (sucesso) {
                System.out.println("   ✅ Saque OK! Novo saldo: " + c.getSaldo());
                ctx.result("SAQUE_OK. Novo Saldo: " + c.getSaldo());
            } else {
                System.out.println("   ⚠️ Saldo insuficiente! (Saldo atual: " + c.getSaldo() + ")");
                ctx.status(400).result("SAQUE_ERRO: Saldo insuficiente.");
            }
        } else {
            System.out.println("   ❌ Conta não encontrada!");
            ctx.status(404).result("Conta nao encontrada.");
        }
        System.out.println("--------------------------------------------------");
    }
}