package model;

import java.time.LocalDate;

public class Locacao {

    private int id;
    private int idFilme;
    private int idCliente;
    private LocalDate dataLocacao;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private String status;
    private double valorDiaria;

    // Construtor vazio (Gson precisa)
    public Locacao() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFilme() { return idFilme; }
    public void setIdFilme(int idFilme) { this.idFilme = idFilme; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { 
        this.dataLocacao = dataLocacao; 
    }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        // Removida a validação que gerava erro 500
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { 
        this.dataDevolucao = dataDevolucao; 
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        // Removi validação que quebrava no JSON
        this.status = status;
    }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    @Override
    public String toString() {
        return "Locacao{id=" + id + ", idFilme=" + idFilme + ", idCliente=" + idCliente +
               ", dataLocacao=" + dataLocacao + ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
               ", dataDevolucao=" + dataDevolucao + ", status='" + status + "', valorDiaria=" + valorDiaria + "}";
    }
}
