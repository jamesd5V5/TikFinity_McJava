package org.mammothplugins.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.mammothplugins.tiktoklive.Values;
import org.mammothplugins.users.PlayerCache;
import org.mammothplugins.tiktoklive.TikTokLive;
import org.mammothplugins.tool.Locations;
import org.mammothplugins.users.Rankings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventZombie extends EventClass {

    private Player player; //Plmchild, no connect to Users
    private String tkUsername;
    private PlayerCache playerCache;

    public EventZombie(Player player, String tkUsername) {
        this.player = player;
        this.tkUsername = tkUsername;
        this.playerCache = PlayerCache.from(tkUsername);
    }

    @Override
    public void runEvent() {
        World world = player.getWorld();
        Location location = new Locations("Spawners").pickRandomMobLocation().add(0, 2, 0);
        playSound();

        int tickCount = 0;
        Values values = new Values("TikTokValues");
        for (int i = 0; i < values.getMinLikesToInteract(); i++) {
            tickCount += 3;
            new BukkitRunnable() {
                @Override
                public void run() {
                    Zombie zombie = world.spawn(location, Zombie.class);
                    zombie.getEquipment().clear();
                    if (!tkUsername.equals("jamesd5")) {
                        customZombie(zombie);
                        CompSound.NOTE_PLING.play(player, 0.2f, 1f);
                    }
                }
            }.runTaskLater(TikTokLive.getInstance(), tickCount);
        }

    }

    protected void playSound() {
        CompSound.NOTE_BASS.play(player, 1f, -0.4f);
    }

    private void customZombie(Zombie zombie) {
        zombie.setCustomName(playerCache.getLevels().getChatColor() + tkUsername);
        zombie.setCustomNameVisible(true);

        if (playerCache.hasConnectedMinecraftAccount())
            zombie.getEquipment().setHelmet(ItemCreator.of(CompMaterial.PLAYER_HEAD, tkUsername + "'s head", "").skullOwner(playerCache.getPlayerName()).make());

        int rank = playerCache.getRank();
        switch (rank) {
            case 1:
                setZombieArmor("LEATHER", zombie);
                break;
            case 2:
                setZombieArmor("GOLDEN", zombie);
                break;
            case 3:
                setZombieArmor("IRON", zombie);
                break;
            case 4:
                setZombieArmor("DIAMOND", zombie);
                break;
            case 5:
                setZombieArmor("NETHERITE", zombie);
                break;
            case 6:
                setZombieArmor("NETHERITE", zombie);
                break;
            default:
                break;
        }
    }

    private void setZombieArmor(String materialName, Zombie zombie) {
        /*
        Rank 0: Nothing
        Rank 1_1 -> Boots
        Rank 1_2 -> Leggings
        Rank 1_3 -> Chestplate
        Rank 1_4 -> Sword
         */
        int tier = playerCache.getLevels().getTierOfRank();
        boolean isMaxLvl = Rankings.isMaxedLvl(playerCache.getLevel());
        List<String> equipmentTypes = new ArrayList<>();
        if (tier >= 1 || isMaxLvl)
            equipmentTypes.add("BOOTS");
        if (tier >= 2 || isMaxLvl)
            equipmentTypes.add("LEGGINGS");
        if (tier >= 3 || isMaxLvl)
            equipmentTypes.add("CHESTPLATE");
        if (tier == 4 || isMaxLvl)
            equipmentTypes.add("SWORD");

        int count = 1;
        for (String equipment : equipmentTypes) {
            ItemStack item;
            if (equipment.equals("SWORD")) {
                CompMaterial compMaterial;
                if (materialName.equals("LEATHER"))
                    compMaterial = CompMaterial.WOODEN_SWORD;
                else if (materialName.equals("GOLDEN"))
                    compMaterial = CompMaterial.GOLDEN_SWORD;
                else if (materialName.equals("IRON"))
                    compMaterial = CompMaterial.IRON_SWORD;
                else if (materialName.equals("DIAMOND"))
                    compMaterial = CompMaterial.DIAMOND_SWORD;
                else if (materialName.equals("NETHERITE"))
                    compMaterial = CompMaterial.NETHERITE_SWORD;
                else
                    compMaterial = CompMaterial.AIR;
                item = ItemCreator.of(compMaterial, "", "").make();
            } else {
                if (isMaxLvl)
                    item = ItemCreator.of(CompMaterial.fromString(materialName + "_" + equipment), "", "").enchant(Enchantment.PROTECTION_ENVIRONMENTAL).make();
                else
                    item = ItemCreator.of(CompMaterial.fromString(materialName + "_" + equipment), "", "").make();
            }
            if (count == 1)
                zombie.getEquipment().setBoots(item);
            else if (count == 2)
                zombie.getEquipment().setLeggings(item);
            else if (count == 3)
                zombie.getEquipment().setChestplate(item);
            else zombie.getEquipment().setItemInMainHand(item);
            count++;
        }
    }

    public static int getAliveZombies() {
        int zombieCount = 0;
        for (Player player : Remain.getOnlinePlayers()) {
            for (Entity entity : player.getNearbyEntities(10, 10, 10))
                if (entity instanceof Zombie)
                    zombieCount++;
        }
        return zombieCount;
    }

}
