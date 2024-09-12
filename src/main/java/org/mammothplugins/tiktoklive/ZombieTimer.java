package org.mammothplugins.tiktoklive;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.events.EventBoss;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.users.PlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class ZombieTimer extends BukkitRunnable {

    @Override
    public void run() {

    }

    public static void runEvery30Secs() {
        Player pltmchild = null;
        for (Player player : Remain.getOnlinePlayers())
            pltmchild = player;
        Player finalPltmchild = pltmchild;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (TimeCore.getHeartBeat().isCancelled()) {
                    cancel();
                    return;
                }
                EventZombie zombie = new EventZombie(finalPltmchild, "jamesd5");
                zombie.runEvent();
            }
        }.runTaskTimer(TikTokLive.getInstance(), 10L, 20 * 30L);
    }
}
