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
            const url = "http://localhost:80/create"
            const request = new Request(url, {
                    method: 'GET'
                }
            )

            fetch(request)
                .then(response => {
                    //connect the player to the lobby
                    //sessionID = response.toString()
                    console.log(response)
                    //websockets("wss://localhost:80/join/" + sessionID + "/name/" + name)
                })
                .catch(err => console.log(err.message))
        })
    }

    render() {
        return(
            <div>
                <div id="newGameLabelDiv">
                    <label htmlFor="buttonNewGame">Create a game</label>
                </div>
                <div className="flexed">
                    <br/>
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
            //connect to lobby with sessionID
            const url = "http://localhost:80/game-info?code=" + sessionID
            const request = new Request(url, {
                    method: 'GET'
                }
            )

            //boolean if lobby with this sessionID is open
            let isValideId = false
            //boolean if there is space for the player to join
            let isFull = true
            //boolean if the game has already started
            let hasStarted

            fetch(request)
                .then(response => {
                    response.json()
                })
                .then(obj => {
                    //look if the lobby with the sessionID is running if yes open ws
                    isValideId = obj.exists
                    isFull = obj.isFull
                    hasStarted = obj.hasStared
                })
                .catch(err => console.log(err.message))

            //check if player can connect to lobby
            if(isValideId){
                if(!isFull){
                    if(!hasStarted){
                        websockets("wss://localhost:80/join/" + sessionID + "/name/" + name)
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
    }

    render() {
        return(
            <div>
                <Input name={"sessionId"} class={"flexed"} type={"text"} text={"Join a Session"} maxLength={"10"}/>
                <Button text={"join"} name={"sessionId"}/>
            </div>
        )
    }
}

//define the Main menu with a banner, nameInput, createSession ,join session and HighscoreTable
class Main_menu extends React.Component {
    componentDidMount() {
        //const url = "http://localhost:80/game-info"
        //const request = new Request(url, {
        //        method: 'GET'
        //    }
        //)

        //fetch(request)
        //    .then(response => {
        //        response.json()
        //    })
        //    .then(response => {
        //        let playerScores = response
        //    })
    }
    render(){
        return (
            <div className="frame">
                <header>
                    <img id="menuBanner" src="./assets/img.png" alt="Snake.IO banner image" />
                </header>
                <main>
                    <div>
                        <Input name={"Name"} type={"text"} text={"Enter your name: "} maxLength={"10"}/>
                    </div>
                    <div id="enterLobby">
                        <Create_session />
                        <Join_session />
                    </div>
                </main>
            </div>
        )
    }
}

//placeholder data before we can communicate with the Database
let player = [
    {name: "Tim", score: "420"},
    {name: "Lukas", score: "1337"},
    {name: "Felix", score: "69"},
    {name: "Timo", score: "7"}
]

//define how the HighscoreTable looks like
function PlayerScore(props){
    return(
        <div>
            <table>
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
                        <td>{props.data[i].score}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}

//define a list of player that are currently in the lobby
function List(){
    return(
        <div>
            <h2>Player:</h2>
            <ul>
                {player.map((player, i) =>
                    <li id={"player" + i}>{player.name}</li>
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
                    <option key={i}>{props.data[i].mapOption}</option>
                )}
            </select>
        </div>
    )

}

//placeholder data as long as there is no data from backend
const testMaps = [
    {mapOption: "coolMap"},
    {mapOption: "awesomeMap"},
    {mapOption: "42"}
]

//define the lobby with eventListener, buttons and mapOptions
class Lobby extends React.Component {
    componentDidMount() {
        document.getElementById("buttonexitLobby").addEventListener("click", () => {
            //ws.close()
            ReactDOM.render(
                <Main_menu />,
                document.getElementById('root')
            );
        })

        document.getElementById("buttonstartGame").addEventListener("click", () => {
            const url = "http://localhost:3000/game-info?" + sessionID
            const request = new Request(url, {
                    method:'POST',
                    headers:{
                        'Content-Type':'application/x-www-form-urlencoded'
                    },
                    body:"lang=en"
                }
            )

            fetch(request)
                .then(response => {
                    // start the game
                })

            ReactDOM.render(
                <Game />,
                document.getElementById('root')
            );
        })
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
                <div id="lobby" className="noMargin">
                    <div id="sessionIDDIV">
                        <h2 id="sessionIDHeadline" className="noMargin">SessionID:</h2>
                        <p id="sessionIDtext" className="noMargin">X15QW4B</p>
                    </div>
                    <MapSelect data={testMaps}/>
                    <div id="playerTable">
                        <List />
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
            //ws.close()
            ReactDOM.render(
                <Lobby />,
                document.getElementById('root')
            );
        })

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
            <div className="frame">
                <Button name={"exitGame"} text={"back"}/>
                <div className="flexed" id="gameScreen">
                    <div id="gameMap">
                        <canvas id="mapCanvas" width={this.props.width} height={this.props.height}>
                        </canvas>
                    </div>
                    <div id="gameScores">
                        <PlayerScore data={player} tableHead1={"Player"} tableHead2={"Score"}/>
                    </div>
                </div>
            </div>
        )
    }
}

//render the Main_menu when the site is opened
ReactDOM.render(
    <Lobby />,
    document.getElementById('root')
);

// values of the input fields
let sessionID = document.getElementById("inputSessionId").value
const name = document.getElementById("inputName").value
