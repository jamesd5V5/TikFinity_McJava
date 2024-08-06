package org.mammothplugins.tiktoklive;

import org.mammothplugins.command.ConnectCommands;
import org.mammothplugins.command.GameToolsCommand;
import org.mineacademy.fo.plugin.SimplePlugin;

public class TikTokLive extends SimplePlugin {
    @Override
    protected void onPluginStart() {
        registerCommand(new ConnectCommands());
        registerCommand(new GameToolsCommand());
    }

    public static TikTokLive getInstance() {
        return (TikTokLive) SimplePlugin.getInstance();
    }
}
