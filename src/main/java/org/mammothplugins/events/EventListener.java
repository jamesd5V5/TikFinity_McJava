package org.mammothplugins.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.Remain;

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

    @EventHandler
    public void onEntityTakeDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof IronGolem) {
            double dmg = event.getDamage();
            if (((IronGolem) entity).getHealth() - dmg <= 0) { //Boss Died
                String killerName = event.getDamager().getCustomName();
                EventBoss.setActiveBoss(false);
                Common.broadcast("&6" + killerName + " killed " + entity.getCustomName());
                for (Player player : Bukkit.getOnlinePlayers())
                    Remain.sendActionBar(player, killerName + "&f killed " + entity.getCustomName() + "&f!");
            } else
                EventBoss.setBossHealth(((IronGolem) entity).getHealth());
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Chicken)
            event.getEntity().remove();
    }
}
