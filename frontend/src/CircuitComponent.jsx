function CircuitComponent({
  component,
  onMove,
  onOutputClick,
  onInputClick
}) {
  function handleMouseDown(event) {
    const startMouseX = event.clientX
    const startMouseY = event.clientY

    const startComponentX = component.x
    const startComponentY = component.y

    function handleMouseMove(event) {
      const changeX = event.clientX - startMouseX
      const changeY = event.clientY - startMouseY

      onMove(
        component.id,
        startComponentX + changeX,
        startComponentY + changeY
      )
    }

    function handleMouseUp() {
      window.removeEventListener('mousemove', handleMouseMove)
      window.removeEventListener('mouseup', handleMouseUp)
    }

    window.addEventListener('mousemove', handleMouseMove)
    window.addEventListener('mouseup', handleMouseUp)
  }

  const inputCount =
    component.type === 'NOT' ? 1 :
    component.type === 'INPUT' ? 0 :
    2

  const hasOutput = true

  return (
    <div
      className="circuit-component"
      style={{
        left: component.x,
        top: component.y,
      }}
      onMouseDown={handleMouseDown}
    >
      <div className="gate-label">
        {component.type}
      </div>

      <div className="input-ports">
        {Array.from({ length: inputCount }).map((_, index) => (
            <div
                key={index}
                className="port input-port"
                onMouseDown={(event) => event.stopPropagation()}
                onClick={() => onInputClick(component.id, index)}
            />
        ))}
        </div>

        {hasOutput && (
        <div
            className="port output-port"
            onMouseDown={(event) => event.stopPropagation()}
            onClick={() => onOutputClick(component.id)}
        />
        )}
    </div>
  )
}

export default CircuitComponent