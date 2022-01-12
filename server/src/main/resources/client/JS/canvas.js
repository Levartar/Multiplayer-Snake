const cellSize = 40

//draw the replacements of the world synchronization massage
function drawChanges(x, y, material){
    const canvasMap = document.getElementById("mapCanvas")
    const context = canvasMap.getContext("2d")

    switch (material) {
        case "#":
            context.fillStyle = "red"
            context.fillRect(x, y, 40, 40)
            break
        case "@":
            context.fillStyle = "green"
            context.fillRect(x, y, 40, 40)
            break
        case " ":
            context.fillStyle = "blue"
            context.fillRect(x, y, 40, 40)
            break
        case "\n":
            x = 0
            y += 40
            break
    }
}

//draw a grid for the depending on the width ang height of the gameBoard
function drawGrid(bw, bh, cellSize){
    const canvasMap = document.getElementById("mapCanvas")
    const context = canvasMap.getContext("2d")

    for (let x = 0; x <= bw; x += cellSize) {
        context.moveTo(0.5 + x, 0);
        context.lineTo(0.5 + x, bh);
    }

    for (let x = 0; x <= bh; x += cellSize) {
        context.moveTo(0, 0.5 + x);
        context.lineTo(bw, 0.5 + x);
    }
    context.strokeStyle = "black";

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
                context.fillStyle = "red"
                context.fillRect(x, y, cellSize, cellSize)
                x += cellSize
                break
            case "@":
                context.fillStyle = "green"
                context.fillRect(x, y, cellSize, cellSize)
                x += cellSize
                break
            case " ":
                context.fillStyle = "blue"
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