let websocket = null;

function connect() {
    if (websocket) return; // Already connected

    websocket = new WebSocket("ws://localhost:21213/");

    websocket.onopen = function () {
        console.log("Connected");
    };

    websocket.onclose = function () {
        console.log("Disconnected");
        websocket = null;
        setTimeout(connect, 1000); // Schedule a reconnect attempt
    };

    websocket.onerror = function () {
        console.log("Connection Failed");
        websocket = null;
        setTimeout(connect, 1000); // Schedule a reconnect attempt
    };

    websocket.onmessage = function (event) {
        let parsedData = JSON.parse(event.data); // Parse the JSON data
        console.log("Data received", parsedData);

        // Send the event data to the Java server
        fetch('http://localhost:8080/spigot-event', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(parsedData)
        }).then(response => console.log("Event sent to Java"));
    };
}

window.addEventListener('load', connect);
