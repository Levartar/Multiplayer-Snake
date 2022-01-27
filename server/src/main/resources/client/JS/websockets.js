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
            const canvasMap = document.getElementById("mapCanvas")
            const context = canvasMap.getContext("2d")

            context.font = "64px Arial"
            context.fillStyle = "black"
            let winner = json.gameover.winner
            context.fillText("The Winner is: " + winner, (world.width/(8+winner.length/10)) * cellSize, (world.height/2) * cellSize)
            // disconect the players 6 sec after the game has ended
            gameEnding = setTimeout(() => {
                console.log("game ended")
                ReactDOM.render(
                    <Lobby players={playerNames} maps={maps}/>,
                    document.getElementById("root")
                )
            }, 6000)
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