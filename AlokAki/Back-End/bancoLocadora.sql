CREATE DATABASE locadora; USE locadora;

CREATE TABLE filme (
id INT AUTO_INCREMENT PRIMARY KEY,
 titulo VARCHAR(100) NOT NULL, 
 genero VARCHAR(50) NOT NULL, 
 anoLancamento INT NOT NULL, 
 quantidadeTotal INT NOT NULL, 
 quantidadeDisponivel INT NOT NULL );

CREATE TABLE cliente ( 
id INT AUTO_INCREMENT PRIMARY KEY,
 nome VARCHAR(120) NOT NULL, 
 telefone VARCHAR(20) );

CREATE TABLE locacao ( 
id INT AUTO_INCREMENT PRIMARY KEY, 
idFilme INT NOT NULL, 
idCliente INT NOT NULL, 
dataLocacao DATE NOT NULL, 
dataPrevistaDevolucao DATE NOT NULL, 
dataDevolucao DATE NULL, status VARCHAR(20) NOT NULL,
 valorDiaria DECIMAL(10,2) NOT NULL,
 FOREIGN KEY (idFilme) REFERENCES filme(id),
FOREIGN KEY (idCliente) REFERENCES cliente(id));

