package api;

import static spark.Spark.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import dao.FilmeDAO;
import dao.LocacaoDAO;
import model.Filme;
import model.Locacao;

public class ApiLocadora {

    private static final FilmeDAO filmeDAO = new FilmeDAO();
    private static final LocacaoDAO locacaoDAO = new LocacaoDAO();
    private static final Gson gson = new Gson();
    private static final String APPLICATION_JSON = "application/json";

    public static void iniciarRotas() {

        // ===============================
        // CORS — CONFIGURAÇÃO CORRETA
        // ===============================
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        options("/*", (req, res) -> {
            res.status(200);
            return "OK";
        });

        after((req, res) -> res.type(APPLICATION_JSON));

        // ===============================
        // ROTAS — FILMES
        // ===============================

        get("/filmes", (req, res) -> gson.toJson(filmeDAO.buscarTodos()));

        get("/filmes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Filme f = filmeDAO.buscarPorId(id);

            if (f == null) {
                res.status(404);
                return "{\"erro\":\"Filme não encontrado\"}";
            }
            return gson.toJson(f);
        });

        post("/filmes", (req, res) -> {
            Filme novo = gson.fromJson(req.body(), Filme.class);
            filmeDAO.inserir(novo);
            res.status(201);
            return gson.toJson(novo);
        });

        put("/filmes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Filme existente = filmeDAO.buscarPorId(id);

            if (existente == null) {
                res.status(404);
                return "{\"erro\":\"Filme não encontrado\"}";
            }

            Filme atualizado = gson.fromJson(req.body(), Filme.class);
            atualizado.setId(id);
            filmeDAO.atualizar(atualizado);
            return gson.toJson(atualizado);
        });

        delete("/filmes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            if (filmeDAO.buscarPorId(id) == null) {
                res.status(404);
                return "{\"erro\":\"Filme não encontrado\"}";
            }

            filmeDAO.deletar(id);
            res.status(204);
            return "";
        });

        // ===============================
        // ROTAS — LOCAÇÕES
        // ===============================

        get("/locacoes", (req, res) -> gson.toJson(locacaoDAO.buscarTodas()));

        get("/locacoes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Locacao loc = locacaoDAO.buscarPorId(id);

            if (loc == null) {
                res.status(404);
                return "{\"erro\":\"Locação não encontrada\"}";
            }

            return gson.toJson(loc);
        });

        post("/locacoes", (req, res) -> {
            Map<String, Object> body = gson.fromJson(req.body(), Map.class);

            // Validação correta
            if (body.get("idFilme") == null ||
                    body.get("idCliente") == null ||
                    body.get("dataLocacao") == null ||
                    body.get("prazoDias") == null ||
                    body.get("valorDiaria") == null) {

                res.status(400);
                return "{\"erro\":\"Campos obrigatórios faltando\"}";
            }

            int idFilme = ((Double) body.get("idFilme")).intValue();
            int idCliente = ((Double) body.get("idCliente")).intValue();

            LocalDate dataLocacao = LocalDate.parse((String) body.get("dataLocacao"));
            int prazo = ((Double) body.get("prazoDias")).intValue();
            LocalDate dataPrevista = dataLocacao.plusDays(prazo);

            double valorDiaria = (Double) body.get("valorDiaria");

            Filme filme = filmeDAO.buscarPorId(idFilme);

            if (filme == null || filme.getQuantidadeDisponivel() <= 0) {
                res.status(400);
                return "{\"erro\":\"Filme indisponível\"}";
            }

            Locacao nova = new Locacao();
            nova.setIdFilme(idFilme);
            nova.setIdCliente(idCliente);
            nova.setDataLocacao(dataLocacao);
            nova.setDataPrevistaDevolucao(dataPrevista);
            nova.setDataDevolucao(null);
            nova.setStatus("ATIVA");
            nova.setValorDiaria(valorDiaria);

            // Atualiza estoque
            filmeDAO.atualizarQuantidade(idFilme, filme.getQuantidadeDisponivel() - 1);

            // Salva no banco
            locacaoDAO.inserir(nova);

            res.status(201);
            return gson.toJson(nova);
        });

        put("/locacoes/:id/devolver", (req, res) -> {
            int idLoc = Integer.parseInt(req.params(":id"));
            Locacao loc = locacaoDAO.buscarPorId(idLoc);

            if (loc == null) {
                res.status(404);
                return "{\"erro\":\"Locação não encontrada\"}";
            }

            LocalDate hoje = LocalDate.now();
            long diasAtraso = 0;

            if (hoje.isAfter(loc.getDataPrevistaDevolucao())) {
                diasAtraso = ChronoUnit.DAYS.between(loc.getDataPrevistaDevolucao(), hoje);
                loc.setStatus("ATRASADA");
            } else {
                loc.setStatus("FINALIZADA");
            }

            locacaoDAO.registrarDevolucao(idLoc, hoje, loc.getStatus());

            Filme f = filmeDAO.buscarPorId(loc.getIdFilme());
            filmeDAO.atualizarQuantidade(f.getId(), f.getQuantidadeDisponivel() + 1);

            double multa = diasAtraso * loc.getValorDiaria();

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("diasAtraso", diasAtraso);
            resposta.put("multa", multa);
            resposta.put("status", loc.getStatus());

            return gson.toJson(resposta);
        });

        delete("/locacoes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            if (locacaoDAO.buscarPorId(id) == null) {
                res.status(404);
                return "{\"erro\":\"Locação não encontrada\"}";
            }

            locacaoDAO.deletar(id);
            res.status(204);
            return "";
        });

        System.out.println("Rotas carregadas com sucesso!");
    }
}
