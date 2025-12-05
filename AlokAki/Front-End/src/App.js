import Clientes from "./components/Clientes";
import Filmes from "./components/Filmes";
import Locacoes from "./components/Locacoes";
import "./App.css";

function App() {
  return (
    <div className="App">
      <h1>Locadora React + Java</h1>
      <Clientes />
      <Filmes />
      <Locacoes />
    </div>
  );
}

export default App;
