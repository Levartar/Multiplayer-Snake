// sessionID of the last lobby the player tried to join
let sessionID
// Array with player names
let playerNames
// mappool
let currentMap
let maps = []
// websocket
let ws
// interval for refreshing the player list (is a function)
let checkCurrentPlayers
// size of the tiles of the grid
const cellSize = 20

function Button(props){
    return(
        <div id={props.name + "ButtonDiv"}>
            <button id={"button" + props.name}>{props.text}</button>
        </div>
    )
}

//define input with corresponding label
function Input(props){
    return(
        <div id={props.name.toLowerCase()}>
            <div id={props.name + "LabelDiv"} className={props.className}>
                <label htmlFor={"input" + props.name} className={props.className}>{props.text}</label>
            </div>
            <div id={props.name + "InputDiv"} className={props.className}>
                <input id={"input" + props.name} className={props.className}
                       type={props.type} name={props.name} maxLength={props.maxLength}/>
            </div>
        </div>
    )
}

//define the "createSession" with label, button and event listeners
class Create_session extends React.Component{
    componentDidMount() {
        document.getElementById("buttonnewGame").addEventListener("click", () => {
            //create a new lobby
            if(!nameCheck()){
                const url = "/create"
                const request = new Request(url, {
                        method: 'GET'
                    }
                )

                fetch(request)
                    .then(response => {
                        response.text().then(value => {
                            //connect the player to the lobby
                            sessionID = value
                            // values of the input fields
                            const name = document.getElementById("inputName").value

                            getGameInfo(sessionID)
                            websockets(name, sessionID)
                        })
                    })
                    .catch(err => console.log(err.message))
            }else {
                alert("Enter a name!!!")
            }

        })
    }

    render() {
        return(
            <div>
                <div id="newGameLabelDiv">
                    <label htmlFor="buttonNewGame">Create a game</label>
                </div>
                <Button text={"play"} name={"newGame"}/>
            </div>
        )
    }
}

//define the "joinSession" with inputField and button
class Join_session extends React.Component{
    componentDidMount() {
        document.getElementById("buttonsessionId").addEventListener("click", () => {
            if(!nameCheck()){
                //connect to lobby with sessionID
                sessionID = document.getElementById("inputsessionId").value

                const url = "/game-info?code=" + sessionID
                const request = new Request(url, {
                        method: 'GET'
                    }
                )

                fetch(request)
                    .then(response => {
                        response.json().then(obj => {
                            //look if the lobby with the sessionID is running if yes open ws
                            let isValideId = obj.exists
                            playerNames = obj.playerNames
                            currentMap = obj.selectedMap
                            maps = obj.mapNames
                            let isFull = playerNames.length > 3;
                            let hasStarted = obj.hasStarted

                            //check if player can connect to lobby
                            if(isValideId){
                                if(!isFull){
                                    if(!hasStarted){
                                        const name = document.getElementById("inputName").value
                                        websockets(name, sessionID)
                                    }else {
                                        alert("the game has already Started. please Wait until it has finished or join another lobby")
                                    }
                                }else {
                                    alert("The lobby with SessionID " + sessionID + " already has no free slots for new players to join")
                                }
                            }else{
                                alert("No lobby with SessionID " + sessionID + " is currently open")
                            }
                        })
                    })
                    .catch(err => console.log(err.message))
            }else {
                alert("Enter a name!!!")
            }
        })
    }

    render() {
        return(
            <div>
                <Input name={"sessionId"} class={"flexed"} placeholder={"sessionID"} type={"text"} text={"Join a Session"} maxLength={"10"}/>
                <Button text={"join"} name={"sessionId"}/>
            </div>
        )
    }
}

//define the Main menu with a banner, nameInput, createSession ,join session and HighscoreTable
class Main_menu extends React.Component {
    componentDidMount() {
        clearInterval(checkCurrentPlayers)
    }
    render(){
        return (
            <div className="frame" id="MainMenuDiv">
                <img id="menuBanner" src={"./assets/img.png"} alt="Snake.IO banner image" />
                <main>
                    <h1>Snake IO</h1>
                    <div id="marginEnterName">
                        <Input name={"Name"} type={"text"} placeholder={"name"} text={"Enter your name: "} maxLength={"10"}/>
                    </div>
                    <div className="marginBottom">
                        <Create_session />
                    </div>
                    <div>
                        <Join_session  />
                    </div>
                </main>
                <PlayerScore id={"tableMainMenu"} data={this.props.scores} tableHead1={"Player"} tableHead2={"Score"}/>
            </div>
        )
    }
}

//define how the HighscoreTable looks like
function PlayerScore(props){
    return(
        <div>
            <table id={props.id}>
                <thead>
                <tr>
                    <th >{props.tableHead1}</th>
                    <th >{props.tableHead2}</th>
                </tr>
                </thead>
                <tbody>
                {/* for every player in the data-Array add a row to the table with player name and the score */}
                {props.data.map((player, i) =>
                    <tr key={i}>
                        <td>{props.data[i].name}</td>
                        <td>{props.data[i].points}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}

//define a list of player that are currently in the lobby
function List(props){
    return(
        <div>
            <h2>Player:</h2>
            <ul>
                {props.players.map((player, i) =>
                    //console.log(player + " and " + i)
                    <li id={"player" + i}>{player}</li>
                )}
            </ul>
        </div>
    )
}

//drop down menu that dynamically matches the length of entries sent from backend
function MapSelect(props) {
    return(
        <div id="mapSelector">
            <h2 id="mapSelectHeadline">Select Map</h2>
            <select name="maps" id="mapDropDown">
                {props.data.map((mapOptions, i) =>
                    <option key={i}>{props.data[i]}</option>
                )}
            </select>
        </div>
    )

}

//placeholder data as long as there is no data from backend


//define the lobby with eventListener, buttons and mapOptions
class Lobby extends React.Component {
    componentDidMount() {
        document.getElementById("buttonexitLobby").addEventListener("click", () => {
            ws.close()
            renderMain()
        })

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

//define the Game with eventListener, the gameBoard and a player List with the current score
class Game extends React.Component {
    componentDidMount() {
        document.getElementById("buttonexitGame").addEventListener("click", () => {
            ws.close()
            renderMain()
        })

        // clear the interval refreshing the player list
        clearInterval(checkCurrentPlayers)

        //send the input of the Player to the backend
        document.addEventListener("keydown", (event) => {
            switch (event.key){
                case 'w':
                case 'a':
                case 's':
                case 'd':
                    ws.send(event.key)
                    break
                case 'ArrowUp':
                    ws.send('w')
                    break
                case 'ArrowLeft':
                    ws.send('a')
                    break
                case 'ArrowDown':
                    ws.send('s')
                    break
                case 'ArrowRight':
                    ws.send('d')
                    break
            }
        })
    }

    render() {
        return(
            <div className="frame" id="frameGame">
                <div className="flexed" id="gameScreen">
                    <Button name={"exitGame"} text={"back"}/>
                    <div id="gameMap">
                        <canvas id="mapCanvas" width={this.props.width} height={this.props.height}>
                        </canvas>
                    </div>
                    <div id="gameScores">
                        <PlayerScore data={this.props.scores} tableHead1={"Player"} tableHead2={"Score"}/>
                    </div>
                </div>
            </div>
        )
    }
}

// render the Main_menu when the site is opened
// the request asks for the highscores to render them
renderMain()


// extra Functions that have nothing to do with react
function nameCheck(){
    return document.getElementById("inputName").value === ""
}

function getGameInfo(sessionID){
    const url = "/game-info?code=" + sessionID
    const request = new Request(url, {
            method: 'GET'
        }
    )

    fetch(request)
        .then(response => {
            response.json().then(obj => {
                currentMap = obj.selectedMap
                playerNames = obj.playerNames
                maps = obj.mapNames

                if(playerNames !== undefined && playerNames.length > 0 && maps !== undefined){
                    ReactDOM.render(
                        <Lobby players={playerNames} maps={maps}/>,
                        document.getElementById("root")
                    )
                }
                setDefault(currentMap)
            })
        })
}
function setDefault(currentMap){
    let mySelect = document.getElementById('mapDropDown');
    let options = mySelect.children

    for (let i = 0; i < options.length; i++) {

        if(options[i].value === currentMap){
            options[i].selected = true
            break
        }
    }
}

function renderMain(){
    let request = new Request("/highscores", {
        method: 'GET'
    })

    fetch(request)
        .then(response => response.json().then(obj => {
            /*
                to use the same code as fore the score table during the game
                i change the name score to points, because otherwise i would have tho change to
                much
            */
            let scores = []
            for (let i = 0; i < obj.length; i++) {
                scores[i] = {
                                name: obj[i].name,
                                points: obj[i].score
                            }
            }
            ReactDOM.render(
                <Main_menu scores={scores}/>,
                document.getElementById('root')
            );
        }))
}