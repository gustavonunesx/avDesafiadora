package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Locacao;
import util.ConnectionFactory;

public class LocacaoDAO {

    // CREATE
    public void inserir(Locacao l) {
        String sql = "INSERT INTO locacao (id_filme, nome_cliente, data_locacao, data_prevista_devolucao, data_devolucao_real, valor_diaria, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, l.getIdFilme());
            stmt.setString(2, l.getNomeCliente());
            stmt.setDate(3, Date.valueOf(l.getDataLocacao()));
            stmt.setDate(4, Date.valueOf(l.getDataPrevistaDevolucao()));

            if (l.getDataDevolucaoReal() != null) {
                stmt.setDate(5, Date.valueOf(l.getDataDevolucaoReal()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setDouble(6, l.getValorDiaria());
            stmt.setString(7, l.getStatus());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ - Listar todas
    public List<Locacao> listar() {
        List<Locacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM locacao";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Locacao l = new Locacao(
                        rs.getInt("id_locacao"),
                        rs.getInt("id_filme"),
                        rs.getString("nome_cliente"),
                        rs.getDate("data_locacao").toLocalDate(),
                        rs.getDate("data_prevista_devolucao").toLocalDate(),
                        rs.getDate("data_devolucao_real") != null ? rs.getDate("data_devolucao_real").toLocalDate()
                                : null,
                        rs.getDouble("valor_diaria"),
                        rs.getString("status"));
                lista.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE - definir devolução
    public void registrarDevolucao(int idLocacao, LocalDate dataReal, String novoStatus) {
        String sql = "UPDATE locacao SET data_devolucao_real = ?, status = ? WHERE id_locacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataReal));
            stmt.setString(2, novoStatus);
            stmt.setInt(3, idLocacao);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ - Buscar por ID
    public Locacao buscarPorId(int id) {
        String sql = "SELECT * FROM locacao WHERE id_locacao = ?";
        Locacao l = null;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                l = new Locacao(
                        rs.getInt("id_locacao"),
                        rs.getInt("id_filme"),
                        rs.getString("nome_cliente"),
                        rs.getDate("data_locacao").toLocalDate(),
                        rs.getDate("data_prevista_devolucao").toLocalDate(),
                        rs.getDate("data_devolucao_real") != null ? rs.getDate("data_devolucao_real").toLocalDate()
                                : null,
                        rs.getDouble("valor_diaria"),
                        rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }
}
