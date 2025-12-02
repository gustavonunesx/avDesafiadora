package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Filme;
import util.ConnectionFactory;

public class FilmeDAO {

    // ============================================================
    // BUSCAR TODOS
    // ============================================================
    public List<Filme> buscarTodos() {
        List<Filme> lista = new ArrayList<>();

        String sql = "SELECT * FROM filme";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Filme f = new Filme();
                f.setId(rs.getInt("id"));
                f.setTitulo(rs.getString("titulo"));
                f.setGenero(rs.getString("genero"));
                f.setAnoLancamento(rs.getInt("anoLancamento"));
                f.setQuantidadeTotal(rs.getInt("quantidadeTotal"));
                f.setQuantidadeDisponivel(rs.getInt("quantidadeDisponivel"));
                lista.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ============================================================
    // BUSCAR POR ID
    // ============================================================
    public Filme buscarPorId(int id) {
        Filme f = null;

        String sql = "SELECT * FROM filme WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                f = new Filme();
                f.setId(rs.getInt("id"));
                f.setTitulo(rs.getString("titulo"));
                f.setGenero(rs.getString("genero"));
                f.setAnoLancamento(rs.getInt("anoLancamento"));
                f.setQuantidadeTotal(rs.getInt("quantidadeTotal"));
                f.setQuantidadeDisponivel(rs.getInt("quantidadeDisponivel"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ============================================================
    // INSERIR
    // ============================================================
    public void inserir(Filme filme) {
        String sql = """
            INSERT INTO filme (
                titulo, genero, anoLancamento,
                quantidadeTotal, quantidadeDisponivel
            ) VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getGenero());
            stmt.setInt(3, filme.getAnoLancamento());
            stmt.setInt(4, filme.getQuantidadeTotal());
            stmt.setInt(5, filme.getQuantidadeDisponivel());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                filme.setId(keys.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================
    public void atualizar(Filme filme) {
        String sql = """
            UPDATE filme SET 
                titulo = ?, 
                genero = ?, 
                anoLancamento = ?, 
                quantidadeTotal = ?, 
                quantidadeDisponivel = ?
            WHERE id = ?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getGenero());
            stmt.setInt(3, filme.getAnoLancamento());
            stmt.setInt(4, filme.getQuantidadeTotal());
            stmt.setInt(5, filme.getQuantidadeDisponivel());
            stmt.setInt(6, filme.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // DELETAR
    // ============================================================
    public void deletar(int id) {
        String sql = "DELETE FROM filme WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // ATUALIZAR QUANTIDADE DISPONÍVEL (locação/devolução)
    // ============================================================
    public void atualizarQuantidade(int idFilme, int novaQtd) {
        String sql = "UPDATE filme SET quantidadeDisponivel = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQtd);
            stmt.setInt(2, idFilme);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
