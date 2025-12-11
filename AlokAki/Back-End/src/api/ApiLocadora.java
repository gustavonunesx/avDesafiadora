package api;

import static spark.Spark.*;
import java.time.LocalDate;
import java.util.Map;
import com.google.gson.Gson;
import util.LocalDateAdapter;
import com.google.gson.GsonBuilder; 
import dao.ClienteDAO;
import dao.FilmeDAO;
import dao.LocacaoDAO;
import model.Cliente;
import model.Filme;
import model.Locacao;

public class ApiLocadora {

    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final FilmeDAO filmeDAO = new FilmeDAO();
    private static final LocacaoDAO locacaoDAO = new LocacaoDAO();
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();

    @SuppressWarnings("unchecked")
    public static void iniciarRotas() {

        // IMPORTANTE: Configurar porta antes das rotas
        port(4567);

        // CORS - CORRIGIDO para aceitar PUT e DELETE
        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
            res.header("Access-Control-Max-Age", "3600");
            res.type("application/json");
        });

        // ======================
        // CLIENTES
        // ======================
        
        // CREATE
        post("/clientes", (req, res) -> {
            try {
                Cliente c = gson.fromJson(req.body(), Cliente.class);
                
                if (c.getNome() == null || c.getNome().trim().isEmpty()) {
                    res.status(400);
                    return "{\"erro\":\"Nome do cliente é obrigatório\"}";
                }
                
                clienteDAO.inserir(c);
                res.status(201);
                return gson.toJson(c);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao criar cliente: " + e.getMessage() + "\"}";
            }
        });

        // READ ALL
        get("/clientes", (req, res) -> {
            try {
                return gson.toJson(clienteDAO.buscarTodos());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar clientes\"}";
            }
        });

        // READ ONE
        get("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cliente c = clienteDAO.buscarPorId(id);
                
                if (c == null) {
                    res.status(404);
                    return "{\"erro\":\"Cliente não encontrado\"}";
                }
                return gson.toJson(c);
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"erro\":\"ID inválido\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar cliente\"}";
            }
        });

        // UPDATE
        put("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Cliente c = gson.fromJson(req.body(), Cliente.class);
                c.setId(id);
                
                if (clienteDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return "{\"erro\":\"Cliente não encontrado\"}";
                }
                
                clienteDAO.atualizar(c);
                return gson.toJson(c);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao atualizar cliente\"}";
            }
        });

        // DELETE
        delete("/clientes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                
                if (clienteDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return "{\"erro\":\"Cliente não encontrado\"}";
                }
                
                clienteDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Cliente deletado com sucesso\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao deletar cliente\"}";
            }
        });

        // ======================
        // FILMES
        // ======================
        
        // CREATE
        post("/filmes", (req, res) -> {
            try {
                Filme novo = gson.fromJson(req.body(), Filme.class);
                
                if (novo.getTitulo() == null || novo.getTitulo().trim().isEmpty()) {
                    res.status(400);
                    return "{\"erro\":\"Título do filme é obrigatório\"}";
                }
                
                if (novo.getQuantidadeDisponivel() < 0) {
                    res.status(400);
                    return "{\"erro\":\"Quantidade não pode ser negativa\"}";
                }
                
                filmeDAO.inserir(novo);
                res.status(201);
                return gson.toJson(novo);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao criar filme\"}";
            }
        });

        // READ ALL
        get("/filmes", (req, res) -> {
            try {
                return gson.toJson(filmeDAO.buscarTodos());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar filmes\"}";
            }
        });

        // READ ONE
        get("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Filme f = filmeDAO.buscarPorId(id);

                if (f == null) {
                    res.status(404);
                    return "{\"erro\":\"Filme não encontrado\"}";
                }
                return gson.toJson(f);
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"erro\":\"ID inválido\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar filme\"}";
            }
        });

        // UPDATE
        put("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Filme f = gson.fromJson(req.body(), Filme.class);
                f.setId(id);
                
                if (filmeDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return "{\"erro\":\"Filme não encontrado\"}";
                }
                
                filmeDAO.atualizar(f);
                return gson.toJson(f);
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao atualizar filme\"}";
            }
        });

        // DELETE
        delete("/filmes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                
                if (filmeDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return "{\"erro\":\"Filme não encontrado\"}";
                }
                
                filmeDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Filme deletado com sucesso\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao deletar filme\"}";
            }
        });

        // ======================
        // LOCAÇÕES
        // ======================
        
        // CREATE
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

                // Validações
                if (dataLocacao.isAfter(dataPrevista)) {
                    res.status(400);
                    return "{\"erro\":\"Data de devolução deve ser posterior à data de locação\"}";
                }

                // Verifica se o cliente existe
                Cliente cliente = clienteDAO.buscarPorId(idCliente);
                if (cliente == null) {
                    res.status(404);
                    return "{\"erro\":\"Cliente não encontrado\"}";
                }

                // Busca o filme
                Filme filme = filmeDAO.buscarPorId(idFilme);
                if (filme == null) {
                    res.status(404);
                    return "{\"erro\":\"Filme não encontrado\"}";
                }

                if (filme.getQuantidadeDisponivel() <= 0) {
                    res.status(400);
                    return "{\"erro\":\"Filme indisponível no momento\"}";
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
                return "{\"erro\":\"Erro ao criar locação: " + e.getMessage() + "\"}";
            }
        });

        // READ ALL
        get("/locacoes", (req, res) -> {
            try {
                return gson.toJson(locacaoDAO.buscarTodas());
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar locações\"}";
            }
        });

        // READ ONE
        get("/locacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Locacao l = locacaoDAO.buscarPorId(id);
                
                if (l == null) {
                    res.status(404);
                    return "{\"erro\":\"Locação não encontrada\"}";
                }
                return gson.toJson(l);
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"erro\":\"ID inválido\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao buscar locação\"}";
            }
        });

        // DEVOLUÇÃO (UPDATE)
        put("/locacoes/:id/devolver", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Locacao locacao = locacaoDAO.buscarPorId(id);
                
                if (locacao == null) {
                    res.status(404);
                    return "{\"erro\":\"Locação não encontrada\"}";
                }
                
                if (locacao.getDataDevolucao() != null) {
                    res.status(400);
                    return "{\"erro\":\"Filme já foi devolvido\"}";
                }
                
                LocalDate dataDevolucao = LocalDate.now();
                locacao.setDataDevolucao(dataDevolucao);
                locacao.setStatus("devolvido");
                
                locacaoDAO.atualizar(locacao);
                
                // Aumenta a quantidade disponível do filme
                Filme filme = filmeDAO.buscarPorId(locacao.getIdFilme());
                filmeDAO.atualizarQuantidade(
                    locacao.getIdFilme(), 
                    filme.getQuantidadeDisponivel() + 1
                );
                
                return gson.toJson(locacao);
                
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao devolver filme\"}";
            }
        });

        // DELETE
        delete("/locacoes/:id", (req, res) -> {
            try {
                int id = Integer.parseInt(req.params(":id"));
                Locacao l = locacaoDAO.buscarPorId(id);
                
                if (l == null) {
                    res.status(404);
                    return "{\"erro\":\"Locação não encontrada\"}";
                }
                
                // Se a locação ainda não foi devolvida, devolve o filme
                if (l.getDataDevolucao() == null) {
                    Filme filme = filmeDAO.buscarPorId(l.getIdFilme());
                    filmeDAO.atualizarQuantidade(
                        l.getIdFilme(), 
                        filme.getQuantidadeDisponivel() + 1
                    );
                }
                
                locacaoDAO.deletar(id);
                res.status(200);
                return "{\"mensagem\":\"Locação deletada com sucesso\"}";
            } catch (Exception e) {
                e.printStackTrace();
                res.status(500);
                return "{\"erro\":\"Erro ao deletar locação\"}";
            }
        });

        System.out.println("Rotas da API carregadas com sucesso!");
        System.out.println("Servidor rodando em http://localhost:4567");
    }
}