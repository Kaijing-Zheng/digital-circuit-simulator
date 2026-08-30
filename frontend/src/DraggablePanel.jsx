import { useRef, useState } from 'react'

function DraggablePanel({
  title,
  children,
  onClose,
  className = ''
}) {
  const panelRef = useRef(null)

  const [position, setPosition] = useState({
    x: 0,
    y: 0
  })

  const [hasMoved, setHasMoved] = useState(false)

  function handleMouseDown(event) {
    if (event.button !== 0) {
      return
    }

    const panel = panelRef.current

    if (!panel) {
      return
    }

    const panelRect = panel.getBoundingClientRect()

    const startMouseX = event.clientX
    const startMouseY = event.clientY

    const startX = panelRect.left
    const startY = panelRect.top

    function handleMouseMove(event) {
      let newX =
        startX + (event.clientX - startMouseX)

      let newY =
        startY + (event.clientY - startMouseY)

      const panelWidth = panel.offsetWidth
      const panelHeight = panel.offsetHeight

      const maxX =
        window.innerWidth - panelWidth

      const maxY =
        window.innerHeight - panelHeight

      newX = Math.max(
        0,
        Math.min(newX, maxX)
      )

      newY = Math.max(
        0,
        Math.min(newY, maxY)
      )

      setPosition({
        x: newX,
        y: newY
      })

      setHasMoved(true)
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

  const style = hasMoved
    ? {
        left: position.x,
        top: position.y,
        right: 'auto',
        bottom: 'auto'
      }
    : {}

  return (
    <div
      ref={panelRef}
      className={`draggable-panel ${className}`}
      style={style}
    >
      <div
        className="draggable-panel-header"
        onMouseDown={handleMouseDown}
      >
        <h2>{title}</h2>

        <button
          className="panel-close-button"
          onMouseDown={event =>
            event.stopPropagation()
          }
          onClick={onClose}
          aria-label={`Close ${title}`}
        >
          ×
        </button>
      </div>

      <div className="draggable-panel-content">
        {children}
      </div>
    </div>
  )
}

export default DraggablePanel