package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Locacao;
import util.ConnectionFactory;

public class LocacaoDAO {

    // =====================================================================
    // BUSCAR TODAS
    // =====================================================================
    public List<Locacao> buscarTodas() {
        List<Locacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM locacao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Locacao l = new Locacao();

                l.setId(rs.getInt("id"));
                l.setIdFilme(rs.getInt("idFilme"));
                l.setIdCliente(rs.getInt("idCliente"));
                l.setDataLocacao(rs.getDate("dataLocacao").toLocalDate());
                l.setDataPrevistaDevolucao(rs.getDate("dataPrevistaDevolucao").toLocalDate());

                Date devolucao = rs.getDate("dataDevolucao");
                if (devolucao != null) {
                    l.setDataDevolucao(devolucao.toLocalDate());
                }

                l.setStatus(rs.getString("status"));
                l.setValorDiaria(rs.getDouble("valorDiaria"));

                lista.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================================
    // BUSCAR POR ID
    // =====================================================================
    public Locacao buscarPorId(int id) {
        Locacao l = null;
        String sql = "SELECT * FROM locacao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                l = new Locacao();

                l.setId(rs.getInt("id"));
                l.setIdFilme(rs.getInt("idFilme"));
                l.setIdCliente(rs.getInt("idCliente"));
                l.setDataLocacao(rs.getDate("dataLocacao").toLocalDate());
                l.setDataPrevistaDevolucao(rs.getDate("dataPrevistaDevolucao").toLocalDate());

                Date devolucao = rs.getDate("dataDevolucao");
                if (devolucao != null) {
                    l.setDataDevolucao(devolucao.toLocalDate());
                }

                l.setStatus(rs.getString("status"));
                l.setValorDiaria(rs.getDouble("valorDiaria"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }

    // =====================================================================
    // INSERIR
    // =====================================================================
    public void inserir(Locacao l) {

        String sql = """
            INSERT INTO locacao (
                idFilme, idCliente, dataLocacao,
                dataPrevistaDevolucao, dataDevolucao,
                status, valorDiaria
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, l.getIdFilme());
            stmt.setInt(2, l.getIdCliente());
            stmt.setDate(3, Date.valueOf(l.getDataLocacao()));
            stmt.setDate(4, Date.valueOf(l.getDataPrevistaDevolucao()));

            if (l.getDataDevolucao() != null) {
                stmt.setDate(5, Date.valueOf(l.getDataDevolucao()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setString(6, l.getStatus());
            stmt.setDouble(7, l.getValorDiaria());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                l.setId(keys.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // REGISTRAR DEVOLUÇÃO
    // =====================================================================
    public void registrarDevolucao(int id, java.time.LocalDate data, String status) {

        String sql = "UPDATE locacao SET dataDevolucao = ?, status = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(data));
            stmt.setString(2, status);
            stmt.setInt(3, id);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // DELETAR
    // =====================================================================
    public void deletar(int id) {

        String sql = "DELETE FROM locacao WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
