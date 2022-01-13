function websockets(name, sessionID){
    const ws = new WebSocket("ws://localhost:80/join/" + sessionID + "/name/" + name)
    ws.onopen = function () {
        //render the lobby when a player connected over the websocket
        ReactDOM.render(
            <Lobby />,
            document.getElementById('root')
        );
        document.getElementById("sessionIDtext").innerText = sessionID
    }
    ws.onmessage = function (evt) {
        // finding out width + height of the canvas
        let endOfFirstLine = evt.world.findIndex(element => element === "\n")
        const firstLine = evt.world.slice(0,endOfFirstLine)
        let amountOfLines = 1
        for (let i = 0; i < evt.world.length; i++) {
            if(evt.world[i] === "\n"){
                amountOfLines++
            }
        }

        //render Game World
        ReactDOM.render(
            <Game  width={firstLine.length * cellSize + 1} height={amountOfLines * cellSize + 1}/>,
            document.getElementById('root')
        )
        drawGrid(firstLine.length * cellSize + 1, amountOfLines * cellSize + 1, cellSize)
        drawWorld(evt.world.length, cellSize)
        for (let i = 0; i < evt.replace.length; i++) {
            let x = evt.replace.pos.x
            let y = evt.replace.pos.y
            let material = evt.replace.mat

            drawChanges(x, y, material)
        }
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