# Trabalho 3: Web Services e API (Sistema Bancário Distribuído)

**Disciplina:** Sistemas Distribuídos | **Instituição:** UFC - Campus Quixadá

**Autoras:** Gabriely Correia Dealem & Laura Soléria Lobo Vitorino Maciel

### Endpoints da API

O servidor Java intercepta requisições HTTP e as roteia para:

* `POST /criar`: Criação de nova conta.
* `POST /deposito`: Adição de fundos.
* `POST /saque`: Retirada de fundos.
* `GET /saldo`: Consulta de saldo atual.

## 1. Requisitos e Tecnologias

* **Java 8+** (com Maven para build)
* **PHP** (para o cliente server-side)
* **Navegador Web** (para o cliente JS)
* **Tecnologias:** HTTP, JSON, Sockets/ServerSocket.

## 2. Compilação e Execução

### Passo 1: Inicializar o Servidor (Java)

O servidor deve ser o primeiro a ser iniciado, pois detém o estado da aplicação. Na raiz do projeto (pasta `Banco` ou raiz geral), execute:

```bash
mvn clean compile exec:java -Dexec.mainClass="server.ServerApi"

```

*O servidor ficará aguardando requisições na porta configurada (ex: 8080).*

### Passo 2: Executar o Cliente PHP

Em um novo terminal, navegue até a pasta do cliente PHP (`client_php`) e inicie o servidor embutido:

```bash
php -S localhost:8000

```

Acesse no navegador: `http://localhost:8000`

### Passo 3: Executar o Cliente JavaScript

Para o cliente JS, não é necessária compilação.

1. Navegue até a pasta `client_js`.
2. Abra o arquivo `index.html` diretamente no navegador.

