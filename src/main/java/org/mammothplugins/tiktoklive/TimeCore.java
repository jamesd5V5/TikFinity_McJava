package org.mammothplugins.tiktoklive;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.mammothplugins.events.EventBoss;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.tool.Locations;
import org.mammothplugins.users.PlayerCache;
import org.mineacademy.fo.Common;

public class TimeCore {
    private static HeartBeat heartBeat;

    public static void start(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        Locations locations = new Locations("Spawners");
        Location loc = locations.getPlayerLocation();
        player.teleport(loc);

        for (Entity entity : player.getNearbyEntities(50, 50, 50))
            entity.remove();
        
        heartBeat = new HeartBeat(player);
        heartBeat.runTaskTimer(TikTokLive.getInstance(), 0l, 10l);
        ZombieTimer.runEvery30Secs();
        Common.log("&7McJava has started.");
    }

    public static void stop(Player player) {
        Common.log("&c======McJava has stopped======");
        Common.log("&7- Overall Likes: " + PlayerCache.getOverallLikes());
        Common.log("&7- Zombie count: " + EventZombie.getAliveZombies());
        Common.log("&7- Passing By Users: " + PlayerCache.getUnsavedCaches());

        for (Entity entity : player.getNearbyEntities(50, 50, 50))
            entity.remove();
        EventBoss.setActiveBoss(false);
        heartBeat.cancel();

        int count = 0;
        for (String username : PlayerCache.getUsernames()) {
            PlayerCache playerCache = PlayerCache.from(username);
            playerCache.save();
            count++;
        }
        Common.broadcast("&7- Saved " + count + " PlayerCaches.");
    }

    public static HeartBeat getHeartBeat() {
        return heartBeat;
    }

}
