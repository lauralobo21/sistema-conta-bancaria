# Sistema Bancário Java (RMI + UDP)

## Requisitos do sistema

- **Java 8** ou superior instalado
- Sistema operacional Windows, Linux ou Mac
- Terminal/Bash para execução dos comandos
- (Opcional) Bibliotecas em `lib/` já presentes no repositório

## Compilação do projeto

No terminal, dentro da raiz do projeto, utilize:

```bash
javac -d bin -cp "lib/*" server/*.java client/*.java
```
Isso compila todos os arquivos fonte para a pasta `bin`, considerando as dependências que estão em `lib/`.

Se desejar compilar todas as pastas de uma vez, use:

```bash
javac -d bin -cp "lib/*" */*.java
```

## Como executar

### Inicializar o Servidor

```bash
java -cp "bin:lib/*" server.ServerMain
```
O servidor ficará aguardando as conexões e as operações dos clientes.

### Inicializar o Cliente Automático

```bash
java -cp "bin:lib/*" client.ClientMain
```
Esse cliente executa operações automáticas para validação e testes.

### Inicializar o Cliente Interativo

```bash
java -cp "bin:lib/*" client.ClientMainMenu
```
Esse cliente permite manipulação manual com menu: criar conta bancária, depositar, consultar saldo, e mais.

[9](https://www.ibm.com/docs/pt-br/was-nd/8.5.5?topic=installing-hardware-software-requirements)
[10](https://www.devmedia.com.br/tecnicas-para-levantamento-de-requisitos/9151)
[11](https://www.digitalocean.com/community/tutorials/java-socket-programming-server-client)
