const API = "http://localhost:4567";

// --- Atalhos ---
const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

// --- NAV ---
$$(".nav-btn").forEach(btn => {
  btn.onclick = () => {
    $$(".nav-btn").forEach(b => b.classList.remove("active"));
    btn.classList.add("active");

    const view = btn.dataset.view;
    $$(".view").forEach(v => v.classList.add("hidden"));
    $("#" + view).classList.remove("hidden");
  };
});

// --- TOAST ---
function toast(msg) {
  const t = $("#toast");
  t.textContent = msg;
  t.classList.remove("hidden");
  setTimeout(() => t.classList.add("hidden"), 2500);
}

// --- LISTAR FILMES ---
async function carregarFilmes() {
  const res = await fetch(`${API}/filmes`);
  const filmes = await res.json();

  const list = $("#filmes-list");
  list.innerHTML = "";

  filmes.forEach(f => {
    const card = document.createElement("div");
    card.className = "card";
    card.innerHTML = `
      <h3>${f.titulo}</h3>
      <p>${f.genero}</p>
      <p>Ano: ${f.anoLancamento}</p>
      <p>Disponíveis: ${f.quantidadeDisponivel}</p>
    `;
    list.appendChild(card);
  });

  // preencher select
  const sel = $("#select-filme");
  sel.innerHTML = filmes.map(f =>
    `<option value="${f.id}">${f.titulo} — Disponíveis: ${f.quantidadeDisponivel}</option>`
  ).join("");
}

// --- CADASTRAR FILME ---
$("#form-filme").onsubmit = async (e) => {
  e.preventDefault();

  const data = Object.fromEntries(new FormData(e.target));

  const res = await fetch(`${API}/filmes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });

  if (res.ok) {
    toast("Filme cadastrado!");
    e.target.reset();
    carregarFilmes();
  } else {
    toast("Erro ao cadastrar filme.");
  }
};

// --- REGISTRAR LOCAÇÃO (C/ CRIAÇÃO DE CLIENTE) ---
$("#form-locacao").onsubmit = async (e) => {
  e.preventDefault();

  const idFilme = $("#select-filme").value;
  const nome = $("#cliente-nome").value.trim();
  const telefone = $("#cliente-telefone").value.trim();
  const dataLocacao = $("#data-locacao").value;
  const prazo = $("#prazo").value;
  const valor = $("#valor-diaria").value;

  if (!nome || !telefone) {
    toast("Preencha nome e telefone!");
    return;
  }

  // 1) Criar cliente
  const respCliente = await fetch(`${API}/clientes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      nome,
      telefone,
      email: ""
    })
  });

  if (!respCliente.ok) {
    toast("Erro ao cadastrar cliente.");
    return;
  }

  const cliente = await respCliente.json();
  const idCliente = cliente.id;

  // 2) Criar locação
  const respLocacao = await fetch(`${API}/locacoes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      idCliente,
      idFilme,
      dataLocacao,
      prazo,
      valor
    })
  });

  if (!respLocacao.ok) {
    toast("Erro ao registrar locação.");
    return;
  }

  toast("Locação registrada com sucesso!");
  e.target.reset();
  carregarLocacoes();
};

// --- LISTAR LOCAÇÕES ---
async function carregarLocacoes() {
  const resp = await fetch(`${API}/locacoes`);
  const locs = await resp.json();

  const list = $("#locacoes-list");
  list.innerHTML = "";

  locs.forEach(l => {
    const div = document.createElement("div");
    div.className = "card";
    div.innerHTML = `
      <h3>Cliente: ${l.cliente.nome}</h3>
      <p>Filme: ${l.filme.titulo}</p>
      <p>Data: ${l.dataLocacao}</p>
      <p>Prazo: ${l.prazoDias} dias</p>
    `;
    list.appendChild(div);
  });
}

// carregar dados iniciais
carregarFilmes();
carregarLocacoes();
