import CircuitComponent from './CircuitComponent'
import CircuitWire from './CircuitWire'
import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [components, setComponents] = useState([])
  const [connections, setConnections] = useState([])
  const [selectedOutput, setSelectedOutput] = useState(null)
  const [simulationResults, setSimulationResults] = useState({})
  const [statusMessage, setStatusMessage] = useState('')

  const [circuitName, setCircuitName] = useState('')
  const [savedCircuits, setSavedCircuits] = useState([])
  const [selectedCircuitId, setSelectedCircuitId] = useState('')

  const [truthTable, setTruthTable] = useState(null)

  useEffect(() => {
    fetchSavedCircuits()
  }, [])

  function clearCircuit() {
    setComponents([])
    setConnections([])
    setSelectedOutput(null)
    setSimulationResults({})
    setSelectedCircuitId('')
    setCircuitName('')
    setStatusMessage('')
  }

  function clearSimulationResults() {
    setSimulationResults({})
    setStatusMessage('')
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

    clearSimulationResults()
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

    clearSimulationResults()
  }

  function getNextComponentName(type) {
    const usedNumbers = components
      .filter(component => component.type === type)
      .map(component => {
        const numberPart = component.name.substring(type.length)
        return Number(numberPart)
      })
      .filter(number => Number.isFinite(number))

    const nextNumber =
      usedNumbers.length === 0
        ? 1
        : Math.max(...usedNumbers) + 1

    return `${type}${nextNumber}`
  }

  function addComponent(type) {
    const newComponent = {
      id: Date.now(),
      type,
      name: getNextComponentName(type),
      value: false,
      x: 100 + components.length * 20,
      y: 100 + components.length * 20,
    }

    setComponents([...components, newComponent])

    clearSimulationResults()
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

    clearSimulationResults()
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
        if (
          component.id === componentId &&
          component.type === 'INPUT'
        ) {
          return {
            ...component,
            value: !component.value,
          }
        }

        return component
      })
    )

    clearSimulationResults()
  }

  function getSimulationValue(component) {
    if (component.type !== 'OUTPUT') {
      return simulationResults[component.name]
    }

    const connection = connections.find(
      connection =>
        connection.destinationId === component.id
    )

    if (!connection) {
      return undefined
    }

    const source = components.find(
      component =>
        component.id === connection.sourceId
    )

    if (!source) {
      return undefined
    }

    return simulationResults[source.name]
  }

  async function simulateCircuit() {
    setStatusMessage('')

    for (const component of components) {
      if (component.type === 'INPUT') {
        continue
      }

      const requiredInputs =
        component.type === 'NOT' ||
        component.type === 'OUTPUT'
          ? 1
          : 2

      for (
        let inputIndex = 0;
        inputIndex < requiredInputs;
        inputIndex++
      ) {
        const connected = connections.some(
          connection =>
            connection.destinationId === component.id &&
            connection.inputIndex === inputIndex
        )

        if (!connected) {
          setStatusMessage(
            `${component.name} input ${inputIndex + 1} is not connected`
          )
          return
        }
      }
    }

    const inputRequests = components
      .filter(component => component.type === 'INPUT')
      .map(component => ({
        name: component.name,
        value: component.value,
      }))

    const gateRequests = components
      .filter(
        component =>
          component.type !== 'INPUT' &&
          component.type !== 'OUTPUT'
      )
      .map(component => ({
        name: component.name,
        type: component.type,
      }))

    const connectionRequests = connections
      .filter(connection => {
        const destination = components.find(
          component =>
            component.id === connection.destinationId
        )

        return destination?.type !== 'OUTPUT'
      })
      .map(connection => {
        const source = components.find(
          component =>
            component.id === connection.sourceId
        )

        const destination = components.find(
          component =>
            component.id === connection.destinationId
        )

        return {
          source: source.name,
          destination: destination.name,
          inputIndex: connection.inputIndex,
        }
      })

    const outputNames = components
      .filter(
        component =>
          component.type !== 'INPUT' &&
          component.type !== 'OUTPUT'
      )
      .map(component => component.name)

    for (
      const output of components.filter(
        component => component.type === 'OUTPUT'
      )
    ) {
      const connection = connections.find(
        connection =>
          connection.destinationId === output.id
      )

      if (connection) {
        const source = components.find(
          component =>
            component.id === connection.sourceId
        )

        if (
          source &&
          !outputNames.includes(source.name)
        ) {
          outputNames.push(source.name)
        }
      }
    }

    const requestBody = {
      inputs: inputRequests,
      gates: gateRequests,
      connections: connectionRequests,
      outputs: outputNames,
    }

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

      setSimulationResults(result.outputs)
      setStatusMessage(
        'Simulation completed successfully'
      )
    } catch (error) {
      console.error('Simulation error:', error)
      setStatusMessage(error.message)
    }
  }

  async function generateTruthTable() {
    setStatusMessage('')

    for (const component of components) {
      if (component.type === 'INPUT') {
        continue
      }

      const requiredInputs =
        component.type === 'NOT' ||
        component.type === 'OUTPUT'
          ? 1
          : 2

      for (
        let inputIndex = 0;
        inputIndex < requiredInputs;
        inputIndex++
      ) {
        const connected = connections.some(
          connection =>
            connection.destinationId === component.id &&
            connection.inputIndex === inputIndex
        )

        if (!connected) {
          setStatusMessage(
            `${component.name} input ${inputIndex + 1} is not connected`
          )
          return
        }
      }
    }

    const inputRequests = components
      .filter(component => component.type === 'INPUT')
      .map(component => ({
        name: component.name,
        value: component.value,
      }))

    const gateRequests = components
      .filter(
        component =>
          component.type !== 'INPUT' &&
          component.type !== 'OUTPUT'
      )
      .map(component => ({
        name: component.name,
        type: component.type,
      }))

    const connectionRequests = connections
      .filter(connection => {
        const destination = components.find(
          component =>
            component.id === connection.destinationId
        )

        return destination?.type !== 'OUTPUT'
      })
      .map(connection => {
        const source = components.find(
          component =>
            component.id === connection.sourceId
        )

        const destination = components.find(
          component =>
            component.id === connection.destinationId
        )

        return {
          source: source.name,
          destination: destination.name,
          inputIndex: connection.inputIndex,
        }
      })

    const outputMappings = []

    for (
      const output of components.filter(
        component => component.type === 'OUTPUT'
      )
    ) {
      const connection = connections.find(
        connection =>
          connection.destinationId === output.id
      )

      if (connection) {
        const source = components.find(
          component =>
            component.id === connection.sourceId
        )

        if (source) {
          outputMappings.push({
            outputName: output.name,
            sourceName: source.name,
          })
        }
      }
    }

    const outputNames = outputMappings.map(
      mapping => mapping.sourceName
    )

    if (outputNames.length === 0) {
      setStatusMessage(
        'Add at least one OUTPUT component'
      )
      return
    }

    const requestBody = {
      inputs: inputRequests,
      gates: gateRequests,
      connections: connectionRequests,
      outputs: outputNames,
    }

    try {
      const response = await fetch('/api/truth-table', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      })

      if (!response.ok) {
        throw new Error(
          'Failed to generate truth table'
        )
      }

      const result = await response.json()

      const displayRows = result.rows.map(row => {
        const displayRow = {}

        for (const input of result.inputs) {
          displayRow[input] = row[input]
        }

        for (const mapping of outputMappings) {
          displayRow[mapping.outputName] =
            row[mapping.sourceName]
        }

        return displayRow
      })

      setTruthTable({
        inputs: result.inputs,
        outputs: outputMappings.map(
          mapping => mapping.outputName
        ),
        rows: displayRows,
      })
      setStatusMessage(
        'Truth table generated successfully'
      )
    } catch (error) {
      console.error(error)
      setStatusMessage(error.message)
    }
  }

  async function saveCircuit() {
    if (!circuitName.trim()) {
      setStatusMessage('Enter a circuit name first')
      return
    }

    const circuitData = {
      components,
      connections,
    }

    const requestBody = {
      name: circuitName,
      circuitData: JSON.stringify(circuitData),
    }

    const isUpdating = selectedCircuitId !== ''

    const url = isUpdating
      ? `/api/circuits/${selectedCircuitId}`
      : '/api/circuits'

    const method = isUpdating ? 'PUT' : 'POST'

    try {
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      })

      if (!response.ok) {
        throw new Error(
          isUpdating
            ? 'Failed to update circuit'
            : 'Failed to save circuit'
        )
      }

      const savedCircuit = await response.json()

      setSelectedCircuitId(
        savedCircuit.id.toString()
      )

      setStatusMessage(
        isUpdating
          ? `Updated circuit "${savedCircuit.name}"`
          : `Saved circuit "${savedCircuit.name}"`
      )

      fetchSavedCircuits()
    } catch (error) {
      console.error(error)
      setStatusMessage(error.message)
    }
  }

  async function fetchSavedCircuits() {
    try {
      const response = await fetch('/api/circuits')

      if (!response.ok) {
        throw new Error(
          'Failed to load saved circuits'
        )
      }

      const circuits = await response.json()
      setSavedCircuits(circuits)
    } catch (error) {
      console.error(error)
      setStatusMessage(error.message)
    }
  }

  async function loadCircuit() {
    if (!selectedCircuitId) {
      setStatusMessage('Select a circuit first')
      return
    }

    try {
      const response = await fetch(
        `/api/circuits/${selectedCircuitId}`
      )

      if (!response.ok) {
        throw new Error('Failed to load circuit')
      }

      const savedCircuit = await response.json()

      const circuitData = JSON.parse(
        savedCircuit.circuitData
      )

      setComponents(circuitData.components)
      setConnections(circuitData.connections)
      setCircuitName(savedCircuit.name)
      setSelectedOutput(null)
      setSimulationResults({})

      setStatusMessage(
        `Loaded circuit "${savedCircuit.name}"`
      )
    } catch (error) {
      console.error(error)
      setStatusMessage(error.message)
    }
  }

  async function deleteSavedCircuit() {
    if (!selectedCircuitId) {
      setStatusMessage('Select a circuit first')
      return
    }

    try {
      const response = await fetch(
        `/api/circuits/${selectedCircuitId}`,
        {
          method: 'DELETE',
        }
      )

      if (!response.ok) {
        throw new Error(
          'Failed to delete circuit'
        )
      }

      setSelectedCircuitId('')
      setStatusMessage('Saved circuit deleted')

      fetchSavedCircuits()
    } catch (error) {
      console.error(error)
      setStatusMessage(error.message)
    }
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Digital Circuit Simulator</h1>

        <div>
          <input
            type="text"
            placeholder="Circuit name"
            value={circuitName}
            onChange={event =>
              setCircuitName(event.target.value)
            }
          />

          <button
            className="simulate-button"
            onClick={simulateCircuit}
          >
            Simulate
          </button>

          <button onClick={generateTruthTable}>
            Truth Table
          </button>

          <button onClick={saveCircuit}>
            Save
          </button>

          <select
            value={selectedCircuitId}
            onChange={event =>
              setSelectedCircuitId(
                event.target.value
              )
            }
          >
            <option value="">
              Select saved circuit
            </option>

            {savedCircuits.map(circuit => (
              <option
                key={circuit.id}
                value={circuit.id}
              >
                {circuit.name}
              </option>
            ))}
          </select>

          <button onClick={loadCircuit}>
            Load
          </button>

          <button onClick={deleteSavedCircuit}>
            Delete Saved
          </button>

          <button
            className="clear-button"
            onClick={clearCircuit}
          >
            Clear Circuit
          </button>
        </div>
      </header>

      <div className="main-layout">
        <aside className="sidebar">
          <h2>Components</h2>

          <button
            onClick={() => addComponent('INPUT')}
          >
            Input
          </button>

          <button
            onClick={() => addComponent('OUTPUT')}
          >
            Output
          </button>

          <button
            onClick={() => addComponent('AND')}
          >
            AND
          </button>

          <button
            onClick={() => addComponent('OR')}
          >
            OR
          </button>

          <button
            onClick={() => addComponent('NOT')}
          >
            NOT
          </button>

          <button
            onClick={() => addComponent('XOR')}
          >
            XOR
          </button>

          <button
            onClick={() => addComponent('NAND')}
          >
            NAND
          </button>

          <button
            onClick={() => addComponent('NOR')}
          >
            NOR
          </button>
        </aside>

        <main className="workspace">
          {statusMessage && (
            <div className="status-message">
              {statusMessage}
            </div>
          )}

          {components.length === 0 && (
            <p>
              Click a component to add it to the
              circuit
            </p>
          )}

          <svg className="wire-layer">
            {connections.map(
              (connection, index) => {
                const source = components.find(
                  component =>
                    component.id ===
                    connection.sourceId
                )

                const destination =
                  components.find(
                    component =>
                      component.id ===
                      connection.destinationId
                  )

                if (!source || !destination) {
                  return null
                }

                return (
                  <CircuitWire
                    key={index}
                    source={source}
                    destination={destination}
                    inputIndex={
                      connection.inputIndex
                    }
                    onDelete={() =>
                      deleteConnection(index)
                    }
                  />
                )
              }
            )}
          </svg>

          {components.map(component => (
            <CircuitComponent
              key={component.id}
              component={component}
              onMove={moveComponent}
              onOutputClick={handleOutputClick}
              onInputClick={handleInputClick}
              isOutputSelected={
                selectedOutput === component.id
              }
              isInputConnected={
                isInputConnected
              }
              onToggleInput={toggleInput}
              simulationValue={
                getSimulationValue(component)
              }
              onDeleteComponent={
                deleteComponent
              }
            />
          ))}

          {truthTable && (
            <div className="truth-table-panel">
              <h3>Truth Table</h3>

              <table>
                <thead>
                  <tr>
                    {truthTable.inputs.map(input => (
                      <th key={input}>
                        {input}
                      </th>
                    ))}

                    {truthTable.outputs.map(output => (
                      <th key={output}>
                        {output}
                      </th>
                    ))}
                  </tr>
                </thead>

                <tbody>
                  {truthTable.rows.map((row, index) => (
                    <tr key={index}>
                      {truthTable.inputs.map(input => (
                        <td key={input}>
                          {row[input] ? '1' : '0'}
                        </td>
                      ))}

                      {truthTable.outputs.map(output => (
                        <td key={output}>
                          {row[output] ? '1' : '0'}
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div className="connection-debug">
            Connections: {connections.length}
          </div>
        </main>
      </div>
    </div>
  )
}

export default App