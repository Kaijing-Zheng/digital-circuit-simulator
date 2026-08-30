import CircuitComponent from './CircuitComponent'
import CircuitWire from './CircuitWire'
import { useState } from 'react'
import './App.css'

function App() {
  const [components, setComponents] = useState([])
  const [connections, setConnections] = useState([])
  const [selectedOutput, setSelectedOutput] = useState(null)

  const [simulationResults, setSimulationResults] = useState({})

  function clearCircuit() {
    setComponents([])
    setConnections([])
    setSelectedOutput(null)
    setSimulationResults({})
  }

  function handleOutputClick(componentId) {
    console.log('Output clicked:', componentId)

    if (selectedOutput === componentId) {
      setSelectedOutput(null)
      return
    }

    setSelectedOutput(componentId)
  }

  function handleInputClick(destinationId, inputIndex) {
    if (selectedOutput === null) {
      return
    }

    if (selectedOutput === destinationId) {
      setSelectedOutput(null)
      return
    }

    const inputAlreadyConnected = connections.some(
      connection =>
        connection.destinationId === destinationId &&
        connection.inputIndex === inputIndex
    )

    if (inputAlreadyConnected) {
      setSelectedOutput(null)
      return
    }

    const newConnection = {
      sourceId: selectedOutput,
      destinationId,
      inputIndex,
    }

    setConnections([...connections, newConnection])
    setSelectedOutput(null)
  }

  function isInputConnected(componentId, inputIndex) {
    return connections.some(
      connection =>
        connection.destinationId === componentId &&
        connection.inputIndex === inputIndex
    )
  }

  function deleteConnection(indexToDelete) {
    setConnections(
      connections.filter((_, index) => index !== indexToDelete)
    )
  }

  function addComponent(type) {
    const countOfSameType = components.filter(
      component => component.type === type
    ).length

    const newComponent = {
      id: Date.now(),
      type,
      name: `${type}${countOfSameType + 1}`,
      value: false,
      x: 100 + components.length * 20,
      y: 100 + components.length * 20,
    }

    setComponents([...components, newComponent])
  }

  function deleteComponent(componentId) {
    setComponents(
      components.filter(
        component => component.id !== componentId
      )
    )

    setConnections(
      connections.filter(
        connection =>
          connection.sourceId !== componentId &&
          connection.destinationId !== componentId
      )
    )

    if (selectedOutput === componentId) {
      setSelectedOutput(null)
    }
  }

  function moveComponent(id, x, y) {
    setComponents(
      components.map(component => {
        if (component.id === id) {
          return {
            ...component,
            x,
            y,
          }
        }

        return component
      })
    )
  }

  function toggleInput(componentId) {
    setComponents(
      components.map(component => {
        if (component.id === componentId && component.type === 'INPUT') {
          return {
            ...component,
            value: !component.value,
          }
        }

        return component
      })
    )
  }

  async function simulateCircuit() {
    const inputRequests = components
      .filter(component => component.type === 'INPUT')
      .map(component => ({
        name: component.name,
        value: component.value,
      }))

    const gateRequests = components
      .filter(component => component.type !== 'INPUT')
      .map(component => ({
        name: component.name,
        type: component.type,
      }))

    const connectionRequests = connections.map(connection => {
      const source = components.find(
        component => component.id === connection.sourceId
      )

      const destination = components.find(
        component => component.id === connection.destinationId
      )

      return {
        source: source.name,
        destination: destination.name,
        inputIndex: connection.inputIndex,
      }
    })

    const outputNames = components
      .filter(component => component.type !== 'INPUT')
      .map(component => component.name)

    const requestBody = {
      inputs: inputRequests,
      gates: gateRequests,
      connections: connectionRequests,
      outputs: outputNames,
    }

    console.log('Sending simulation request:', requestBody)

    try {
      const response = await fetch('/api/simulate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      })

      if (!response.ok) {
        throw new Error('Simulation failed')
      }

      const result = await response.json()

      console.log('Simulation result:', result)

      setSimulationResults(result.outputs)
    } catch (error) {
      console.error('Simulation error:', error)
    }
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Digital Circuit Simulator</h1>

        <button 
          className="simulate-button"
          onClick={simulateCircuit}
        >
          Simulate
        </button>

        <button
          className="clear-button"
          onClick={clearCircuit}
        >
          Clear Circuit
        </button>
      </header>

      <div className="main-layout">
        <aside className="sidebar">
          <h2>Components</h2>

          <button onClick={() => addComponent('INPUT')}>
            Input
          </button>

          <button onClick={() => addComponent('AND')}>
            AND
          </button>

          <button onClick={() => addComponent('OR')}>
            OR
          </button>

          <button onClick={() => addComponent('NOT')}>
            NOT
          </button>

          <button onClick={() => addComponent('XOR')}>
            XOR
          </button>

          <button onClick={() => addComponent('NAND')}>
            NAND
          </button>

          <button onClick={() => addComponent('NOR')}>
            NOR
          </button>
        </aside>

        <main className="workspace">
          {components.length === 0 && (
            <p>Click a component to add it to the circuit</p>
          )}

          <svg className="wire-layer">
            {connections.map((connection, index) => {
              const source = components.find(
                component =>
                  component.id === connection.sourceId
              )

              const destination = components.find(
                component =>
                  component.id === connection.destinationId
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
                  onDelete={() => deleteConnection(index)}
                />
              )
            })}
          </svg>

          {components.map(component => (
            <CircuitComponent
              key={component.id}
              component={component}
              onMove={moveComponent}
              onOutputClick={handleOutputClick}
              onInputClick={handleInputClick}
              isOutputSelected={selectedOutput === component.id}
              isInputConnected={isInputConnected}
              onToggleInput={toggleInput}
              simulationValue={simulationResults[component.name]}
              onDeleteComponent={deleteComponent}
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