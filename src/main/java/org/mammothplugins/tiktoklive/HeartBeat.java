package org.mammothplugins.tiktoklive;

import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.users.PlayerCache;
import org.mineacademy.fo.Common;

public class HeartBeat extends BukkitRunnable {
    @Override
    public void run() {
        for (String username : PlayerCache.getUsernames()) {
            PlayerCache playerCache = PlayerCache.from(username);
            //May take much power to run... So may hvae just have to hardcode somthing in PlayerCache
        }
    }

    public static void runEvery5Mins() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String username : PlayerCache.getUsernames()) {
                    PlayerCache playerCache = PlayerCache.from(username);
                    playerCache.save();
                }
                Common.log("Saved PlayerCache of " + PlayerCache.getUsernames().size() + " players.");
            }
        }.runTaskTimer(TikTokLive.getInstance(), 10L, 6000L);
    }
}
