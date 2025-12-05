import { useState, useEffect } from "react";
import { fetchLocacoes, criarLocacao, fetchClientes, fetchFilmes, devolverLocacao } from "../api/Api.js";

export default function Locacoes() {
    const [locacoes, setLocacoes] = useState([]);
    const [clientes, setClientes] = useState([]);
    const [filmes, setFilmes] = useState([]);
    const [idCliente, setIdCliente] = useState("");
    const [idFilme, setIdFilme] = useState("");
    const [prazo, setPrazo] = useState("");
    const [valorDiaria, setValorDiaria] = useState("");

    useEffect(() => {
        atualizarLista();
        fetchClientes().then(setClientes);
        fetchFilmes().then(setFilmes);
    }, []);

    const atualizarLista = async () => {
        const dados = await fetchLocacoes();
        setLocacoes(dados);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const hoje = new Date().toISOString().split("T")[0]; // yyyy-mm-dd
        await criarLocacao({
            idCliente: parseInt(idCliente),
            idFilme: parseInt(idFilme),
            dataLocacao: hoje,
            dataPrevistaDevolucao: new Date(new Date().setDate(new Date().getDate() + parseInt(prazo))).toISOString().split("T")[0],
            status: "ATIVA",
            valorDiaria: parseFloat(valorDiaria)
        });
        setIdCliente(""); setIdFilme(""); setPrazo(""); setValorDiaria("");
        atualizarLista();
    };

    const handleDevolver = async (id) => {
        const hoje = new Date().toISOString().split("T")[0];
        await devolverLocacao(id, hoje);
        atualizarLista();
    };

    return (
        <div>
            <h2>Locações</h2>
            <form onSubmit={handleSubmit}>
                <select value={idCliente} onChange={e => setIdCliente(e.target.value)} required>
                    <option value="">Selecione o Cliente</option>
                    {clientes.map(c => <option key={c.id} value={c.id}>{c.nome}</option>)}
                </select>
                <select value={idFilme} onChange={e => setIdFilme(e.target.value)} required>
                    <option value="">Selecione o Filme</option>
                    {filmes.map(f => <option key={f.id} value={f.id}>{f.titulo}</option>)}
                </select>
                <input placeholder="Prazo (dias)" type="number" value={prazo} onChange={e => setPrazo(e.target.value)} required />
                <input placeholder="Valor diária" type="number" value={valorDiaria} onChange={e => setValorDiaria(e.target.value)} required />
                <button type="submit">Adicionar Locação</button>
            </form>

            <ul>
                {locacoes.map(l => (
                    <li key={l.id}>
                        Cliente {l.idCliente} alugou Filme {l.idFilme} - Status: {l.status}
                        {l.status === "ATIVA" && <button onClick={() => handleDevolver(l.id)}>Devolver</button>}
                    </li>
                ))}
            </ul>
        </div>
    );
}
