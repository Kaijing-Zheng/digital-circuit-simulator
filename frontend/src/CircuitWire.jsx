function CircuitWire({
  source,
  destination,
  inputIndex
}) {
  const gateWidth = 100
  const gateHeight = 60

  // Output port is centered on the right side
  const startX = source.x + gateWidth
  const startY = source.y + gateHeight / 2

  // Input port is on the left side
  const endX = destination.x

  let endY

  if (destination.type === 'NOT') {
    endY = destination.y + gateHeight / 2
  } else {
    if (inputIndex === 0) {
      endY = destination.y + gateHeight / 3
    } else {
      endY = destination.y + (gateHeight * 2) / 3
    }
  }

  return (
    <line
      x1={startX}
      y1={startY}
      x2={endX}
      y2={endY}
      className="circuit-wire"
    />
  )
}

export default CircuitWire