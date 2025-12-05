import { useState, useEffect } from "react";
import { fetchClientes, criarCliente } from "../api/Api.js";

export default function Clientes() {
    const [clientes, setClientes] = useState([]);
    const [nome, setNome] = useState("");
    const [telefone, setTelefone] = useState("");

    useEffect(() => {
        atualizarLista();
    }, []);

    const atualizarLista = async () => {
        const dados = await fetchClientes();
        setClientes(dados);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await criarCliente({ nome, telefone });
        setNome("");
        setTelefone("");
        atualizarLista();
    };

    return (
        <div>
            <h2>Clientes</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Nome"
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="Telefone"
                    value={telefone}
                    onChange={(e) => setTelefone(e.target.value)}
                />
                <button type="submit">Adicionar Cliente</button>
            </form>

            <ul>
                {clientes.map((c) => (
                    <li key={c.id}>{c.nome} - {c.telefone}</li>
                ))}
            </ul>
        </div>
    );
}
