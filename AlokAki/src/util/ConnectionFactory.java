package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/movietime?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "senha_de_vc"; // ALTERE para sua senha

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
