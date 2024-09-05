package org.mammothplugins.tiktoklive;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.events.EventBoss;
import org.mammothplugins.users.PlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class HeartBeat extends BukkitRunnable {
    
    private Player pltmchild;
    private boolean isOnCooldown;

    public HeartBeat(Player pltmchild) {
        this.pltmchild = pltmchild;
        this.isOnCooldown = false;
    }

    @Override
    public void run() {
        if (EventBoss.isActiveBoss()) {
            Remain.sendActionBar(pltmchild, "&c&lBoss Health: &r&c" + EventBoss.getBossHealth() + "/" + EventBoss.getBossMaxHealth());
        } else if (isOnCooldown == false) {
            isOnCooldown = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    EventBoss eventBoss = new EventBoss(pltmchild);
                    eventBoss.runEvent();
                    isOnCooldown = false;
                }
            }.runTaskLater(TikTokLive.getInstance(), 100);
        }

//        for (String username : PlayerCache.getUsernames()) {
//            PlayerCache playerCache = PlayerCache.from(username);
//            //May take much power to run... So may hvae just have to hardcode somthing in PlayerCache
//
//
//        }
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
