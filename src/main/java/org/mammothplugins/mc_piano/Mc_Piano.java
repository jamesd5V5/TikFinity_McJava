package org.mammothplugins.mc_piano;

import org.mammothplugins.command.PianoCommands;
import org.mineacademy.fo.plugin.SimplePlugin;

public class Mc_Piano extends SimplePlugin {
    @Override
    protected void onPluginStart() {
        registerCommand(new PianoCommands());
    }

    public static Mc_Piano getInstance() {
        return (Mc_Piano) SimplePlugin.getInstance();
    }
}
