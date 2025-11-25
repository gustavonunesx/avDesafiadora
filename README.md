Sistema de Locadora de Filmes
🎬 Sobre o Projeto

Este projeto consiste em um sistema completo de Locadora de Filmes, desenvolvido em Java, utilizando JDBC, MySQL, DAO pattern e uma API REST simples.
Inclui também uma interface básica em HTML + CSS + JavaScript, organizada e funcional.

O sistema permite:

Cadastrar filmes

Listar filmes

Cadastrar locações

Registrar devoluções

Verificar atrasos

Controlar estoque (quantidade disponível)

Integrar tudo via REST ou App.java para testes locais

📁 Estrutura do Projeto
src/
 ├── model/
 │    ├── Filme.java
 │    └── Locacao.java
 ├── dao/
 │    ├── FilmeDAO.java
 │    └── LocacaoDAO.java
 ├── api/
 │    └── LocadoraController.java
 ├── util/
 │    └── ConnectionFactory.java
 └── App.java

🧩 Modelos (Entities)
🎞️ Filme.java

Representa um filme disponível na locadora.

Atributos:

id

titulo

genero

anoLancamento

quantidadeTotal

quantidadeDisponivel

📄 Locacao.java

Representa uma locação realizada.

Atributos:

id

idFilme (FK)

dataLocacao

dataPrevistaDevolucao

dataDevolucao

status

valorDiaria

🗄️ Banco de Dados (MySQL)
✔️ Criação do banco e tabelas
CREATE DATABASE locadora;
USE locadora;

CREATE TABLE filme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    anoLancamento INT NOT NULL,
    quantidadeTotal INT NOT NULL,
    quantidadeDisponivel INT NOT NULL
);

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(20)
);

CREATE TABLE locacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idFilme INT NOT NULL,
    idCliente INT NOT NULL,
    dataLocacao DATE NOT NULL,
    dataPrevistaDevolucao DATE NOT NULL,
    dataDevolucao DATE NULL,
    status VARCHAR(20) NOT NULL,
    valorDiaria DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (idFilme) REFERENCES filme(id),
    FOREIGN KEY (idCliente) REFERENCES cliente(id)
);


📌 Obs.: Cliente foi incluído devido ao pedido posterior do usuário.

🧠 DAO Pattern (Acesso ao Banco)
🎞️ FilmeDAO.java

Contém operações:

inserirFilme()

listarFilmes()

atualizarEstoque()

buscarPorId()

📄 LocacaoDAO.java

Contém operações:

registrarLocacao()

registrarDevolucao()

listarLocacoes()

buscarPorId()

verificarAtrasos()

🌐 API REST (Spring-like minimal)

Endpoints:

Método	Rota	Função
GET	/filmes	lista todos os filmes
POST	/filmes	cadastra novo filme
GET	/locacoes	lista locações
POST	/locacoes	cria uma locação
PUT	/locacoes/{id}/devolver	registra devolução
💻 App.java — Testes Offline

Inclui testes de:

Inserir filmes

Criar locações

Devolver filme

Calcular atraso

Listar tudo

Perfeito para testes antes de integrar ao backend.

🖥️ Interface Web (HTML + CSS + JavaScript)

Inclui:

✔️ Layout moderno e simples

Tema escuro

Navegação superior

Seções: Filmes / Locação / Devolução

✔️ CSS limpo

Sem exageros

Sistema elegante

Responsivo

✔️ JS simulando consumo da API

Cadastro

Listagem

Devolução

Notificações (toast)

📌 Funcionalidades do Sistema
🎞️ Gerenciamento de Filmes

Cadastro de novos filmes

Quantidade total

Quantidade disponível

Edição simples

📄 Locação

Escolher filme

Definir data devolução prevista

Redução automática do estoque

🔄 Devolução

Registrar devolução

Atualizar estoque

Calcular atraso

Atualizar status

🚨 Atraso

Detecta atraso automaticamente

Exibe diferença entre datas

🚀 Como Rodar o Projeto
1️⃣ Configurar MySQL
CREATE DATABASE locadora;

2️⃣ Ajustar ConnectionFactory.java
String url = "jdbc:mysql://localhost:3306/locadora";
String user = "root";
String pass = "SUA_SENHA";

3️⃣ Rodar App.java

Executa todos os testes automáticos.

4️⃣ Usar API via Postman

Exemplo:

GET http://localhost:8080/filmes

5️⃣ Abrir interface web

Abra:

index.html

📚 Tecnologias Utilizadas

Java 17

JDBC

MySQL

DAO Pattern

HTML + CSS + JavaScript

REST Fake / Controller simples

LocalDate (Java Time API)
