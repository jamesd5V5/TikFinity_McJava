package org.mammothplugins.command;

import jdk.jfr.Event;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.mammothplugins.events.EventBoss;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.tiktoklive.HeartBeat;
import org.mammothplugins.tiktoklive.StressTest;
import org.mammothplugins.tiktoklive.TikTokLive;
import org.mammothplugins.tiktoklive.TimeCore;
import org.mammothplugins.users.FetchPlayer;
import org.mammothplugins.users.PlayerCache;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

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
            if ("save".equalsIgnoreCase(this.args[0])) {
                for (String username : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(username);
                    playerCache.save();
                }
                Common.broadcast("&7Saved All PlayerCaches.");

                foundCommand = true;
            } else if ("wipe".equalsIgnoreCase(this.args[0])) {
                for (String userName : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(userName);
                    playerCache.resetTotalLikes();
                    playerCache.resetLvl();
                }
                PlayerCache.clearCaches();
                PlayerCache basic = PlayerCache.from("jamesd5");
                basic.save();
                Common.broadcast("&7Cleared All PlayerCaches.");
                foundCommand = true;
            } else if ("butcher".equalsIgnoreCase(this.args[0])) {
                Player player = (Player) getSender();
                int count = 0;
                for (Entity entity : player.getNearbyEntities(50, 50, 50))
                    if (entity instanceof Zombie) {
                        entity.remove();
                        count++;
                    }
                Common.broadcast("&7Removed " + count + " zombies.");
                foundCommand = true;
            } else if ("start".equalsIgnoreCase(this.args[0])) {
                Player player = (Player) getSender();
                TimeCore.start(player);
                foundCommand = true;
            } else if ("stop".equalsIgnoreCase(this.args[0])) {
                Player player = (Player) getSender();
                TimeCore.stop(player);
                foundCommand = true;
            } else {
                String username = args[0];
                if (FetchPlayer.doesPlayerExist(username)) {
                    PlayerCache playerCache = PlayerCache.from(username);
                    playerCache.setPlayerName(username);
                    Common.broadcast("User has connect to the mc account " + username);
                } else {
                    Common.broadcast("&cMc Account " + username + " does not exist.");
                }
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
                Common.broadcast("Rank: " + playerCache.getRank() + "_" + playerCache.getTierOfRank() + " (" + playerCache.getLevel() + ")");
                Common.broadcast("Player: " + playerCache.getPlayerName());
                Common.broadcast("Likes: " + playerCache.getCurrentLikes() + "/" + playerCache.getTotalLikes());
                Common.broadcast("==================================");
                foundCommand = true;
            }
            if ("follows".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Common.tell(player, "&7User " + username + " followed!");
                    playerCache.setIsFollowing(true);
                }
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
                    //Common.tell(player, "&6&lUser " + username + " sent Zombies!");
                    //Remain.send

                }
                foundCommand = true;
            }
            if ("reset".equalsIgnoreCase(this.args[1])) {
                playerCache.resetTotalLikes();
                playerCache.resetLvl();
                //playerCache.resetCurrentGifts();
                Common.broadcast("User " + username + " has been reset.");
                foundCommand = true;
            }
            if ("setspawn".equalsIgnoreCase(this.args[0])) {
                String name = this.args[1];
                Player player = (Player) getSender();
                Locations locations = new Locations("Spawners");
                if (name.equals("boss"))
                    locations.setBossLocation(player.getLocation());
                else if (name.equals("mob1"))
                    locations.setMobLocation1(player.getLocation());
                else if (name.equals("mob2"))
                    locations.setMobLocation2(player.getLocation());
                else if (name.equals("mob3"))
                    locations.setMobLocation3(player.getLocation());
                else if (name.equals("player"))
                    locations.setPlayerLocation(player.getLocation());
                Common.broadcast(name + "&7 has been set.");

                foundCommand = true;
            }
            if ("boss".equalsIgnoreCase(this.args[0])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EventBoss event = new EventBoss(player);
                    event.runEvent();
                }
                foundCommand = true;
            }
            if ("stress".equalsIgnoreCase(this.args[0])) {
                StressTest stressTest = new StressTest(getPlayer());
                stressTest.testZombiesPerSecond(Integer.parseInt(args[1]));
                Common.broadcast("Running stress test with " + args[1] + " likes a second.");
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
            if ("lvl".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < (Integer.parseInt(args[2])); i++)
                        playerCache.advanceLvl();
                    Common.tell(player, "&7User " + username + " has advanced to Rank " + playerCache.getLevels().getChatColor() + playerCache.getRank() + "_" + playerCache.getTierOfRank() + "(" + playerCache.getLevel() + ")!");
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
        list1.add("butcher");
        list1.add("start");
        list1.add("stop");
        list1.add("boss");

        List<String> list2 = new ArrayList<>();
        list2.add("zombies");
        list2.add("reset");
        list2.add("info");
        list2.add("likes");
        list2.add("lvl");

        List<String> list3 = new ArrayList<>();
        list3.add("boss");
        list3.add("mob1");
        list3.add("mob2");
        list3.add("mob3");
        list3.add("player");

        List<String> nextList = this.args[0].equals("setspawn") ? list3 : list2;
        return this.args.length == 1 ? list1 : nextList;
    }
}
