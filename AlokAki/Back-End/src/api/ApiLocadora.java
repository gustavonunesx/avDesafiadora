package api;

import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.options; 
import static spark.Spark.put;

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

        after((req, res) -> res.type(APPLICATION_JSON));

        // 1. Configuração de CORS para permitir o frontend (http://127.0.0.1:5500)
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            // Permite todas as origens (ideal para desenvolvimento local)
            response.header("Access-Control-Allow-Origin", "*"); 
            return "ok";
        });
        
        // 2. Define o Content-Type como JSON e adiciona o header de permissão para todas as respostas
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", APPLICATION_JSON);
        });

        // ------------------- FILMES -------------------

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


        // ------------------- LOCAÇÕES -------------------

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
            Locacao nova = gson.fromJson(req.body(), Locacao.class);

            Filme f = filmeDAO.buscarPorId(nova.getIdFilme());

            if (f == null || f.getQuantidadeDisponivel() <= 0) {
                res.status(400);
                return "{\"erro\":\"Filme indisponível\"}";
            }

            filmeDAO.atualizarQuantidade(f.getId(), f.getQuantidadeDisponivel() - 1);

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


        System.out.println("Rotas carregadas!");
    }
}
