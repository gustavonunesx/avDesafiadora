/* CÓDIGO COMPLETO CORRIGIDO ABAIXO */

package api;

import static spark.Spark.*;

import java.time.LocalDate;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.LocacaoDAO;
import model.Cliente;
import model.Filme;
import model.Locacao;
import util.LocalDateAdapter;

public class ApiLocadora {

private static final ClienteDAO clienteDAO = new ClienteDAO();
private static final FilmeDAO filmeDAO = new FilmeDAO();
private static final LocacaoDAO locacaoDAO = new LocacaoDAO();

private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .setPrettyPrinting()
        .create();

public static void iniciarRotas() {

    // --- CORS ---
    before((req, res) -> {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
    });

    options("/*", (req, res) -> "OK");
    after((req, res) -> res.type("application/json"));


    // ====================================================
    // CLIENTES
    // ====================================================
    post("/clientes", (req, res) -> {
        try {
            Cliente c = gson.fromJson(req.body(), Cliente.class);
            clienteDAO.inserir(c);
            res.status(201);
            return gson.toJson(c);
        } catch (Exception e) {
            res.status(400);
            return "{\"erro\":\"" + e.getMessage() + "\"}";
        }
    });

    get("/clientes", (req, res) -> gson.toJson(clienteDAO.buscarTodos()));


    // ====================================================
    // FILMES
    // ====================================================
    post("/filmes", (req, res) -> {
        try {
            Filme f = gson.fromJson(req.body(), Filme.class);
            filmeDAO.inserir(f);
            res.status(201);
            return gson.toJson(f);
        } catch (Exception e) {
            res.status(400);
            return "{\"erro\":\"" + e.getMessage() + "\"}";
        }
    });

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


    // ====================================================
    // LOCAÇÕES
    // ====================================================
    post("/locacoes", (req, res) -> {
        try {
            Locacao nova = gson.fromJson(req.body(), Locacao.class);

            Filme filme = filmeDAO.buscarPorId(nova.getIdFilme());
            if (filme == null || filme.getQuantidadeDisponivel() <= 0) {
                res.status(400);
                return "{\"erro\":\"Filme indisponível\"}";
            }

            locacaoDAO.inserir(nova);

            // Atualiza estoque
            filmeDAO.atualizarQuantidade(
                    filme.getId(),
                    filme.getQuantidadeDisponivel() - 1
            );

            res.status(201);
            return gson.toJson(nova);

        } catch (Exception e) {
            res.status(500);
            return "{\"erro\":\"Erro ao registrar locação: " + e.getMessage() + "\"}";
        }
    });

    get("/locacoes", (req, res) -> gson.toJson(locacaoDAO.buscarTodas()));

    get("/locacoes/:id", (req, res) -> {
        int id = Integer.parseInt(req.params(":id"));
        Locacao l = locacaoDAO.buscarPorId(id);

        if (l == null) {
            res.status(404);
            return "{\"erro\":\"Locação não encontrada\"}";
        }
        return gson.toJson(l);
    });


    // DEVOLUÇÃO DA LOCAÇÃO
    put("/locacoes/:id/devolver", (req, res) -> {
        try {
            int id = Integer.parseInt(req.params(":id"));
            Map<?, ?> json = gson.fromJson(req.body(), Map.class);

            String dataStr = (String) json.get("dataDevolucao");
            String status = (String) json.get("status");

            LocalDate dataDevolucao = LocalDate.parse(dataStr);

            // Atualiza devolução
            locacaoDAO.registrarDevolucao(id, dataDevolucao, status);

            // Atualiza estoque
            Locacao l = locacaoDAO.buscarPorId(id);
            Filme filme = filmeDAO.buscarPorId(l.getIdFilme());
            filmeDAO.atualizarQuantidade(
                    filme.getId(),
                    filme.getQuantidadeDisponivel() + 1
            );

            return gson.toJson(l);

        } catch (Exception e) {
            res.status(400);
            return "{\"erro\":\"" + e.getMessage() + "\"}";
        }
    });


    // DELETAR LOCAÇÃO
    delete("/locacoes/:id", (req, res) -> {
        int id = Integer.parseInt(req.params(":id"));
        locacaoDAO.deletar(id);
        return "{\"mensagem\":\"Locação deletada com sucesso\"}";
    });


    System.out.println("Rotas da API carregadas com sucesso!");
}

}