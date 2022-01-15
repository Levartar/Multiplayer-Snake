function websockets(name, sessionID){
    ws = new WebSocket("ws://localhost:80/join/" + sessionID + "/name/" + name)
    ws.onopen = function () {
        //render the lobby when a player connected over the websocket
        currentPlayer()
        if(playerNames === undefined){
            playerNames = [name]
        }

        ReactDOM.render(
            <Lobby players={playerNames}/>,
            document.getElementById('root')
        );
        document.getElementById("sessionIDtext").innerText = sessionID
    }
    ws.onmessage = function (evt) {
        let json = JSON.parse(evt.data)
        console.log(json)
        if(json.world !== undefined){
            // copy the newest coppy of the World if the backend sends it
            world = json.world
        }
        if(json.replace !== undefined){
            // copy the replace Data, then change world
            replace = json.replace
            for (let i = 0; i < replace.length; i++) {
                world.worldstring = world.worldstring.replaceAt(replace[i].pos.y * (world.width + 1) + replace[i].pos.x, replace[i].mat)
            }
        }

        //render Game World
        ReactDOM.render(
            <Game  width={world.width * cellSize + 1} height={world.height * cellSize + 1} scores={json.scores}/>,
            document.getElementById('root')
        )
        drawWorld(world.worldstring, cellSize)
        drawSnakes(json.snakes, cellSize)
        drawGrid(world.width * cellSize + 1, world.height * cellSize + 1, cellSize)
    }
    //exit the lobby/game and render the main_Menu
    ws.onclose = function () {
        ReactDOM.render(
            <Main_menu />,
            document.getElementById('root')
        );
        alert("Connection is closed...");
    };

    ws.onerror = function (error){
        console.log("Error: " + error)
    }
}
// World sync message
let world
let replace

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
                context.fillStyle = "red"
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

            context.fillStyle = "green"
            context.fillRect(x * cellSize, y * cellSize, cellSize, cellSize)
        }
    }
}
// used to change the worldString with the new data from replace
String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}