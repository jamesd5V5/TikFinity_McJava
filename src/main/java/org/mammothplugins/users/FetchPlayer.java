package org.mammothplugins.users;

//import org.json.JSONObject;

import org.mineacademy.fo.jsonsimple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FetchPlayer {

    private static String nameURL = "https://api.mojang.com/users/profiles/minecraft/";

    private static JSONObject getPlayerJson(String username) throws IOException {
        URL url;
        url = new URL(nameURL + username);
        URLConnection urlConnection = url.openConnection();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line);
        bufferedReader.close();
        if (!stringBuilder.toString().startsWith("{"))
            return null;
        return new JSONObject();
    }

    public static boolean doesPlayerExist(String username) {
        try {
            if (getPlayerJson(username) != null)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}