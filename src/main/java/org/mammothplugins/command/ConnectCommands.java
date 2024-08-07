package org.mammothplugins.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.users.PlayerCache;
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
            if ("save".equalsIgnoreCase(this.args[0])) {
                for (String username : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(username);
                    playerCache.save();
                }
                Common.broadcast("&7Saved All PlayerCaches.");

                foundCommand = true;
            }
            if ("wipe".equalsIgnoreCase(this.args[0])) {
                PlayerCache.clearCaches();
                PlayerCache basic = PlayerCache.from("jamesd5");
                basic.save();
                Common.broadcast("&7Cleared All PlayerCaches.");
                foundCommand = true;
            }
        }
        if (this.args.length == 2) {
            //tk username zombies
            String username = args[0];
            PlayerCache playerCache = PlayerCache.from(username);
            if ("info".equalsIgnoreCase(this.args[1])) {
                Common.broadcast("==================================");
                Common.broadcast("Follows: " + playerCache.isFollowing());
                Common.broadcast("Rank: " + playerCache.getRank());
                Common.broadcast("Likes: " + playerCache.getCurrentLikes() + "/" + playerCache.getTotalLikes());
                Common.broadcast("==================================");
                foundCommand = true;
            }
            if ("likes".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Common.tell(player, "&7User " + username + " sent a Like!");
                    playerCache.addCurrentLikes();
                }
                foundCommand = true;
            }
            if ("zombies".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EventZombie event = new EventZombie(player, username);
                    event.runEvent();
                    Common.tell(player, "&6&lUser " + username + " sent Zombies!");

                }
                foundCommand = true;
            }
            if ("reset".equalsIgnoreCase(this.args[1])) {
                playerCache.resetTotalLikes();
                playerCache.resetRank();
                //playerCache.resetCurrentGifts();
                Common.broadcast("User " + username + " has been reset.");
                foundCommand = true;
            }
        }
        if (this.args.length == 3) {
            String username = args[0];
            PlayerCache playerCache = PlayerCache.from(username);
            if ("likes".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < (Integer.parseInt(args[2])); i++)
                        playerCache.addCurrentLikes();
                    Common.tell(player, "&7User " + username + " sent " + args[2] + " Likes!" + " Total: " + playerCache.getTotalLikes());
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
        list1.add("save");
        list1.add("wipe");
        list1.add("RainyMcWarm");

        List<String> list2 = new ArrayList<>();
        list2.add("zombies");
        list2.add("reset");
        list2.add("info");
        list2.add("likes");

        return this.args.length == 1 ? list1 : list2;
    }
}
