package org.mammothplugins.events;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.users.PlayerCache;
import org.mammothplugins.tiktoklive.TikTokLive;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class EventZombie extends EventClass {

    private Player player;
    private String username;
    private PlayerCache playerCache;

    public EventZombie(Player player, String username) {
        this.player = player;
        this.username = username;
        this.playerCache = PlayerCache.from(username);
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

        if (playerCache.getRank() == 1) {
            ItemStack c1 = ItemCreator.of(CompMaterial.LEATHER_CHESTPLATE, username + "'s chestplate", "").make();
            ItemStack l1 = ItemCreator.of(CompMaterial.LEATHER_LEGGINGS, username + "'s leggings", "").make();
            ItemStack b1 = ItemCreator.of(CompMaterial.LEATHER_BOOTS, username + "'s boots", "").make();

            zombie.getEquipment().setChestplate(c1);
            zombie.getEquipment().setLeggings(l1);
            zombie.getEquipment().setBoots(b1);
        } else if (playerCache.getRank() == 2) {
            ItemStack c2 = ItemCreator.of(CompMaterial.GOLDEN_CHESTPLATE, username + "'s chestplate", "").make();
            ItemStack l2 = ItemCreator.of(CompMaterial.GOLDEN_LEGGINGS, username + "'s leggings", "").make();
            ItemStack b2 = ItemCreator.of(CompMaterial.GOLDEN_BOOTS, username + "'s boots", "").make();

            zombie.getEquipment().setChestplate(c2);
            zombie.getEquipment().setLeggings(l2);
            zombie.getEquipment().setBoots(b2);
        } else if (playerCache.getRank() == 3) {
            ItemStack c3 = ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE, username + "'s chestplate", "").make();
            ItemStack l3 = ItemCreator.of(CompMaterial.DIAMOND_LEGGINGS, username + "'s leggings", "").make();
            ItemStack b3 = ItemCreator.of(CompMaterial.DIAMOND_BOOTS, username + "'s boots", "").make();

            zombie.getEquipment().setChestplate(c3);
            zombie.getEquipment().setLeggings(l3);
            zombie.getEquipment().setBoots(b3);
        }
    }
}
