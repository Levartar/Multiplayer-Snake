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
    if(mySelect !== undefined && mySelect !== null){
        let options = mySelect.children

        for (let i = 0; i < options.length; i++) {

            if(options[i].value === currentMap){
                options[i].selected = true
                break
            }
        }
    }
}