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
        document.addEventListener('touchstart', handleTouchStart, false);
        document.addEventListener('touchmove', handleTouchMove, false);

        //Mobile Inputs from https://stackoverflow.com/users/7852/givanse
        let xDown = null;
        let yDown = null;

        function getTouches(evt) {
            return evt.touches ||             // browser API
                evt.originalEvent.touches; // jQuery
        }

        function handleTouchStart(evt) {
            const firstTouch = getTouches(evt)[0];
            xDown = firstTouch.clientX;
            yDown = firstTouch.clientY;
        };

        function handleTouchMove(evt) {
            if ( ! xDown || ! yDown ) {
                return;
            }

            let xUp = evt.touches[0].clientX;
            let yUp = evt.touches[0].clientY;

            let xDiff = xDown - xUp;
            let yDiff = yDown - yUp;

            if ( Math.abs( xDiff ) > Math.abs( yDiff ) ) {/*most significant*/
                if ( xDiff > 0 ) {
                    /* right swipe */
                    ws.send('a')
                } else {
                    /* left swipe */
                    ws.send('d')
                }
            } else {
                if ( yDiff > 0 ) {
                    /* down swipe */
                    ws.send('s')
                } else {
                    /* up swipe */
                    ws.send('w')
                }
            }
            /* reset values */
            xDown = null;
            yDown = null;
        };
    }

    render() {
        return(
            <div className="frame" id="frameGame">
                <div className="flexed" id="gameScreen">
                    <Button name={"exitGame"} text={"back"}/>
                    <div id="gameMap">
                        <canvas id="worldCanvas" width={this.props.width} height={this.props.height}
                                style="position: absolute; left: 0; top: 0; z-index: 0;">
                        </canvas>
                        <canvas id="snakeCanvas" width={this.props.width} height={this.props.height}
                                style="position: absolute; left: 0; top: 0; z-index: 1;">
                        </canvas>
                        <canvas id="gridCanvas" width={this.props.width} height={this.props.height}
                                style="position: absolute; left: 0; top: 0; z-index: 2;">
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

function mobileInputs(event){

}