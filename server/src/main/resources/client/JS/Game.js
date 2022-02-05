let keyState = {}

//define the Game with eventListener, the gameBoard and a player List with the current score
class Game extends React.Component {
    componentDidMount() {
        document.getElementById("buttonexitGame").addEventListener("click", () => {
            ws.close()
            renderMain()
        })

        // clear the interval refreshing the player list
        clearInterval(checkCurrentPlayers)

        //----------PC-INPUTS------------------------------
        document.addEventListener('keydown', onKeyPress)
        document.addEventListener('keyup', onKeyRelease)

        //----------MOBILE-INPUTS------------------------------
        document.addEventListener('touchstart', handleTouchStart, false);
        document.addEventListener('touchmove', handleTouchMove, false);
    }

    componentWillUnmount(){
        document.removeEventListener("keydown", sendInput)
        document.removeEventListener("keydown", onKeyPress)
        document.removeEventListener("keyup", onKeyRelease)
        document.removeEventListener("touchstart", handleTouchStart)
        document.removeEventListener("touchmove", handleTouchMove)
    }

    render() {
        return(
            <div className="frame" id="frameGame">
                <div className="flexed" id="gameScreen">
                    <Button name={"exitGame"} text={"back"}/>
                    <div id="gameMap">
                        <canvas id="pseudoCanvas" width={this.props.width} height={this.props.height}></canvas>
                        <canvas id="worldCanvas" class="canvas" width={this.props.width} height={this.props.height}
                                style={{'z-index': 0}}>
                        </canvas>
                        <canvas id="snakeCanvas" class="canvas" width={this.props.width} height={this.props.height}
                                style={{'z-index': 1}}>
                        </canvas>
                        <canvas id="gridCanvas" class="canvas" width={this.props.width} height={this.props.height}
                                style={{'z-index': 2}}>
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

function sendInput(key){
    switch (key){
        case 'w':
        case 'a':
        case 's':
        case 'd':
            ws.send(key)
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

//----------PC-INPUTS------------------------------
function onKeyPress(event) {
    // keycode = event.code
    let change = keyState[event.key] !== true
    keyState[event.key] = true;

    if (change) {
        console.clear()
        console.log("Press", event.key)
        handleInput(event.key);
    }
}

function onKeyRelease(event) {
    console.clear()
    keyState[event.key] = false;
    console.log("Release", event.key)
    handleInput(null);
}

function handleInput(code) {
    for (let c in keyState) {
        console.log(c, keyState[c])
    }

    if (code != null) {
        sendInput(code)
    } else {
        for (let c in keyState) {
            if (true === keyState[c]) {
                console.error(c)
                sendInput(c)
                return
            }
        }
    }
}

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
}

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
            ws.send('w')
        } else {
            /* up swipe */
            ws.send('s')
        }
    }
    /* reset values */
    xDown = null;
    yDown = null;
}