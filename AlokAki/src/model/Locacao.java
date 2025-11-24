package model;

import java.time.LocalDate;

    public class Locacao {
    private int id;
    private int idFilme;
    private String nomeCliente;
    private LocalDate dataLocacao;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal; // pode ser null
    private double valorDiaria;
    private String status; // ATIVA, ATRASADA, FINALIZADA

    public Locacao() {}

    public Locacao(int id, int idFilme, String nomeCliente, LocalDate dataLocacao,
                   LocalDate dataPrevistaDevolucao, LocalDate dataDevolucaoReal,
                   double valorDiaria, String status) {
        this.id = id;
        this.idFilme = idFilme;
        this.nomeCliente = nomeCliente;
        this.dataLocacao = dataLocacao;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.valorDiaria = valorDiaria;
        this.status = status;
    }

    // GETTERS E SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFilme() { return idFilme; }
    public void setIdFilme(int idFilme) { this.idFilme = idFilme; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { this.dataLocacao = dataLocacao; }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }

    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

