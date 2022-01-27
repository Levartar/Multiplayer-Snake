//define the "createSession" with label, button and event listeners
class CreateSession extends React.Component{
    componentDidMount() {
        document.getElementById("buttonnewGame").addEventListener("click", () => {
            //create a new lobby
            if(nameCheck()){
                document.getElementById("inputName").value = "Guest"+Math.floor(Math.random()*999999)
            }

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
                            let name = document.getElementById("inputName").value

                            getGameInfo(sessionID)
                            websockets(name, sessionID)
                        })
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
                <Button text={"play"} name={"newGame"}/>
            </div>
        )
    }
}