package org.mammothplugins.tiktoklive;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.events.EventZombie;

public class StressTest {

    private Player pltmchild;
    private boolean isRunning;

    public StressTest(Player pltmchild) {
        this.pltmchild = pltmchild;
    }

    public void testZombiesPerSecond(int likesPerSecond) {
        isRunning = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning == false || TimeCore.getHeartBeat().isCancelled()) {
                    cancel();
                    return;
                }
                for (int i = 0; i < likesPerSecond; i++) {
                    EventZombie event = new EventZombie(pltmchild, "dummie");
                    event.runEvent();
                }
            }
        }.runTaskTimer(TikTokLive.getInstance(), 0, 20);
    }

    public void stopRunning() {
        isRunning = false;
    }
}
