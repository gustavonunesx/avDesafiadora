package api;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Filter;

import dao.ProdutoDAO;
import dao.CategoriaDAO;
import model.Produto;
import model.Categoria;

import com.google.gson.Gson;

public class ApiProduto {

    // instancia do DAO e o GSON
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private static final Gson gson = new Gson();

    // constante para garantir que todas as respostas sejam JSON
    private static final String APPLICATION_JSON = "application/json";

    public static void main(String[] args) {

        // configuração do Servidor
        port(4567); // Define a porta da API. Acesso via http://localhost:4567

        // filtro para definir o tipo de conteúdo como JSON
        after(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.type(APPLICATION_JSON);
            }
        });

        // GET /produtos - Buscar todos
        get("/produtos", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                return gson.toJson(produtoDAO.buscarTodos());
            }
        });

        // GET /produtos/:id - Buscar por ID
        get("/produtos/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    // converter o parâmetro da URL (String) para Long, que é o tipo do ID
                    Long id = Long.parseLong(request.params(":id"));

                    Produto produto = produtoDAO.buscarPorId(id); // Usa o Long ID

                    if (produto != null) {
                        return gson.toJson(produto);
                    } else {
                        response.status(404); // Not Found
                        return "{\"mensagem\": \"Produto com ID " + id + " não encontrado\"}";
                    }
                } catch (NumberFormatException e) {
                    response.status(400); // Bad Request
                    return "{\"mensagem\": \"Formato de ID inválido.\"}";
                }
            }
        });

        // POST /produtos - Criar novo produto
        post("/produtos", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Produto novoProduto = gson.fromJson(request.body(), Produto.class);
                    produtoDAO.inserir(novoProduto);

                    response.status(201); // Created
                    return gson.toJson(novoProduto);
                } catch (Exception e) {
                    response.status(500);
                    System.err.println("Erro ao processar requisição POST: " + e.getMessage());
                    e.printStackTrace();
                    return "{\"mensagem\": \"Erro ao criar produto.\"}";
                }
            }
        });

        // PUT /produtos/:id - Atualizar produto existente
        put("/produtos/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (produtoDAO.buscarPorId(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"Produto não encontrado para atualização.\"}";
                    }

                    Produto produtoParaAtualizar = gson.fromJson(request.body(), Produto.class);
                    produtoParaAtualizar.setId(id); // garante que o ID da URL seja usado

                    produtoDAO.atualizar(produtoParaAtualizar);

                    response.status(200); // OK
                    return gson.toJson(produtoParaAtualizar);

                } catch (NumberFormatException e) {
                    response.status(400); // Bad Request
                    return "{\"mensagem\": \"Formato de ID inválido.\"}";
                } catch (Exception e) {
                    response.status(500);
                    System.err.println("Erro ao processar requisição PUT: " + e.getMessage());
                    e.printStackTrace();
                    return "{\"mensagem\": \"Erro ao atualizar produto.\"}";
                }
            }
        });

        // DELETE /produtos/:id - Deletar um produto
        delete("/produtos/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id")); // Usa Long

                    if (produtoDAO.buscarPorId(id) == null) {
                        response.status(404);
                        return "{\"mensagem\": \"Produto não encontrado para exclusão.\"}";
                    }

                    produtoDAO.deletar(id); // Usa o Long ID

                    response.status(204); // No Content
                    return ""; // Corpo vazio

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\": \"Formato de ID inválido.\"}";
                }
            }
        });

        // GET /categorias - Buscar todas
        get("/categorias", (request, response) -> gson.toJson(categoriaDAO.buscarTodos()));

        // GET /categorias/:id - Buscar por ID
        get("/categorias/:id", (request, response) -> {
            try {
                Long id = Long.parseLong(request.params(":id"));
                Categoria categoria = categoriaDAO.buscarPorId(id);

                if (categoria != null) {
                    return gson.toJson(categoria);
                } else {
                    response.status(404);
                    return "{\"mensagem\": \"Categoria com ID " + id + " não encontrada\"}";
                }
            } catch (NumberFormatException e) {
                response.status(400);
                return "{\"mensagem\": \"Formato de ID inválido.\"}";
            }
        });

        // POST /categorias - Criar nova categoria
        // JSON esperado: { "nome": "Nova Categoria" }
        post("/categorias", (request, response) -> {
            try {
                Categoria novaCategoria = gson.fromJson(request.body(), Categoria.class);
                categoriaDAO.inserir(novaCategoria);

                response.status(201); // Created
                return gson.toJson(novaCategoria);
            } catch (Exception e) {
                response.status(500);
                e.printStackTrace();
                return "{\"mensagem\": \"Erro ao criar categoria.\"}";
            }
        });

        // PUT /categorias/:id - Atualizar categoria
        put("/categorias/:id", (request, response) -> {
            try {
                Long id = Long.parseLong(request.params(":id"));

                if (categoriaDAO.buscarPorId(id) == null) {
                    response.status(404);
                    return "{\"mensagem\": \"Categoria não encontrada para atualização.\"}";
                }

                Categoria categoriaParaAtualizar = gson.fromJson(request.body(), Categoria.class);
                categoriaParaAtualizar.setId(id); // Garante o ID da URL

                categoriaDAO.atualizar(categoriaParaAtualizar);

                response.status(200); // OK
                return gson.toJson(categoriaParaAtualizar);

            } catch (NumberFormatException e) {
                response.status(400);
                return "{\"mensagem\": \"Formato de ID inválido.\"}";
            } catch (Exception e) {
                response.status(500);
                e.printStackTrace();
                return "{\"mensagem\": \"Erro ao atualizar categoria.\"}";
            }
        });

        // DELETE /categorias/:id - Deletar categoria
        delete("/categorias/:id", (request, response) -> {
            try {
                Long id = Long.parseLong(request.params(":id"));

                if (categoriaDAO.buscarPorId(id) == null) {
                    response.status(404);
                    return "{\"mensagem\": \"Categoria não encontrada para exclusão.\"}";
                }

                categoriaDAO.deletar(id);

                response.status(204); // No Content
                return "";

            } catch (NumberFormatException e) {
                response.status(400);
                return "{\"mensagem\": \"Formato de ID inválido.\"}";
            } catch (Exception e) {
                // Captura específica para violação de chave estrangeira
                if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                    response.status(409); // Conflict
                    return "{\"mensagem\": \"Não é possível excluir a categoria. Ela está sendo usada por um ou mais produtos.\"}";
                }
                response.status(500);
                e.printStackTrace();
                return "{\"mensagem\": \"Erro ao deletar categoria.\"}";
            }
        });

        System.out.println("API de Produtos iniciada na porta 4567. Acesse: http://localhost:4567/produtos");
    }
}