package org.mammothplugins.livestream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.mineacademy.fo.Common;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;

public class WebSocketIntegration {

    public static void connectToTikTok() {
        if (getJsFileFromClassPath("TikTokWebSocketConnector.js") == null) {
            Common.log("Could not locate Js File");
            return;
        }
        Common.log("FOUND Js File!");
        try {
            // Start the HTTP server for handling WebSocket events
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/spigot-event", new TikTokLiveEvent());
            server.setExecutor(null);
            server.start();
            Common.log("HTTP Server started on port 8080");

            // Start the JavaScript WebSocket client in a new process
            startJavaScriptWebSocket();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startJavaScriptWebSocket() {
        try {
            // Get the JavaScript file from the classpath
            File jsFile = getJsFileFromClassPath("TikTokWebSocketConnector.js");
            String scriptPath = jsFile.getAbsolutePath();
            Common.log("Script Path: " + scriptPath);

            // Start the JavaScript process
            ProcessBuilder builder = new ProcessBuilder("node", scriptPath);
            Process process = builder.start();

            Common.log("Started WebSocket JavaScript client");

            // Optionally, capture and log the output of the JavaScript process
            new Thread(() -> {
                try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Common.log("JS Client Output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getJsFileFromClassPath(String fileName) {
        // Get the URL of the resource
        URL resourceUrl = WebSocketIntegration.class.getResource(fileName);
        if (resourceUrl == null) {
            Common.log("Resource Not Found");
            return null;
        }

        // Convert URL to file path
        File jsFile = new File(resourceUrl.getFile());
        if (!jsFile.exists()) {
            Common.log("Js Not Found");
            return null;
        }

        return jsFile;
    }

    static class TikTokLiveEvent implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read incoming event from JavaScript WebSocket
                String event = new String(exchange.getRequestBody().readAllBytes());
                System.out.println("Received event from JavaScript: " + event);

                // Process the event (e.g., broadcast to Minecraft players)
                // TODO: Add your logic here for handling the event

                // Send response
                String response = "Event received";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
