package org.mammothplugins.command;

import org.bukkit.entity.Player;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.MenuTools;

import java.util.List;

public final class GameToolsCommand extends SimpleCommand {

    public GameToolsCommand() {
        super("tspawn");
    }

    @Override
    protected void onCommand() {
        this.checkConsole();
        Player player = (Player) getSender();
        Locations locations = new Locations("Spawner");
        locations.setSpawnLocation(player.getLocation());
        Common.broadcast("&7SpawnerLocation has been set.");
    }

    @Override
    protected List<String> tabComplete() {
        return NO_COMPLETE;
    }
}
