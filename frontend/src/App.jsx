import { useState } from 'react'
import './App.css'

function App() {
  const [components, setComponents] = useState([])

  function addComponent(type) {
    const newComponent = {
      id: Date.now(),
      type: type,
      x: 300,
      y: 200,
    }

    setComponents([...components, newComponent])
  }

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

          <button onClick={() => addComponent('INPUT')}>Input</button>
          <button onClick={() => addComponent('AND')}>AND</button>
          <button onClick={() => addComponent('OR')}>OR</button>
          <button onClick={() => addComponent('NOT')}>NOT</button>
          <button onClick={() => addComponent('XOR')}>XOR</button>
          <button onClick={() => addComponent('NAND')}>NAND</button>
          <button onClick={() => addComponent('NOR')}>NOR</button>
        </aside>

        <main className="workspace">
          {components.length === 0 && (
            <p>Click a component to add it to the circuit</p>
          )}

          {components.map((component) => (
            <div
              key={component.id}
              className="circuit-component"
              style={{
                left: component.x,
                top: component.y,
              }}
            >
              {component.type}
            </div>
          ))}
        </main>
      </div>
    </div>
  )
}

export default App