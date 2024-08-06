package org.mammothplugins.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mammothplugins.events.ZombieEvent;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.List;

public class ConnectCommands extends SimpleCommand {
    public ConnectCommands() {
        super("tk");
        //setPermission("piano.commands");
    }

    @Override
    protected void onCommand() {
        boolean foundCommand = false;
        if (this.args.length == 1) {
            if ("setspawn".equalsIgnoreCase(this.args[0])) {
                Player player = (Player) getSender();
                Locations locations = new Locations("Spawner");
                locations.setSpawnLocation(player.getLocation());
                Common.broadcast("&7SpawnerLocation has been set.");

                foundCommand = true;
            }
        }
        if (this.args.length == 2) {
            //tk username zombies
            if ("zombies".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ZombieEvent event = new ZombieEvent(player, args[0]);
                    event.runEvent();
                    Common.tell(player, "User " + this.args[0] + " sent Zombies!");

                }
                foundCommand = true;
            }
        }
        if (!foundCommand) {
            Common.tell(this.getPlayer(), new String[]{"&cWrong Arguments! Use /piano for help."});
            CompSound.VILLAGER_NO.play(this.getPlayer());
        }
    }

    @Override
    protected List<String> tabComplete() {
        List<String> list1 = new ArrayList<>();
        list1.add("setspawn");
        list1.add("RainyMcWarm");

        List<String> list2 = new ArrayList<>();
        list2.add("zombies");

        return this.args.length == 1 ? list1 : list2;
    }
}
