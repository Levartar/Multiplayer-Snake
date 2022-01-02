let ws;
let username = document.getElementById("inputName").value // username from input field

if ("Websocket" in window){
    ws = new WebSocket("wss://url" + username)  // servername
    ws.onopen = function () {
        // perform Action when connection is opened
        // probably entering the lobby
    }

    ws.onmessage = function (evt) {
        // receiving the new gameWorld, and rendering it
    }

    ws.onclose = function () {
        // websocket is closed.
        alert("Connection is closed...");
        // returning back to the starting screen
    };

    ws.onerror = function (error){
        console.log("Error: " + error)
    }
}else {
    // The browser doesn't support WebSocket
    alert("WebSocket NOT supported by your Browser!");
}

window.onload = function(){
    // not needed before in game
    /*document.addEventListener("keydown", event => {
         switch (event.key){
             case 'w':
             case 'a':
             case 's':
             case 'd':
                 ws.send(event.key)
         }
    })*/


    document.getElementById("buttonNewGame").onclick = (event => {
        // create lobby, then render lobby
        let url = "www.xyz.de";

        const request = new Request(url,
            {
                method: 'GET'
            })

        fetch(request)
            .then(response => {
                // response should be the lobby code, so we can connect the player to the lobby
        })

        console.log("lobby created")
    })

    document.getElementById("buttonJoinSession").addEventListener("click", event => {
        // join existing lobby, then render lobby
        let sessionID = document.getElementById("inputSessionId").value;
        let url = "www.xyz.de?code=" + sessionID;

        const request = new Request(url,
            {
                method: 'GET'
            })

        fetch(request)
            .then(response => {
                // player is connected to yhe lobby now, -> render lobby
            })

        console.log("joined lobby, lobby-ID: " + sessionID)
    })
};
