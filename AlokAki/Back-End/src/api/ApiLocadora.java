package api;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
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
        post("/clientes", (req, res) -> {
            Cliente c = gson.fromJson(req.body(), Cliente.class);
            clienteDAO.inserir(c);
            res.status(201);
            return gson.toJson(c);
        });

        get("/clientes", (req, res) -> gson.toJson(clienteDAO.buscarTodos()));

        // ======================
        // FILMES
        // ======================
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

        // ======================
        // LOCAÇÕES
        // ======================
        get("/locacoes", (req, res) -> gson.toJson(locacaoDAO.buscarTodas()));

        
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

        System.out.println("Rotas da API carregadas com sucesso!");
    }
}
