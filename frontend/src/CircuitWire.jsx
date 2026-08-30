function CircuitWire({
  source,
  destination,
  inputIndex,
  onDelete
}) {
  const gateWidth = 100
  const gateHeight = 60

  const startX = source.x + gateWidth
  const startY = source.y + gateHeight / 2

  const endX = destination.x

  let endY

  if (
    destination.type === 'NOT' ||
    destination.type === 'OUTPUT'
  ) {
    endY = destination.y + gateHeight / 2
  } else {
    if (inputIndex === 0) {
      endY = destination.y + gateHeight / 3
    } else {
      endY = destination.y + (gateHeight * 2) / 3
    }
  }

  const middleX = (startX + endX) / 2

  const path = `
    M ${startX} ${startY}
    L ${middleX} ${startY}
    L ${middleX} ${endY}
    L ${endX} ${endY}
  `

  return (
    <path
      d={path}
      className="circuit-wire"
      onClick={onDelete}
      fill="none"
    />
  )
}

export default CircuitWire