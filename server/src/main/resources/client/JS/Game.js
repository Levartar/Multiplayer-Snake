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
        document.addEventListener("keydown", sendInput)
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

function sendInput(event){
    switch (event.key){
        case 'w':
        case 'a':
        case 's':
        case 'd':
            ws.send(event.key)
            break
        case 'ArrowUp':
        case 'W':
            ws.send('w')
            break
        case 'ArrowLeft':
        case 'A':
            ws.send('a')
            break
        case 'ArrowDown':
        case 'S':
            ws.send('s')
            break
        case 'ArrowRight':
        case 'D':
            ws.send('d')
            break
    }
}