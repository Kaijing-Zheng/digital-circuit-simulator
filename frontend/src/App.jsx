import CircuitComponent from './CircuitComponent'

import CircuitWire from './CircuitWire'

import { useState } from 'react'
import './App.css'

function App() {
  const [components, setComponents] = useState([])

  const [connections, setConnections] = useState([])
  const [selectedOutput, setSelectedOutput] = useState(null)

  function handleOutputClick(componentId) {
    setSelectedOutput(componentId)
  }

  function handleInputClick(destinationId, inputIndex) {
    if (selectedOutput === null) {
      return
    }

    const newConnection = {
      sourceId: selectedOutput,
      destinationId: destinationId,
      inputIndex: inputIndex,
    }

    setConnections([...connections, newConnection])
    setSelectedOutput(null)
  }

  function addComponent(type) {
    const newComponent = {
      id: Date.now(),
      type: type,
      x: 100 + components.length * 20,
      y: 100 + components.length * 20,
    }

    setComponents([...components, newComponent])
  }

  function moveComponent(id, x, y) {
    setComponents(
      components.map((component) => {
        if (component.id === id) {
          return {
            ...component,
            x: x,
            y: y,
          }
        }

        return component
      })
    )
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

          <svg className="wire-layer">
            {connections.map((connection, index) => {
              const source = components.find(
                component => component.id === connection.sourceId
              )

              const destination = components.find(
                component => component.id === connection.destinationId
              )

              if (!source || !destination) {
                return null
              }

              return (
                <CircuitWire
                  key={index}
                  source={source}
                  destination={destination}
                  inputIndex={connection.inputIndex}
                />
              )
            })}
          </svg>

          {components.map((component) => (
            <CircuitComponent
              key={component.id}
              component={component}
              onMove={moveComponent}
              onOutputClick={handleOutputClick}
              onInputClick={handleInputClick}
            />
          ))}

          <div className="connection-debug">
            Connections: {connections.length}
          </div>
        </main>
      </div>
    </div>
  )
}

export default App