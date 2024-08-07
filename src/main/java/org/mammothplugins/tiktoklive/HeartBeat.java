package org.mammothplugins.tiktoklive;

import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.users.PlayerCache;

public class HeartBeat extends BukkitRunnable {
    @Override
    public void run() {
        for (String username : PlayerCache.getUsernames()) {
            PlayerCache playerCache = PlayerCache.from(username);
            //May take much power to run... So may hvae just have to hardcode somthing in PlayerCache
        }
    }
}
