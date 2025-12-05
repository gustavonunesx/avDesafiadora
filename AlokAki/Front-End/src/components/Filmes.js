import { useState, useEffect } from "react";
import { fetchFilmes, criarFilme } from "../api/Api.js";

export default function Filmes() {
    const [filmes, setFilmes] = useState([]);
    const [titulo, setTitulo] = useState("");
    const [genero, setGenero] = useState("");
    const [ano, setAno] = useState("");
    const [quantidade, setQuantidade] = useState("");

    useEffect(() => {
        atualizarLista();
    }, []);

    const atualizarLista = async () => {
        const dados = await fetchFilmes();
        setFilmes(dados);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await criarFilme({
            titulo,
            genero,
            anoLancamento: parseInt(ano),
            quantidadeTotal: parseInt(quantidade),
            quantidadeDisponivel: parseInt(quantidade),
        });
        setTitulo(""); setGenero(""); setAno(""); setQuantidade("");
        atualizarLista();
    };

    return (
        <div>
            <h2>Filmes</h2>
            <form onSubmit={handleSubmit}>
                <input placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} required />
                <input placeholder="Gênero" value={genero} onChange={e => setGenero(e.target.value)} required />
                <input placeholder="Ano" type="number" value={ano} onChange={e => setAno(e.target.value)} required />
                <input placeholder="Quantidade" type="number" value={quantidade} onChange={e => setQuantidade(e.target.value)} required />
                <button type="submit">Adicionar Filme</button>
            </form>

            <ul>
                {filmes.map(f => (
                    <li key={f.id}>{f.titulo} ({f.genero}) - Disponível: {f.quantidadeDisponivel}</li>
                ))}
            </ul>
        </div>
    );
}
