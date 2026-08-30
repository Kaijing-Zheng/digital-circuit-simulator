import './App.css'

function App() {
  return (
    <div className="app">
      <header className="header">
        <h1>Digital Circuit Simulator</h1>
        <button className="simulate-button">
          Simulate
        </button>
      </header>

      <div className="main-layout">

        <aside className="sidebar">
          <h2>Components</h2>

          <button>Input</button>
          <button>AND</button>
          <button>OR</button>
          <button>NOT</button>
          <button>XOR</button>
          <button>NAND</button>
          <button>NOR</button>
        </aside>

        <main className="workspace">
          <p>Drag components here to build a circuit</p>
        </main>

      </div>
    </div>
  )
}

export default App