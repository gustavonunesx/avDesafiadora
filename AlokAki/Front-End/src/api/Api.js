const BASE_URL = "http://localhost:4567";

export async function fetchClientes() {
    const res = await fetch(`${BASE_URL}/clientes`);
    return res.json();
}

export async function criarCliente(cliente) {
    const res = await fetch(`${BASE_URL}/clientes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cliente),
    });
    return res.json();
}

export async function fetchFilmes() {
    const res = await fetch(`${BASE_URL}/filmes`);
    return res.json();
}

export async function criarFilme(filme) {
    const res = await fetch(`${BASE_URL}/filmes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(filme),
    });
    return res.json();
}

export async function fetchLocacoes() {
    const res = await fetch(`${BASE_URL}/locacoes`);
    return res.json();
}

export async function criarLocacao(locacao) {
    const res = await fetch(`${BASE_URL}/locacoes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(locacao),
    });
    return res.json();
}

export async function devolverLocacao(id, dataDevolucao) {
    const res = await fetch(`${BASE_URL}/locacoes/${id}/devolver`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ dataDevolucao, status: "FINALIZADA" }),
    });
    return res.json();
}
