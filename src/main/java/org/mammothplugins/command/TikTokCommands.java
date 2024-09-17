package org.mammothplugins.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mammothplugins.events.EventBoss;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.livestream.WebSocketIntegration;
import org.mammothplugins.tiktoklive.*;
import org.mammothplugins.users.PlayerCache;
import org.mammothplugins.tool.Locations;
import org.mammothplugins.users.Rankings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.List;

public class TikTokCommands extends SimpleCommand {

    private boolean foundCommand;

    public TikTokCommands() {
        super("tk");
        //setPermission("piano.commands");
    }

    @Override
    protected void onCommand() {
        if (getSender() instanceof Player)
            inGameCommands();
        else
            consoleCommands();


        if (!foundCommand) {
            Common.broadcast(new String[]{"&cWrong Arguments! Use /tk for help."});
            if (getSender() instanceof Player)
                CompSound.VILLAGER_NO.play(this.getPlayer());
        }
        foundCommand = false;
    }

    private void inGameCommands() {
        if (this.args.length == 1) {
            if ("start".equalsIgnoreCase(this.args[0])) {
                checkConsole();
                Player player = (Player) getSender();
                TimeCore.start(player);
                foundCommand = true;
            } else if ("stop".equalsIgnoreCase(this.args[0])) {
                checkConsole();
                Player player = (Player) getSender();
                TimeCore.stop(player);
                foundCommand = true;
            }
        }


        if (this.args.length == 2) {
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

        }
    }

    private void consoleCommands() {
        if (this.args.length == 0) {
            Common.log("=============TkConnect=============");
            Common.log("&7||tk wipe|save|countZ|rankInfo");
            Common.log("&7tk (username) info|follows|likes|zombies");
            Common.log("&7tk (username) info|follows|likes|zombies|reset");
            Common.log("&7tk (username) likes (Add Amount of Likes)");
            Common.log("&7tk (username) lvls (Add Amount of Lvls)");
            Common.log("&7||tk stress (Likes Per Second)");
            Common.log("&7||tk setMinLikes (Min Likes To Call Event)");
            Common.log("===================================");
            foundCommand = true;
        }
        if (this.args.length == 1) {
            if ("save".equalsIgnoreCase(this.args[0])) {
                int count = 0;
                for (String username : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(username);
                    playerCache.save();
                    count++;
                }
                Common.log("&7Saved " + count + " PlayerCaches.");
                foundCommand = true;
            } else if ("countZ".equalsIgnoreCase(this.args[0])) { //counts Zombies
                Common.log("Zombie count: " + EventZombie.getAliveZombies());
                foundCommand = true;
            } else if ("rankInfo".equalsIgnoreCase(this.args[0])) {
                Rankings.listRanksWithPoints();
                foundCommand = true;


                //NOT WORKING RIGHT
            } else if ("wipe".equalsIgnoreCase(this.args[0])) { //Resets only current caches
                for (String userName : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(userName);
                    playerCache.resetCache();
                    playerCache.save();
                }
                Common.broadcast("&7Cleared All PlayerCaches.");
                foundCommand = true;
            } else if ("connect".equalsIgnoreCase(this.args[0])) {
                WebSocketIntegration.connectToTikTok();
                foundCommand = true;
            }
        }

        if (this.args.length == 2) {
            String username = args[0];
            PlayerCache playerCache = PlayerCache.from(username);
            Values values = new Values("TikTokValues");

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
            if ("setMinLikes".equalsIgnoreCase(this.args[0])) {
                int minLikes = Integer.parseInt(args[1]);
                values.setMinLikesToInteract(minLikes);
                Common.log("Set minimum amount of likes to " + args[1] + " to call an event.");
                foundCommand = true;
            }
            if ("info".equalsIgnoreCase(this.args[1])) {
                Common.log("==================================");
                Common.log("Follows: " + playerCache.isFollowing());
                Common.log("Rank: " + playerCache.getRank() + "_" + playerCache.getTierOfRank() + " (" + playerCache.getLevel() + ")");
                Common.log("Player: " + playerCache.getPlayerName());
                Common.log("Likes: " + playerCache.getCurrentLikes() + "/" + playerCache.getTotalLikes());
                Common.log("==================================");
                foundCommand = true;
            }
            if ("follows".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Common.tell(player, "&7TikTok User &6" + username + " &7followed!");
                    playerCache.setIsFollowing(true);
                }
                foundCommand = true;
            }
            if ("likes".equalsIgnoreCase(this.args[1])) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Common.tell(player, "&7User " + username + " sent &6" + values.getMinLikesToInteract() + " &7Likes!");
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
                playerCache.resetCache();
                Common.log("User " + username + " has been reset.");
                foundCommand = true;
            }
        }

        if (this.args.length == 3) {
            String username = args[0];
            PlayerCache playerCache = PlayerCache.from(username);
            if ("likes".equalsIgnoreCase(this.args[1])) {
                playerCache.addCurrentLikes(Integer.parseInt(args[2]));
                Common.log("&7User " + username + " sent " + args[2] + " Likes!" + " Total: " + playerCache.getTotalLikes());
                foundCommand = true;
            }
            if ("lvl".equalsIgnoreCase(this.args[1])) {
                playerCache.advanceLvl(Integer.parseInt(args[2]));
                Common.log("&7User " + username + " has advanced to Rank " + playerCache.getLevels().getChatColor() + playerCache.getRank() + "_" + playerCache.getTierOfRank() + "(" + playerCache.getLevel() + ")!");
                foundCommand = true;
            }
        }
    }

    @Override
    protected List<String> tabComplete() {
        List<String> list1 = new ArrayList<>();
        list1.add("setspawn");
        list1.add("start");
        list1.add("stop");

        List<String> list3 = new ArrayList<>();
        list3.add("boss");
        list3.add("mob1");
        list3.add("mob2");
        list3.add("mob3");
        list3.add("player");

        return this.args.length == 1 ? list1 : list3;
    }
}
