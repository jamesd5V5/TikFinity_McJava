package org.mammothplugins.users;

//import org.json.JSONObject;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.jsonsimple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (username.length() < 3 && username.length() > 16) {
            Common.broadcast("Not right length");
            return false;
        }
        Pattern special = Pattern.compile("[!@#$%&*()+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(username);
        if (hasSpecial.find()) {
            Common.broadcast("Found Special Chracter");
            return false;
        }
        try {
            if (getPlayerJson(username) != null)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}