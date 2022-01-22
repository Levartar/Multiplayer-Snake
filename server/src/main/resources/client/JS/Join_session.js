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