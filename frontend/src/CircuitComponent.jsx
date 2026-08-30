function CircuitComponent({ component, onMove }) {

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

  return (
    <div
      className="circuit-component"
      style={{
        left: component.x,
        top: component.y,
      }}
      onMouseDown={handleMouseDown}
    >
      {component.type}
    </div>
  )
}

export default CircuitComponent