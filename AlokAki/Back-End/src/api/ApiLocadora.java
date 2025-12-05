package api;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;
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
        .create();

    @SuppressWarnings("unchecked")
    public static void iniciarRotas() {

        // CORS
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        options("/*", (req, res) -> "OK");

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

                // Conversão segura dos números
                int idFilme = body.get("idFilme") instanceof Double 
                    ? ((Double) body.get("idFilme")).intValue() 
                    : (Integer) body.get("idFilme");
                    
                int idCliente = body.get("idCliente") instanceof Double 
                    ? ((Double) body.get("idCliente")).intValue() 
                    : (Integer) body.get("idCliente");
                    
                double valorDiaria = body.get("valorDiaria") instanceof Double
                    ? (Double) body.get("valorDiaria")
                    : ((Integer) body.get("valorDiaria")).doubleValue();
                
                LocalDate dataLocacao = LocalDate.parse(body.get("dataLocacao").toString());
                LocalDate dataPrevista = LocalDate.parse(body.get("dataPrevistaDevolucao").toString());

                // Busca o filme
                Filme filme = filmeDAO.buscarPorId(idFilme);

                if (filme == null || filme.getQuantidadeDisponivel() <= 0) {
                    res.status(400);
                    return "{\"erro\":\"Filme indisponível\"}";
                }

                // Cria a locação
                Locacao nova = new Locacao();
                nova.setIdCliente(idCliente);
                nova.setIdFilme(idFilme);
                nova.setDataLocacao(dataLocacao);
                nova.setDataPrevistaDevolucao(dataPrevista);
                nova.setDataDevolucao(null);
                nova.setValorDiaria(valorDiaria);
                nova.setStatus(body.get("status").toString());

                locacaoDAO.inserir(nova);

                // Atualiza quantidade disponível
                filmeDAO.atualizarQuantidade(idFilme, filme.getQuantidadeDisponivel() - 1);

                res.status(201);
                return gson.toJson(nova);
                
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"" + e.getMessage() + "\"}";
            }
        });

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
}