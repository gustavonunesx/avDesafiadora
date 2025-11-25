MovieTime - API minimal para locadora (Java 17 + Spark + JDBC)

1) Criar banco
   - Execute o script sql/movietime_schema.sql no seu MySQL.

2) Configurar conexão
   - Abra factory/ConnectionFactory.java e altere USER e PASS para suas credenciais.

3) Build e Run
   - Usando Maven:
     mvn clean package
     mvn exec:java -Dexec.mainClass="App"
   - Ou rode a classe App diretamente da sua IDE.

4) Testes rápidos (curl)

- Criar filme:
curl -X POST http://localhost:4567/filmes -H "Content-Type: application/json" -d '{
  "titulo":"Matrix",
  "genero":"Sci-Fi",
  "anoLancamento":1999,
  "quantidadeTotal":3,
  "quantidadeDisponivel":3
}'

- Listar filmes:
curl http://localhost:4567/filmes

- Criar locação:
curl -X POST http://localhost:4567/locacoes -H "Content-Type: application/json" -d '{
  "idFilme":1,
  "nomeCliente":"João",
  "dataLocacao":"2025-11-24",
  "valorDiaria":5.0
}'

- Devolver (registra devolução com data atual):
curl -X PUT http://localhost:4567/locacoes/1/devolver

Observações:
- Datas no formato ISO: yyyy-MM-dd
- Endpoints minimalistas para teste. Validações básicas implementadas.
