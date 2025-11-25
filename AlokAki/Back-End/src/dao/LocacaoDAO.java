package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Locacao;

public class LocacaoDAO {

    private static final List<Locacao> locacoes = new ArrayList<>();
    private static int proximoId = 1;

    public List<Locacao> buscarTodas() {
        return locacoes;
    }

    public Locacao buscarPorId(int id) {
        return locacoes.stream()
            .filter(l -> l.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void inserir(Locacao locacao) {
        locacao.setId(proximoId++);
        locacoes.add(locacao);
    }

    public void deletar(int id) {
        locacoes.removeIf(l -> l.getId() == id);
    }

    public void registrarDevolucao(int id, LocalDate dataDev, String status) {
        Locacao l = buscarPorId(id);
        if (l != null) {
            l.setDataDevolucao(dataDev);
            l.setStatus(status);
        }
    }
}
