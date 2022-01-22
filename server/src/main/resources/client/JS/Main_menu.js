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

//define the Main menu with a banner, nameInput, createSession ,join session and HighscoreTable
class Main_menu extends React.Component {
    componentDidMount() {
        clearInterval(checkCurrentPlayers)
        clearTimeout(gameEnding)
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

// extra Functions that have nothing to do with react
function nameCheck(){
    return document.getElementById("inputName").value === ""
}