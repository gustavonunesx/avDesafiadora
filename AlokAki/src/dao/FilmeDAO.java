package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Filme;
import util.ConnectionFactory;

public class FilmeDAO {

    // CREATE
    public void inserir(Filme f) {
        String sql = "INSERT INTO filme (titulo, genero, ano_lancamento, quantidade_total, quantidade_disponivel) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getTitulo());
            stmt.setString(2, f.getGenero());
            stmt.setInt(3, f.getAnoLancamento());
            stmt.setInt(4, f.getQuantidadeTotal());
            stmt.setInt(5, f.getQuantidadeDisponivel());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ - Listar todos
    public List<Filme> listar() {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filme";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Filme f = new Filme(
                    rs.getInt("id_filme"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano_lancamento"),
                    rs.getInt("quantidade_total"),
                    rs.getInt("quantidade_disponivel")
                );
                lista.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE - atualizar quantidade disponível
    public void atualizarQuantidade(int idFilme, int novaQtd) {
        String sql = "UPDATE filme SET quantidade_disponivel = ? WHERE id_filme = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQtd);
            stmt.setInt(2, idFilme);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ - Buscar por ID
    public Filme buscarPorId(int id) {
        String sql = "SELECT * FROM filme WHERE id_filme = ?";
        Filme f = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                f = new Filme(
                        rs.getInt("id_filme"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getInt("ano_lancamento"),
                        rs.getInt("quantidade_total"),
                        rs.getInt("quantidade_disponivel")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }
}
