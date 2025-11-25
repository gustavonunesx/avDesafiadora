package dao;

import java.util.ArrayList;
import java.util.List;
import model.Filme;

public class FilmeDAO {

    private static final List<Filme> filmes = new ArrayList<>();
    private static int proximoId = 1;

    public List<Filme> buscarTodos() {
        return filmes;
    }

    public Filme buscarPorId(int id) {
        return filmes.stream()
            .filter(f -> f.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void inserir(Filme filme) {
        filme.setId(proximoId++);
        filmes.add(filme);
    }

    public void atualizar(Filme filmeAtualizado) {
        Filme f = buscarPorId(filmeAtualizado.getId());
        if (f != null) {
            f.setTitulo(filmeAtualizado.getTitulo());
            f.setGenero(filmeAtualizado.getGenero());
            f.setQuantidadeDisponivel(filmeAtualizado.getQuantidadeDisponivel());
        }
    }

    public void deletar(int id) {
        filmes.removeIf(f -> f.getId() == id);
    }

    public void atualizarQuantidade(int idFilme, int novaQtd) {
        Filme f = buscarPorId(idFilme);
        if (f != null) {
            f.setQuantidadeDisponivel(novaQtd);
        }
    }
}
