// World sync message
let world
let replace

// time out after the game is finished
let gameEnding

// size of the tiles of the grid
const cellSize = 20

function websockets(name, sessionID){
    let protocol = isSecure() ? "wss://" : "ws://"
    ws = new WebSocket(protocol + window.location.host + "/join/" + sessionID + "/name/" + name)
    ws.onopen = function () {
        //render the lobby when a player connected over the websocket
        if(playerNames === undefined){
            playerNames = []
        }

        ReactDOM.render(
            <Lobby players={playerNames} maps={maps}/>,
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

        // draw a countdown before the game starts
        if(json.countdown !== undefined){
            const canvasMap = document.getElementById("mapCanvas")
            const context = canvasMap.getContext("2d")

            context.font = "64px Arial"
            context.fillStyle = "black"
            context.fillText(json.countdown, (world.width/2) * cellSize, (world.height/2) * cellSize)
        }
        // check if the game is over and display the winner
        if(json.gameover !== undefined){
            alert("The Winner is: " + json.gameover.winner)
            // disconect the players 6 sec after the game has ended
            gameEnding = setTimeout(() => {
                console.log("game ended")
                ReactDOM.render(
                    <Lobby players={playerNames} maps={maps}/>,
                    document.getElementById("root")
                )
            }, 1500)
        }
    }
    //exit the lobby/game and render the main_Menu
    ws.onclose = function () {
        renderMain()
    };

    ws.onerror = function (error){
        console.log("Error: " + error)
    }
}

function isSecure()
{
    return window.location.protocol === 'https:';
}

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
        context.fillText(snakes[i].name, snakes[i].positions[0].x * cellSize, snakes[i].positions[0].y * cellSize)
    }
}
// used to change the worldString with the new data from replace
String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}