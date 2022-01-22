//define the lobby with eventListener, buttons and mapOptions
class Lobby extends React.Component {
    componentDidMount() {
        document.removeEventListener("keydown", sendInput)

        document.getElementById("buttonexitLobby").addEventListener("click", () => {
            ws.close()
            renderMain()
        })

        // render the sessionID in the lobby so other player can join the lobby
        document.getElementById("sessionIDtext").innerText = sessionID

        document.getElementById("buttonstartGame").addEventListener("click", () => {
            const url = "/start?code=" + sessionID
            const request = new Request(url, {
                    method:'POST'
                }
            )

            // start the game
            fetch(request).then()
        })
        document.getElementById("mapDropDown").addEventListener("change", () => {
            const url = "/select-map?code=" + sessionID
            const request = new Request(url, {
                    method:'POST',
                    body: document.getElementById("mapDropDown").value
                }
            )
            // send the selected map to the backend
            fetch(request).then()
        })

        checkCurrentPlayers = setInterval(() => {
            getGameInfo(sessionID)

        }, 1000);
    }

    render() {
        return(
            <div className="frame">
                <div>
                    <Button name={"exitLobby"} text={"back"}/>
                </div>
                <div id="lobbyHeadlineDIV">
                    <h1 id="lobbyHeadline">Lobby</h1>
                </div>
                <div id="lobby">
                    <div id="sessionIDDIV">
                        <h2 id="sessionIDHeadline" >SessionID:</h2>
                        <p id="sessionIDtext" > </p>
                    </div>
                    <MapSelect data={this.props.maps}/>
                    <div id="playerTable">
                        <List players={this.props.players}/>
                    </div>
                </div>
                <div id="startGameDIV" className="flexed">
                    <Button name={"startGame"} text={"Start"}/>
                </div>
            </div>
        )
    }
}