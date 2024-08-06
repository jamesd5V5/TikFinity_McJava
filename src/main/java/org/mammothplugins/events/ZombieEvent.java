package org.mammothplugins.events;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.tiktoklive.TikTokLive;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class ZombieEvent extends EventClass {

    private Player player;
    private String username;

    public ZombieEvent(Player player, String username) {
        this.player = player;
        this.username = username;
    }

    @Override
    public void runEvent() {
        World world = player.getWorld();
        playSound();

        int tickCount = 0;
        for (int i = 0; i < 10; i++) {
            tickCount += 3;
            new BukkitRunnable() {
                @Override
                public void run() {
                    Zombie zombie = world.spawn(new Locations("Spawner").getSpawnLocation().add(0, 2, 0), Zombie.class);
                    customZombie(zombie);
                    CompSound.NOTE_PLING.play(player, 0.2f, 1f);
                }
            }.runTaskLater(TikTokLive.getInstance(), tickCount);
        }

    }

    protected void playSound() {
        CompSound.ZOMBIE_METAL.play(player, 1f, -0.4f);
    }

    private void customZombie(Zombie zombie) {
        zombie.setCustomName(ChatColor.GREEN + username);
        zombie.getEquipment().setHelmet(ItemCreator.of(CompMaterial.PLAYER_HEAD, username + "'s head", "").skullOwner(username).make());

        //May be cool to give the equpiment upgrades based on levels
        zombie.getEquipment().setItemInMainHand(ItemCreator.of(CompMaterial.ROSE_BUSH, "Rose", "").make());
    }
}
