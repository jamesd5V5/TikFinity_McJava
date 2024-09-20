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
        console.log("Raw data received from TikTok WebSocket:", event.data);
            let parsedData = JSON.parse(event.data);
            console.log("Parsed Data received from TikTok WebSocket:", parsedData);

            // Send the event data to the Java server
            fetch('http://localhost:8080/spigot-event', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(parsedData)
            }).then(response => response.text())
              .then(data => console.log("Event sent to Java: ", data))
              .catch(error => console.error("Error sending event:", error));
    };
}

window.addEventListener('load', connect);
