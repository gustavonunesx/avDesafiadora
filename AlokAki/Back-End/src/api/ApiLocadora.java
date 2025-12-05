/* CÓDIGO COMPLETO CORRIGIDO ABAIXO */

package api;

<<<<<<< HEAD
import static spark.Spark.*;

=======
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2
import java.time.LocalDate;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
<<<<<<< HEAD

=======
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.LocacaoDAO;
import model.Cliente;
import model.Filme;
import model.Locacao;
import util.LocalDateAdapter;

public class ApiLocadora {

<<<<<<< HEAD
private static final ClienteDAO clienteDAO = new ClienteDAO();
private static final FilmeDAO filmeDAO = new FilmeDAO();
private static final LocacaoDAO locacaoDAO = new LocacaoDAO();

private static final Gson gson = new GsonBuilder()
=======
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final FilmeDAO filmeDAO = new FilmeDAO();
    private static final LocacaoDAO locacaoDAO = new LocacaoDAO();
    private static final Gson gson = new GsonBuilder()
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .setPrettyPrinting()
        .create();

<<<<<<< HEAD
public static void iniciarRotas() {

    // --- CORS ---
    before((req, res) -> {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
    });
=======
    @SuppressWarnings("unchecked")
    public static void iniciarRotas() {
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2

    options("/*", (req, res) -> "OK");
    after((req, res) -> res.type("application/json"));


<<<<<<< HEAD
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
=======
        after((req, res) -> res.type("application/json"));

        // ======================
        // CLIENTES
        // ======================
        
        // Criar cliente
        post("/clientes", (req, res) -> {
            try {
                Cliente c = gson.fromJson(req.body(), Cliente.class);
                clienteDAO.inserir(c);
                res.status(201);
                return gson.toJson(c);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Buscar todos os clientes
        get("/clientes", (req, res) -> gson.toJson(clienteDAO.buscarTodos()));

        // Buscar cliente por ID
        get("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cliente c = clienteDAO.buscarPorId(id);
                
                if (c == null) {
                    res.status(404);
                    return "{\"erro\":\"Cliente não encontrado\"}";
                }
                return gson.toJson(c);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Atualizar cliente
        put("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cliente c = gson.fromJson(req.body(), Cliente.class);
                c.setId(id);
                
                clienteDAO.atualizar(c);
                
                res.status(200);
                return gson.toJson(c);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Deletar cliente
        delete("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                clienteDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Cliente deletado com sucesso\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // ======================
        // FILMES
        // ======================
        
        // Buscar todos os filmes
        get("/filmes", (req, res) -> gson.toJson(filmeDAO.buscarTodos()));

        // Buscar filme por ID
        get("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Filme f = filmeDAO.buscarPorId(id);

                if (f == null) {
                    res.status(404);
                    return "{\"erro\":\"Filme não encontrado\"}";
                }
                return gson.toJson(f);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Criar filme
        post("/filmes", (req, res) -> {
            try {
                Filme novo = gson.fromJson(req.body(), Filme.class);
                filmeDAO.inserir(novo);
                res.status(201);
                return gson.toJson(novo);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Atualizar filme
        put("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Filme f = gson.fromJson(req.body(), Filme.class);
                f.setId(id);
                
                filmeDAO.atualizar(f);
                
                res.status(200);
                return gson.toJson(f);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Deletar filme
        delete("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                filmeDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Filme deletado com sucesso\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // ======================
        // LOCAÇÕES
        // ======================
        
        // Buscar todas as locações
        get("/locacoes", (req, res) -> gson.toJson(locacaoDAO.buscarTodas()));

        // Buscar locação por ID
        get("/locacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Locacao l = locacaoDAO.buscarPorId(id);

                if (l == null) {
                    res.status(404);
                    return "{\"erro\":\"Locação não encontrada\"}";
                }
                return gson.toJson(l);
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Criar locação
        post("/locacoes", (req, res) -> {
            try {
                Map<String, Object> body = gson.fromJson(req.body(), Map.class);
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2

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

<<<<<<< HEAD
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

=======
        // Registrar devolução (atualização parcial)
        put("/locacoes/:id/devolucao", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                
                Locacao locacao = locacaoDAO.buscarPorId(id);
                
                if (locacao == null) {
                    res.status(404);
                    return "{\"erro\":\"Locação não encontrada\"}";
                }

                LocalDate dataDevolucao = LocalDate.now();
                locacaoDAO.registrarDevolucao(id, dataDevolucao, "FINALIZADA");

                // Devolve o filme ao estoque
                Filme filme = filmeDAO.buscarPorId(locacao.getIdFilme());
                filmeDAO.atualizarQuantidade(
                    locacao.getIdFilme(), 
                    filme.getQuantidadeDisponivel() + 1
                );

                res.status(200);
                return "{\"mensagem\":\"Devolução registrada com sucesso\"}";
                
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        // Deletar locação
        delete("/locacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                locacaoDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Locação deletada com sucesso\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

        System.out.println("Rotas da API carregadas com sucesso!");
    }
>>>>>>> e27f28e738a234d07332b8593ac95b479cc5a8e2
}