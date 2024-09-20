package org.mammothplugins.livestream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.mineacademy.fo.Common;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Files;

public class WebSocketIntegration {

    public static void connectToTikTok(File pluginFolder) {
        if (!new File(pluginFolder, "TikTokWebSocketConnector.js").exists()) {
            Common.log("Could not locate JS file in plugin folder");
            return;
        }

        Common.log("FOUND Js File in plugin folder!");
        try {
            // Start the HTTP server for handling WebSocket events
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/spigot-event", new TikTokLiveEvent());
            server.setExecutor(null);
            server.start();
            Common.log("HTTP Server started on port 8080");

            // Start the JavaScript WebSocket client
            startJavaScriptWebSocket(pluginFolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startJavaScriptWebSocket(File pluginFolder) throws IOException {
        // Reference the JavaScript file from the plugin's folder
        File jsFile = new File(pluginFolder, "TikTokWebSocketConnector.js");

        if (!jsFile.exists()) {
            Common.log("Could not find JavaScript file in the plugin folder!");
            return;
        }

        // Use Node.js to run the JavaScript file
        String nodePath = "C:\\Program Files\\nodejs\\node.exe";
        ProcessBuilder builder = new ProcessBuilder(nodePath, jsFile.getAbsolutePath());
        Process process = null;
        try {
            process = builder.start();
        } catch (Exception e) {
            Common.log("Failed to locate Node.js. Please make sure Node.js is installed with 'Program Files\nodejs\node.exe'.");
        }

        Common.log("Started WebSocket JavaScript client from plugin folder");

        // Optionally capture the output of the JavaScript process
        Process finalProcess = process;
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Common.log("JS Client Output: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static InputStream getJsFileFromClassPath(String fileName) {
        // Get the resource as an InputStream
        InputStream resourceStream = WebSocketIntegration.class.getClassLoader().getResourceAsStream("org/mammothplugins/livestream/" + fileName);

        if (resourceStream == null) {
            Common.log("Resource Not Found: " + fileName);
            return null;
        }

        return resourceStream;
    }

    public static void initializePluginFiles(File pluginFolder) {
        File jsFile = new File(pluginFolder, "TikTokWebSocketConnector.js");

        if (!jsFile.exists()) {
            try (InputStream in = WebSocketIntegration.class.getClassLoader().getResourceAsStream("TikTokWebSocketConnector.js");
                 OutputStream out = new FileOutputStream(jsFile)) {

                if (in != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    Common.log("TikTokWebSocketConnector.js copied to plugin folder.");
                } else {
                    Common.log("Could not find TikTokWebSocketConnector.js in JAR resources.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static class TikTokLiveEvent implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read incoming event from JavaScript WebSocket
                String event = new String(exchange.getRequestBody().readAllBytes());
                Common.log("Received event from JavaScript: " + event);

                // Process the event (e.g., broadcast to Minecraft players)
                // TODO: Add your logic here for handling the event
                //t
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
