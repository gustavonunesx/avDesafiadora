// API base (ajuste se a sua API estiver em outra porta)
const API_BASE = 'http://localhost:4567';

const $ = sel => document.querySelector(sel);
const $$ = sel => document.querySelectorAll(sel);

const views = {
  filmes: $('#view-filmes'),
  novoFilme: $('#view-novo-filme'),
  novaLocacao: $('#view-nova-locacao'),
  locacoes: $('#view-locacoes')
};

const btnFilmes = $('#btn-filmes');
const btnNovoFilme = $('#btn-novo-filme');
const btnNovaLocacao = $('#btn-nova-locacao');
const btnLocacoes = $('#btn-locacoes');

const toastEl = $('#toast');
function toast(msg, ms=2500){ toastEl.textContent = msg; toastEl.classList.remove('hidden'); setTimeout(()=>toastEl.classList.add('hidden'), ms); }

// Navegação simples
function show(view){
  Object.values(views).forEach(v => v.classList.add('hidden'));
  view.classList.remove('hidden');
  $$('.nav-btn').forEach(b => b.classList.remove('active'));
  if(view === views.filmes) btnFilmes.classList.add('active');
  if(view === views.novoFilme) btnNovoFilme.classList.add('active');
  if(view === views.novaLocacao) btnNovaLocacao.classList.add('active');
  if(view === views.locacoes) btnLocacoes.classList.add('active');
}

// Eventos nav
btnFilmes.onclick = () => { show(views.filmes); loadFilmes(); };
btnNovoFilme.onclick = () => { show(views.novoFilme); };
btnNovaLocacao.onclick = () => { show(views.novaLocacao); loadFilmesIntoSelect(); };
btnLocacoes.onclick = () => { show(views.locacoes); loadLocacoes(); };

// FILMES
async function loadFilmes(){
  const list = $('#filmes-list');
  list.innerHTML = '<em>Carregando...</em>';
  try {
    const res = await fetch(`${API_BASE}/filmes`);
    const filmes = await res.json();
    if(!Array.isArray(filmes)) filmes = [];
    list.innerHTML = filmes.map(f => `
      <div class="card">
        <h3>${escapeHtml(f.titulo)}</h3>
        <p>${escapeHtml(f.genero)} • ${f.anoLancamento || ''}</p>
        <div class="meta">
          <span class="badge">Disponível: ${f.quantidadeDisponivel}</span>
          <div>
            <button class="btn" onclick="preencherLocacao(${f.id}, '${escapeJs(f.titulo)}')">Alugar</button>
          </div>
        </div>
      </div>
    `).join('');
  } catch(err){
    list.innerHTML = '<span class="muted">Erro ao carregar filmes</span>';
    console.error(err);
  }
}

// preenche select / ou abre form de locacao com filme
function preencherLocacao(id, titulo){
  show(views.novaLocacao);
  loadFilmesIntoSelect().then(()=>{
    const sel = $('#select-filme');
    sel.value = id;
  });
  toast(`Preparando locação para "${titulo}"`);
}

async function loadFilmesIntoSelect(){
  const sel = $('#select-filme');
  sel.innerHTML = '<option>Carregando...</option>';
  try {
    const res = await fetch(`${API_BASE}/filmes`);
    const filmes = await res.json();
    sel.innerHTML = filmes.map(f => `<option value="${f.id}">${escapeHtml(f.titulo)} (${f.quantidadeDisponivel})</option>`).join('');
  } catch(e){
    sel.innerHTML = '<option value="">Erro ao carregar</option>';
  }
}

// FORM criar filme
$('#form-filme').addEventListener('submit', async (ev)=>{
  ev.preventDefault();
  const form = ev.target;
  const data = {
    titulo: form.titulo.value.trim(),
    genero: form.genero.value.trim(),
    anoLancamento: parseInt(form.anoLancamento.value) || null,
    quantidadeTotal: parseInt(form.quantidadeTotal.value) || 1,
    quantidadeDisponivel: parseInt(form.quantidadeTotal.value) || 1
  };
  try {
    const res = await fetch(`${API_BASE}/filmes`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(data)
    });
    if(res.status === 201){ form.reset(); toast('Filme criado'); loadFilmes(); show(views.filmes); }
    else { const j = await res.text(); toast('Erro ao criar'); console.error(j); }
  } catch(err){ console.error(err); toast('Erro de rede'); }
});

// FORM criar locacao
$('#form-locacao').addEventListener('submit', async (ev)=>{
  ev.preventDefault();
  const form = ev.target;
  const idFilme = parseInt(form.idFilme.value);
  const dataLocacao = form.dataLocacao.value;
  const prazoDias = parseInt(form.prazoDias.value) || 3;
  const valorDiaria = parseFloat(form.valorDiaria.value) || 0;

  // calcular dataPrevistaDevolucao simples: +prazoDias
  const dt = new Date(dataLocacao);
  dt.setDate(dt.getDate() + prazoDias);
  const dataPrevista = dt.toISOString().slice(0,10);

  const payload = {
    idFilme,
    dataLocacao,
    dataPrevistaDevolucao: dataPrevista,
    dataDevolucao: null,
    status: "ATIVA",
    valorDiaria
  };

  try {
    const res = await fetch(`${API_BASE}/locacoes`, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(payload)
    });
    if(res.status === 201){
      toast('Locação registrada');
      form.reset();
      show(views.locacoes);
      loadLocacoes();
    } else {
      const txt = await res.text();
      console.error(txt);
      toast('Erro ao registrar locação');
    }
  } catch(e){ console.error(e); toast('Erro de rede'); }
});

// LISTAR LOCAÇÕES
async function loadLocacoes(){
  const box = $('#locacoes-list');
  box.innerHTML = '<em>Carregando...</em>';
  try {
    const res = await fetch(`${API_BASE}/locacoes`);
    const locs = await res.json();
    if(!Array.isArray(locs)) locs = [];
    box.innerHTML = locs.map(l => `
      <div class="locacao-card">
        <div class="loc-info">
          <div>
            <div class="title">#${l.id} — Filme ID: ${l.idFilme}</div>
            <div class="loc-meta">Locado: ${l.dataLocacao} • Prevista: ${l.dataPrevistaDevolucao} • Data devolução: ${l.dataDevolucao || '—'}</div>
          </div>
        </div>
        <div>
          <div style="text-align:right;color:var(--muted);">R$ ${Number(l.valorDiaria).toFixed(2)}</div>
          ${l.status !== 'FINALIZADA' ? `<button class="btn" onclick="devolver(${l.id})">Devolver</button>` : `<span class="badge">FINALIZADA</span>`}
        </div>
      </div>
    `).join('');
  } catch(e){ box.innerHTML = '<div>Erro ao carregar</div>'; console.error(e); }
}

// devolver locação (usa data atual)
async function devolver(id){
  try {
    const res = await fetch(`${API_BASE}/locacoes/${id}/devolver`, { method: 'PUT' });
    const data = await res.json();
    toast(`Devolvido — multa R$ ${Number(data.multa || 0).toFixed(2)}`);
    loadLocacoes();
    loadFilmes();
  } catch(e){ console.error(e); toast('Erro ao devolver'); }
}

// helpers
function escapeHtml(s){ if(!s) return ''; return s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }
function escapeJs(s){ return (s||'').replace(/'/g,"\\'").replace(/"/g,'\"'); }

// inicializa
show(views.filmes);
loadFilmes();
