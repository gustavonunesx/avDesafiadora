package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import util.ConnectionFactory;

public class ClienteDAO {

    // ============================================================
    // BUSCAR TODOS
    // ============================================================
    public List<Cliente> buscarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    public Cliente buscarPorId(int id) {
        Cliente c = null;
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    // ============================================================
    // INSERIR
    // ============================================================
    public void inserir(Cliente c) {
        String sql = "INSERT INTO cliente (nome, telefone) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================
    public void atualizar(Cliente c) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());
            stmt.setInt(3, c.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // DELETAR
    // ============================================================
    public void deletar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}