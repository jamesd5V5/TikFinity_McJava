package org.mammothplugins.tiktoklive;

import org.mammothplugins.command.ConnectCommands;
import org.mammothplugins.command.UserCommands;
import org.mammothplugins.events.EventListener;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.plugin.SimplePlugin;

public class TikTokLive extends SimplePlugin {
    @Override
    protected void onPluginStart() {
        registerCommand(new ConnectCommands());
        registerCommand(new UserCommands());
        registerEvents(new EventListener());

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
