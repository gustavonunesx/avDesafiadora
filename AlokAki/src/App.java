import static spark.Spark.port;
import api.ApiLocadora;

public class App {

    public static void main(String[] args) {

        port(4567); // porta da API

        ApiLocadora.iniciarRotas();

        System.out.println("API da Locadora rodando em http://localhost:4567");
    }
}
