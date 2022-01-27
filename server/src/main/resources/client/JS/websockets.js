// World sync message
let worldWidth
let worldHeight
let replace
let removeCounter = false

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


        if (json.world !== undefined) {
            worldWidth = json.world.width
            worldHeight = json.world.height
        }

        ReactDOM.render(
            <Game width={worldWidth * cellSize + 1} height={worldHeight * cellSize + 1} scores={json.scores}/>,
            document.getElementById('root')
        )

        if (json.world !== undefined) {
            // copy the newest copy of the World if the backend sends it
            drawGrid(worldWidth * cellSize + 1, worldHeight * cellSize + 1, cellSize)
            drawWorld(json.world.worldstring, cellSize)
            //render Game World

        }

        if (json.replace !== undefined) {
            // copy the replace Data, then change world
            replace = json.replace
            drawReplace(json.replace, cellSize)
            //world.worldstring = world.worldstring.replaceAt(replace[i].pos.y * (world.width + 1) + replace[i].pos.x, replace[i].mat)
        }

        if (json.snakes !== undefined) {
            drawSnakes(json.snakes, cellSize)
        }

            // draw a countdown before the game starts
        if (json.countdown !== undefined) {
            const canvasMap = document.getElementById("gridCanvas")
            const context = canvasMap.getContext("2d")
            context.clearRect(0, 0, canvasMap.width, canvasMap.height);
            drawGrid(worldWidth * cellSize + 1, worldHeight * cellSize + 1, cellSize)

            context.font = "64px Arial"
            context.fillStyle = "black"
            context.fillText(json.countdown, (worldWidth / 2) * cellSize, (worldHeight / 2) * cellSize)
            removeCounter = true
        }

        if (json.countdown == undefined && removeCounter){
            const canvasMap = document.getElementById("gridCanvas")
            const context = canvasMap.getContext("2d")
            context.clearRect(0, 0, canvasMap.width, canvasMap.height);
            drawGrid(worldWidth * cellSize + 1, worldHeight * cellSize + 1, cellSize)
        }
            // check if the game is over and display the winner
        if (json.gameover !== undefined) {
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


// used to change the worldString with the new data from replace
String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}