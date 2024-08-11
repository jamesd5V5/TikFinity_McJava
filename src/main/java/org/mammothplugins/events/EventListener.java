package org.mammothplugins.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompParticle;

public class EventListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        event.getDrops().clear();
        event.setDroppedExp(0);

        if (entity instanceof Zombie)
            for (int i = 0; i < 5; i++)
                CompParticle.REDSTONE.spawn(RandomUtil.nextLocation(entity.getLocation(), 1, true));
    }
}
