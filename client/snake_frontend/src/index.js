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
                <input id={"input" + props.name} type={props.type} name={props.name} maxLength={props.maxLength}/>
            </div>
        </div>
    )
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
                <Input name={"sessionId"} type={"text"} text={"Join a Session"} maxLength={"10"}/>
                <Button text={"join"} name={"sessionId"}/>
            </div>
        )
    }
}

class Main_menu extends React.Component {
    render(){
        return (
            <div className="Frame">
                <header>
                    <img src={images.banner} alt="Snake.IO banner image" />
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

let player = [
    {name: "Tim", color: "red"},
    {name: "Lukas", color: "green"},
    {name: "Felix", color: "yellow"},
    {name: "Timo", color: "blue"}
]

function TablePlayer(props){
    return(
        <div>
            <table>
                <thead>
                    <tr>
                        <th colSpan="2">{props.tableHead}</th>
                    </tr>
                </thead>
                <tbody>
                    {props.data.map((player, i) =>
                        <tr key={i}>
                            <td>{props.data[i].name}</td>
                            <td>{props.data[i].color}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    )
}

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
            <div className="Frame">
                <div id="exitLobby">
                    <Button name={"exitLobby"} text={"exit Lobby"}/>
                </div>
                <div id="lobbyHeadlineDIV">
                    <h1 id="lobbyHeadline">Lobby</h1>
                </div>
                <div id="lobby" className="noMargin">
                    <div id="sessionIDDIV">
                        <h2 id="sessionIDHeadline" className="noMargin">SessionID:</h2>
                        <p id="sessionIDtext" className="noMargin">X15QW4B</p>
                    </div>
                    <div id="playerTable">
                        <List />
                    </div>
                </div>
                <div id="startGameDIV">
                    <Button text={"Start Game"} name={"startGame"}/>
                </div>
            </div>
        )
    }
}

class Game extends React.Component {
    render() {
        return(
            <div className="Frame">

            </div>
        )
    }
}

ReactDOM.render(
    <React.StrictMode>
        <Lobby />
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
