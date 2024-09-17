package org.mammothplugins.tiktoklive;

import org.bukkit.event.Listener;
import org.mammothplugins.command.TikTokCommands;
import org.mammothplugins.command.UserCommands;
import org.mammothplugins.events.EventListener;
import org.mammothplugins.livestream.WebSocketIntegration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;

public class TikTokLive extends SimplePlugin {
    @Override
    protected void onPluginStart() {
        registerCommand(new TikTokCommands());
        registerCommand(new UserCommands());

        registerEvents(new EventListener());

        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
            Common.log("mkdirs");
        }
        WebSocketIntegration.initializePluginFiles(pluginFolder);

        HeartBeat.runEvery5Mins();
    }

    @Override
    protected void onReloadablesStart() {
        //Variable.loadVariables();
    }

    public static TikTokLive getInstance() {
        return (TikTokLive) SimplePlugin.getInstance();
    }
}
