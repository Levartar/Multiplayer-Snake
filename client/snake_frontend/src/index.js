import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';

const images = {
    banner: require("./assets/banner.png")
}

function Button(props){
    return(
        <div id={props.name + "ButtonDiv"}>
            <button id={"button" + props.name}>{props.text}</button>
        </div>
    )
}

function Input(props){
    return(
        <div id={props.name.toLowerCase()}>
            <div id={props.name + "LabelDiv"}>
                <label htmlFor={"input" + props.name}>{props.text}</label>
            </div>
            <div id={props.name + "InputDiv"}>
                <input id={"input" + props.name} type={props.type} name={props.name}/>
            </div>
        </div>
    )
}

function Banner(){
    return(
        <div>
            <img src={images.banner} alt="Snake.IO banner image" />
        </div>
    )
}

class Enter_name extends React.Component{
    render() {
        return(
            <div>
                <Input name={"Name"} type={"text"} text={"Enter your name: "}/>
            </div>
        )
    }
}
class Create_session extends React.Component{
    componentDidMount() {
        document.getElementById("buttonnewGame").addEventListener("click", () => {
            ReactDOM.render(
                <Lobby />,
                document.getElementById('root')
            );
        })
    }

    componentWillUnmount() {
        document.getElementById("buttonnewGame").removeEventListener("click", () => {})
    }

    render() {
        return(
            <div>
                <div id="newGameLabelDiv">
                    <label htmlFor="buttonNewGame">Create a game</label>
                </div>
                <div id="emptySpaceDiv">
                    <br/>
                </div>
                <Button text={"play"} name={"newGame"}/>

            </div>
        )
    }
}

class Join_session extends React.Component{
    componentDidMount() {
        document.getElementById("buttonsessionId").addEventListener("click", () => {
            ReactDOM.render(
                <Lobby />,
                document.getElementById('root')
            );
        })
    }

    componentWillUnmount() {
        document.getElementById("buttonsessionId").removeEventListener("click", () => {})
    }

    render() {
        return(
            <div>
                <Input name={"sessionId"} type={"text"} text={"Join a Session"}/>
                <Button text={"join"} name={"sessionId"}/>
            </div>
        )
    }
}

class Main_menu extends React.Component {
    render(){
        return (
            <div>
                <header>
                    <Banner />
                </header>
                <main>
                    <Enter_name />
                    <div id="enterLobby">
                        <Create_session />
                        <Join_session />
                    </div>
                </main>
            </div>
        )
    }
}

let player = [
    {name: "player1", color: "red"},
    {name: "player2", color: "green"},
    {name: "player3", color: "yellow"},
    {name: "player4", color: "blue"}
]

function Table(){
    return(
        <div>
            <table>
                <thead>
                    <tr>
                        <th colSpan="2">Player</th>
                    </tr>
                </thead>
                <tbody>
                    {player.map((player, i) =>
                        <tr key={i}>
                            <td>{player.name}</td>
                            <td>{player.color}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    )
}

class Lobby extends React.Component {
    componentDidMount() {
        document.getElementById("buttonexitLobby").addEventListener("click", () => {
            ReactDOM.render(
                <Main_menu />,
                document.getElementById('root')
            );
        })

        document.getElementById("buttonstartGame").addEventListener("click", () => {
            ReactDOM.render(
                <Game />,
                document.getElementById('root')
            );
        })
    }

    componentWillUnmount() {
        document.getElementById("buttonexitLobby").removeEventListener("click", () => {

        })
    }

    render() {
        return(
            <div>
                <div id="lobby">
                    <div id="lobbyDetails">
                        <div id="lobbyHeadlineDIV">
                            <h1 id="lobbyHeadline">Lobby</h1>
                        </div>
                        <div id="sessionIDDIV">
                            <h2 id="sessionIDHeadline">SessionID:</h2>
                            <br />
                            <p id="sessionIDtext">X15QW4B</p>
                        </div>
                        <div id="startGameDIV">
                            <Button text={"Start Game"} name={"startGame"}/>
                        </div>
                    </div>
                    <div id="playerTable">
                        <Table />
                    </div>
                </div>
                <div>
                    <Button name={"exitLobby"} text={"return to Main menu"}/>
                </div>
            </div>
        )
    }
}

class Game extends React.Component {

}

ReactDOM.render(
    <Main_menu />,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
