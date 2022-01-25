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
    const atlasImage = new Image();   // Create new img element
    atlasImage.src = "./assets/snakeAtlas.png"; // Set source path

    let x = 0
    let y = 0

    // draw the World array
    for (let i = 0; i < world.length; i++) {
        switch (world[i]) {
            case "#":
                //context.fillStyle = "black"
                //context.fillRect(x, y, cellSize, cellSize)
                //s = source, d = destination
                //.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
                context.drawImage(atlasImage, 10, 30, 10, 10, x, y, cellSize, cellSize); //Wall
                x += cellSize
                break
            case "@":
                //context.fillStyle = "red"
                //context.fillRect(x, y, cellSize, cellSize)
                context.drawImage(atlasImage, 0, 30, 10, 10, x, y, cellSize, cellSize); //Apple
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
    const atlasImage = new Image();   // Create new img element
    atlasImage.src = "./assets/snakeAtlas.png"; // Set source path

    for (let i = 0; i < snakes.length; i++) {
        //draw Body
        for (let j = 1; j < snakes[i].positions.length-1; j++) {

            let x = snakes[i].positions[j].x
            let y = snakes[i].positions[j].y
            let after = snakes[i].positions[j+1]
            let prior = snakes[i].positions[j-1]
            let direction

            if (Math.abs(x-after.x)===1&&Math.abs(x-prior.x)===1){direction = "sideways"} else
            if (Math.abs(y-after.y)===1&&Math.abs(y-prior.y)===1){direction = "straight"} else
            if (x-after.x===-1&&y-prior.y===-1){direction = "downToLeft"} else
            if (x-after.x===1&&y-prior.y===-1){direction = "downToRight"} else
            if (x-after.x===-1&&y-prior.y===1){direction = "upToLeft"} else
            if (x-after.x===1&&y-prior.y===1){direction = "upToRight"} else
            {direction = "nd"}
            switch (direction){
                case "sideways":
                    context.drawImage(atlasImage, 10, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailUp
                    break;
                case "straight":
                    context.drawImage(atlasImage, 0, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailDown
                    break;
                case "downToRight":
                    context.drawImage(atlasImage, 40, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailLeft
                    break;
                case "downToLeft":
                    context.drawImage(atlasImage, 20, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "upToRight":
                    context.drawImage(atlasImage, 50, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "upToLeft":
                    context.drawImage(atlasImage, 40, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "nd":
                    context.drawImage(atlasImage, 0, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //Bubble
                    break;
            }
            //context.fillStyle = snakes[i].color
            //context.fillRect(x * cellSize, y * cellSize, cellSize, cellSize)
        }

        //draw Tail
        if (typeof snakes[i].positions[snakes[i].positions.length-1] != "undefined"){ //Tail exists
            let tail = snakes[i].positions[snakes[i].positions.length-1]
            let x = tail.x
            let y = tail.y
            let direction
            //get prior direction
            let prior = snakes[i].positions[snakes[i].positions.length-2]
            if (prior.x - tail.x == -1){direction = "right"} else
            if (prior.x - tail.x == 1){direction = "left"} else
            if (prior.y - tail.y == -1){direction = "up"} else
            if (prior.y - tail.y == 1){direction = "down"} else
            {direction = "nd"}
            switch (direction){
                case "up":
                    context.drawImage(atlasImage, 20, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailUp
                    break;
                case "down":
                    context.drawImage(atlasImage, 10, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailDown
                    break;
                case "left":
                    context.drawImage(atlasImage, 30, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailLeft
                    break;
                case "right":
                    context.drawImage(atlasImage, 40, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "nd":
                    context.drawImage(atlasImage, 0, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //Bubble
                    break;
            }
        }

        //draw Head
        if (snakes[i].positions.length>0){ //check if snake has a head to prevent errors
            let x = snakes[i].positions[0].x
            let y = snakes[i].positions[0].y
            switch (snakes[i].direction){
                case "up":
                    context.drawImage(atlasImage, 0, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadUp
                    break;
                case "down":
                    context.drawImage(atlasImage, 10, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadDown
                    break;
                case "right":
                    context.drawImage(atlasImage, 30, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadLeft
                    break;
                case "left":
                    context.drawImage(atlasImage, 20, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadRight
                    break;
            }
        }

        // draw the names of the players over their names
        context.font = "16px Arial"
        context.fillStyle = "black"
        // the '- (snakes[i].name.length * 8)/2 + cellSize/2' is to center the name over the players head
        context.fillText(snakes[i].name, (snakes[i].positions[0].x * cellSize) - (snakes[i].name.length * 8)/2 + cellSize/2, snakes[i].positions[0].y * cellSize, cellSize * 4)
    }
}