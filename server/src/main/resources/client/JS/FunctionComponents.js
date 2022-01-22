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

//define how the HighscoreTable looks like
function PlayerScore(props){
    return(
        <div>
            <table id={props.id}>
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
                        <td>{props.data[i].points}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}

//define a list of player that are currently in the lobby
function List(props){
    return(
        <div>
            <h2>Player:</h2>
            <ul>
                {props.players.map((player, i) =>
                    //console.log(player + " and " + i)
                    <li id={"player" + i}>{player}</li>
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
                    <option key={i}>{props.data[i]}</option>
                )}
            </select>
        </div>
    )
}