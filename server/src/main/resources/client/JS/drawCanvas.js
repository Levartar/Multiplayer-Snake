//draw a grid depending on the width ang height of the gameBoard
function drawGrid(bw, bh, cellSize){
    const canvasMap = document.getElementById("mapCanvas")
    const context = canvasMap.getContext("2d")

    for (let x = 0; x <= bw; x += cellSize) {
        context.moveTo(0.5 + x, 0)
        context.lineTo(0.5 + x, bh)
    }

    for (let x = 0; x <= bh; x += cellSize) {
        context.moveTo(0, 0.5 + x)
        context.lineTo(bw, 0.5 + x)
    }
    context.strokeStyle = "black"

    context.stroke()
}

//draw the gameWorld of the world synchronization massage
function drawWorld(world, cellSize){
    const canvasMap = document.getElementById("mapCanvas")
    const context = canvasMap.getContext("2d")

    let x = 0
    let y = 0

    // draw the World array
    for (let i = 0; i < world.length; i++) {
        switch (world[i]) {
            case "#":
                context.fillStyle = "black"
                context.fillRect(x, y, cellSize, cellSize)
                x += cellSize
                break
            case "@":
                context.fillStyle = "yellow"
                context.fillRect(x, y, cellSize, cellSize)
                x += cellSize
                break
            case " ":
                context.fillStyle = "#d3d3d3"
                context.fillRect(x, y, cellSize, cellSize)
                x += cellSize
                break
            case "\n":
                x = 0
                y += cellSize
                break
            default:
                break
        }
    }
}

// draw snakes from an array with all player snakes
function drawSnakes(snakes, cellSize){
    const canvasMap = document.getElementById("mapCanvas")
    const context = canvasMap.getContext("2d")

    for (let i = 0; i < snakes.length; i++) {
        for (let j = 0; j < snakes[i].positions.length; j++) {
            let x = snakes[i].positions[j].x
            let y = snakes[i].positions[j].y

            context.fillStyle = snakes[i].color
            context.fillRect(x * cellSize, y * cellSize, cellSize, cellSize)
        }

        // draw the names of the players over their names
        context.font = "16px Arial"
        context.fillStyle = "black"
        // the '- (snakes[i].name.length * 8)/2 + cellSize/2' is to center the name over the players head
        context.fillText(snakes[i].name, (snakes[i].positions[0].x * cellSize) - (snakes[i].name.length * 8)/2 + cellSize/2, snakes[i].positions[0].y * cellSize, cellSize * 4)
    }
}