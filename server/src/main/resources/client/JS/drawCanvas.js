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
                //s = source, d = destination
                //.drawImage(image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);
                context.drawImage(atlasImage, 10, 30, 10, 10, x+.5, y+.5, cellSize, cellSize); //Wall
                x += cellSize
                break
            case "@":
                context.drawImage(atlasImage, 0, 30, 10, 10, x+.5, y+.5, cellSize, cellSize); //Apple
                x += cellSize
                break
            case "T":
                context.drawImage(atlasImage, 20, 30, 10, 10, x+.5, y+.5, cellSize, cellSize); //AppleTree
                x += cellSize
                break
            case " ":
                const hashValue = hash(x+""+y)
                console.log(hashValue)
                console.log(hashValue % 4)
                switch ((hashValue % 4) +1){
                    case 1: context.drawImage(atlasImage, 40, 20, 10, 10, x+.5, y+.5, cellSize, cellSize); //Ground1
                        break
                    case 2: context.drawImage(atlasImage, 50, 20, 10, 10, x+.5, y+.5, cellSize, cellSize); //Ground2
                        break
                    case 3: context.drawImage(atlasImage, 40, 30, 10, 10, x+.5, y+.5, cellSize, cellSize); //Ground3
                        break
                    case 4: context.drawImage(atlasImage, 50, 30, 10, 10, x+.5, y+.5, cellSize, cellSize); //Ground4
                        break
                    default: context.fillStyle = "#d3d3d3"
                        context.fillRect(x, y, cellSize, cellSize)
                }
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
        const coloredAtlasImage = alterImage(atlasImage,snakes[i].color)
        //draw Body
        for (let j = 1; j < snakes[i].positions.length-1; j++) {

            let x = snakes[i].positions[j].x
            let y = snakes[i].positions[j].y
            let after = snakes[i].positions[j+1]
            let prior = snakes[i].positions[j-1]
            let direction

            if (Math.abs(x-after.x)===1&&Math.abs(x-prior.x)===1){direction = "sideways"} else
            if (Math.abs(y-after.y)===1&&Math.abs(y-prior.y)===1){direction = "straight"} else
            if (y-after.y===1&&x-prior.x===1){direction = "upToLeft"} else
            if (y-after.y===1&&x-prior.x===-1){direction = "upToRight"} else
            if (x-after.x===1&&y-prior.y===-1){direction = "downToLeft"} else
            if (x-after.x===-1&&y-prior.y===1){direction = "upToRight"} else
            if (x-after.x===1&&y-prior.y===1){direction = "upToLeft"} else
            if (y-after.y===-1&&x-prior.x===1){direction = "downToLeft"} else
            if (y-after.y===1&&x-prior.x===-1){direction = "downToLeft"} else
            if (x-after.x===-1&&y-prior.y===-1){direction = "downToRight"} else
            if (y-after.y===-1&&x-prior.x===-1){direction = "downToRight"} else

            {direction = "nd"}
            switch (direction){
                case "sideways":
                    context.drawImage(coloredAtlasImage, 10, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailUp
                    break;
                case "straight":
                    context.drawImage(coloredAtlasImage, 0, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailDown
                    break;
                case "downToLeft":
                    context.drawImage(coloredAtlasImage, 40, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailLeft
                    break;
                case "downToRight":
                    context.drawImage(coloredAtlasImage, 20, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "upToRight":
                    context.drawImage(coloredAtlasImage, 30, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "upToLeft":
                    context.drawImage(coloredAtlasImage, 50, 0, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "nd":
                    context.drawImage(coloredAtlasImage, 0, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //Bubble
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
                    context.drawImage(coloredAtlasImage, 20, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailUp
                    break;
                case "down":
                    context.drawImage(coloredAtlasImage, 10, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailDown
                    break;
                case "left":
                    context.drawImage(coloredAtlasImage, 30, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailLeft
                    break;
                case "right":
                    context.drawImage(coloredAtlasImage, 40, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //TailRight
                    break;
                case "nd":
                    context.drawImage(coloredAtlasImage, 0, 10, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //Bubble
                    break;
            }
        }

        //draw Head
        if (snakes[i].positions.length>0){ //check if snake has a head to prevent errors
            let x = snakes[i].positions[0].x
            let y = snakes[i].positions[0].y
            switch (snakes[i].direction){
                case "up":
                    context.drawImage(coloredAtlasImage, 0, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadUp
                    break;
                case "down":
                    context.drawImage(coloredAtlasImage, 10, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadDown
                    break;
                case "right":
                    context.drawImage(coloredAtlasImage, 30, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadLeft
                    break;
                case "left":
                    context.drawImage(coloredAtlasImage, 20, 20, 10, 10, x * cellSize, y * cellSize, cellSize, cellSize); //HeadRight
                    break;
            }
        }

        // draw the names of the players over their names
        context.font = "bolder 16px Arial"
        context.fillStyle = "Black"
        // the '- (snakes[i].name.length * 8)/2 + cellSize/2' is to center the name over the players head
        context.fillText(snakes[i].name, (snakes[i].positions[0].x * cellSize) - (snakes[i].name.length * 8)/2 + cellSize/2, snakes[i].positions[0].y * cellSize, cellSize * 4)
    }
}

function alterImage(imageObj,color){
    let canvas = document.createElement("canvas",);
    let ctx= canvas.getContext("2d");
    console.log(color)
    console.log(parseInt(color.substr(1,2),16))
    console.log(parseInt(color.substr(3,2),16))
    console.log(parseInt(color.substr(5,2),16))

    ctx.drawImage(imageObj, 0, 0);
    let id= ctx.getImageData(0, 0, canvas.width, canvas.height);

    // Iterate over data.  Data is RGBA matrix so go by +=4 to get to next pixel data.
    for (let i = 0; i < id.data.length; i += 4) {
        // Check if RGB == 0 (black)
        //color = #FF0000
        id.data[i] = (id.data[i]*parseInt(color.substr(1,2),16))/255
        id.data[i+1] = (id.data[i+1]*parseInt(color.substr(3,2),16))/255
        id.data[i+2] = (id.data[i+2]*parseInt(color.substr(5,2),16))/255
    }
    console.log(id)
    // redraw your altered data on the canvas.
    ctx.putImageData(id, 0, 0);
    return canvas
}

//hash from https://gist.github.com/iperelivskiy
function hash(s) {
    /* Simple hash function. */
    var a = 1, c = 0, h, o;
    if (s) {
        a = 0;
        /*jshint plusplus:false bitwise:false*/
        for (h = s.length - 1; h >= 0; h--) {
            o = s.charCodeAt(h);
            a = (a<<6&268435455) + o + (o<<14);
            c = a & 266338304;
            a = c!==0?a^c>>21:a;
        }
    }
    return String(a);
};