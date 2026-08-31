import { useEffect, useState } from 'react'

function CircuitComponent({
  component,
  onMove,
  onOutputClick,
  onInputClick,
  isOutputSelected,
  isInputConnected,
  onToggleInput,
  simulationValue,
  onRenameComponent,
  onDuplicateComponent,
  onDeleteComponent,
}) {
  const [contextMenu, setContextMenu] =
    useState(null)

  useEffect(() => {
    if (!contextMenu) {
      return
    }

    function handleClickAway() {
      setContextMenu(null)
    }

    window.addEventListener(
      'mousedown',
      handleClickAway
    )

    return () => {
      window.removeEventListener(
        'mousedown',
        handleClickAway
      )
    }
  }, [contextMenu])

  function handleMouseDown(event) {
    if (event.button !== 0) {
      return
    }

    const startMouseX = event.clientX
    const startMouseY = event.clientY

    const startComponentX = component.x
    const startComponentY = component.y

    function handleMouseMove(event) {
      const changeX =
        event.clientX - startMouseX

      const changeY =
        event.clientY - startMouseY

      onMove(
        component.id,
        startComponentX + changeX,
        startComponentY + changeY
      )
    }

    function handleMouseUp() {
      window.removeEventListener(
        'mousemove',
        handleMouseMove
      )

      window.removeEventListener(
        'mouseup',
        handleMouseUp
      )
    }

    window.addEventListener(
      'mousemove',
      handleMouseMove
    )

    window.addEventListener(
      'mouseup',
      handleMouseUp
    )
  }

  function handleContextMenu(event) {
    event.preventDefault()
    event.stopPropagation()

    setContextMenu({
      x: event.clientX,
      y: event.clientY,
    })
  }

  function handleRename() {
    const newName = window.prompt(
      'Rename component:',
      component.name
    )

    if (newName === null) {
      setContextMenu(null)
      return
    }

    onRenameComponent(
      component.id,
      newName
    )

    setContextMenu(null)
  }

  function handleDuplicate() {
    onDuplicateComponent(component.id)
    setContextMenu(null)
  }

  function handleDelete() {
    onDeleteComponent(component.id)
    setContextMenu(null)
  }

  const inputCount =
    component.type === 'INPUT'
      ? 0
      : component.type === 'NOT' ||
        component.type === 'OUTPUT'
        ? 1
        : 2

  return (
    <>
      <div
        className="circuit-component"
        style={{
          left: component.x,
          top: component.y,
        }}
        onMouseDown={handleMouseDown}
        onContextMenu={handleContextMenu}
      >
        <div
          className="gate-label"
          onMouseDown={(event) =>
            event.stopPropagation()
          }
          onContextMenu={
            handleContextMenu
          }
          onDoubleClick={() => {
            if (
              component.type === 'INPUT'
            ) {
              onToggleInput(component.id)
            }
          }}
        >
          <div>
            {component.name}
          </div>

          <div className="logic-value">
            {component.type === 'INPUT'
              ? component.value
                ? '1'
                : '0'
              : simulationValue ===
                  undefined
                ? '?'
                : simulationValue
                  ? '1'
                  : '0'}
          </div>
        </div>

        {component.type === 'DFF' && (
          <>
            <span className="dff-label dff-label-d">
              D
            </span>

            <span className="dff-label dff-label-clk">
              CLK
            </span>

            <span className="dff-label dff-label-q">
              Q
            </span>
          </>
        )}

        <div className="input-ports">
          {Array.from({
            length: inputCount,
          }).map((_, index) => (
            <div
              key={index}
              className={
                isInputConnected(
                  component.id,
                  index
                )
                  ? 'port input-port connected-port'
                  : 'port input-port'
              }
              onMouseDown={(event) =>
                event.stopPropagation()
              }
              onClick={() =>
                onInputClick(
                  component.id,
                  index
                )
              }
            />
          ))}
        </div>

        {component.type !== 'OUTPUT' && (
          <div
            className={
              isOutputSelected
                ? 'port output-port selected-port'
                : 'port output-port'
            }
            onMouseDown={(event) =>
              event.stopPropagation()
            }
            onClick={() =>
              onOutputClick(component.id)
            }
          />
        )}
      </div>

      {contextMenu && (
        <div
          className="component-context-menu"
          style={{
            left: contextMenu.x,
            top: contextMenu.y,
          }}
          onMouseDown={(event) =>
            event.stopPropagation()
          }
        >
          <button
            type="button"
            onClick={handleRename}
          >
            Rename
          </button>

          <button
            type="button"
            onClick={handleDuplicate}
          >
            Duplicate
          </button>

          <div className="context-menu-divider" />

          <button
            type="button"
            className="context-menu-delete"
            onClick={handleDelete}
          >
            Delete
          </button>
        </div>
      )}
    </>
  )
}

export default CircuitComponent