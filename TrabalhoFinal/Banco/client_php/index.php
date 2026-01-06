<?php
session_start(); // Inicia a memória do navegador

$mensagem = '';
$tipoMensagem = '';
$apiUrl = "http://localhost:7000";

// --- LÓGICA DE SAIR (LOGOUT) ---
if (isset($_GET['sair'])) {
    session_destroy();
    header("Location: index.php");
    exit;
}

// --- PROCESSAMENTO DOS FORMULÁRIOS ---
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // 1. LÓGICA PARA CRIAR CONTA E JÁ ENTRAR
    if (isset($_POST['acao']) && $_POST['acao'] == 'criar') {
        $url = $apiUrl . "/contas";
        $data = [
            "tipo" => $_POST['tipo'], 
            "numero" => $_POST['numero'], 
            "nome" => $_POST['nome'], 
            "cpf" => $_POST['cpf'],
            "idade" => $_POST['idade'], 
            "extra" => $_POST['extra']
        ];

        $res = enviarRequisicao($url, 'POST', $data);
        
        if ($res['codigo'] == 201) {
            // Se criou com sucesso, loga o usuário automaticamente
            $_SESSION['conta_logada'] = $_POST['numero'];
            $_SESSION['nome_usuario'] = $_POST['nome']; // Guarda o nome pra ficar bonito
            $mensagem = "Conta criada! Bem-vindo.";
            $tipoMensagem = 'sucesso';
        } else {
            $mensagem = "Erro ao criar: " . $res['corpo'];
            $tipoMensagem = 'erro';
        }
    }

    // 2. LÓGICA PARA APENAS ENTRAR (LOGIN SIMPLES)
    if (isset($_POST['acao']) && $_POST['acao'] == 'entrar') {
        $_SESSION['conta_logada'] = $_POST['numero_acesso'];
        // Tenta buscar o nome só pra exibir (opcional)
        $_SESSION['nome_usuario'] = "Cliente"; 
    }

    // 3. LÓGICA DE OPERAÇÕES (QUANDO JÁ ESTÁ LOGADO)
    if (isset($_POST['operacao'])) {
        $endpoint = $_POST['operacao'];
        $numero = $_SESSION['conta_logada']; // Pega da sessão, seguro!
        
        if ($endpoint == 'consultar') {
            $res = enviarRequisicao($apiUrl . "/contas/" . $numero, 'GET');
        } else {
            // Depósito ou Saque
            $url = $apiUrl . "/operacoes/" . $endpoint; // deposito ou saque
            $data = ["numero" => $numero, "valor" => $_POST['valor']];
            $res = enviarRequisicao($url, 'POST', $data);
        }

        if ($res['codigo'] >= 200 && $res['codigo'] < 300) {
            $mensagem = "✅ " . $res['corpo'];
            $tipoMensagem = 'sucesso';
        } else {
            $mensagem = "❌ " . $res['corpo'];
            $tipoMensagem = 'erro';
        }
    }
}

// Função auxiliar para falar com o Java
function enviarRequisicao($url, $method, $data = []) {
    $options = [
        'http' => [
            'header'  => "Content-type: application/json\r\n",
            'method'  => $method,
            'content' => ($method == 'POST') ? json_encode($data) : null,
            'ignore_errors' => true
        ]
    ];
    $context  = stream_context_create($options);
    $result = @file_get_contents($url, false, $context);
    
    // Pega o código HTTP (200, 404, 500...)
    $codigo = 0;
    if (isset($http_response_header)) {
        preg_match("/HTTP\/\d\.\d\s+(\d+)/", $http_response_header[0], $matches);
        $codigo = intval($matches[1]);
    }
    return ['codigo' => $codigo, 'corpo' => $result];
}
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Banco PHP</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #eef2f7; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
        .container { background: white; padding: 30px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); width: 400px; }
        h2 { text-align: center; color: #2c3e50; margin-top: 0; }
        input, select, button { width: 100%; padding: 12px; margin: 8px 0; border-radius: 6px; border: 1px solid #ddd; box-sizing: border-box; }
        button { cursor: pointer; font-weight: bold; color: white; border: none; transition: 0.3s; }
        .btn-blue { background: #3498db; } .btn-blue:hover { background: #2980b9; }
        .btn-green { background: #27ae60; } .btn-green:hover { background: #219150; }
        .btn-red { background: #c0392b; } .btn-red:hover { background: #a93226; }
        .msg { padding: 10px; border-radius: 5px; text-align: center; margin-bottom: 15px; font-size: 0.9em; }
        .sucesso { background: #d4edda; color: #155724; }
        .erro { background: #f8d7da; color: #721c24; }
        .logout { display: block; text-align: center; margin-top: 15px; color: #7f8c8d; text-decoration: none; font-size: 0.9em; }
        .divider { border-top: 1px solid #eee; margin: 20px 0; text-align: center; color: #aaa; font-size: 0.8em; }
    </style>
</head>
<body>

<div class="container">
    
    <?php if ($mensagem): ?>
        <div class="msg <?php echo $tipoMensagem; ?>"><?php echo $mensagem; ?></div>
    <?php endif; ?>

    <?php if (!isset($_SESSION['conta_logada'])): ?>
        
        <h2>🏦 Bem-vindo ao Banco</h2>
        
        <form method="POST">
            <input type="hidden" name="acao" value="criar">
            <input type="text" name="numero" placeholder="Número da Nova Conta" required>
            <input type="text" name="nome" placeholder="Seu Nome" required>
            <div style="display:flex; gap:5px;">
                <input type="text" name="cpf" placeholder="CPF" required>
                <input type="number" name="idade" placeholder="Idade" required>
            </div>
            <select name="tipo">
                <option value="CC">Conta Corrente</option>
                <option value="CP">Conta Poupança</option>
            </select>
            <input type="text" name="extra" placeholder="Limite ou Aniversário" required>
            <button type="submit" class="btn-blue">Criar Conta e Entrar</button>
        </form>

        <div class="divider">JÁ TEM CONTA? ACESSE ABAIXO</div>

        <form method="POST">
            <input type="hidden" name="acao" value="entrar">
            <input type="text" name="numero_acesso" placeholder="Número da Conta Existente" required>
            <button type="submit" class="btn-green">Acessar Conta</button>
        </form>

    <?php else: ?>

        <h2>🏧 Olá, <?php echo isset($_SESSION['nome_usuario']) ? $_SESSION['nome_usuario'] : 'Cliente'; ?>!</h2>
        <p style="text-align:center; color:#7f8c8d;">Conta Conectada: <strong><?php echo $_SESSION['conta_logada']; ?></strong></p>
        <hr style="border:0; border-top:1px solid #eee; margin-bottom: 20px;">

        <form method="POST">
            <label>Valor da Operação (R$)</label>
            <input type="number" name="valor" placeholder="0.00">
            
            <div style="display:flex; gap:10px;">
                <button type="submit" name="operacao" value="deposito" class="btn-green">Depositar</button>
                <button type="submit" name="operacao" value="saque" class="btn-red">Sacar</button>
            </div>
            
            <button type="submit" name="operacao" value="consultar" class="btn-blue" style="margin-top:10px;">🔍 Ver Saldo na Tela</button>
        </form>

        <a href="?sair=true" class="logout">🔒 Sair da Conta</a>

    <?php endif; ?>

</div>

</body>
</html>