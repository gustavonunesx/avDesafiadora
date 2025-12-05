import api.ApiLocadora;
import static spark.Spark.*;

public class App {
    
    public static void main(String[] args) {
        
        // Define a porta do servidor (padrão 4567)
        port(4567);
        
        // Inicializa as rotas da API
        ApiLocadora.iniciarRotas();
        
        System.out.println("===========================================");
        System.out.println("   Servidor rodando em: http://localhost:4567");
        System.out.println("===========================================");
    }
}