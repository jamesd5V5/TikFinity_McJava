package org.mammothplugins.events;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.mammothplugins.tiktoklive.TikTokLive;
import org.mammothplugins.tool.Locations;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.Random;

public class EventBoss extends EventClass {

    @Getter
    private static boolean activeBoss;
    @Getter
    private static double bossHealth;
    @Getter
    private static double bossMaxHealth;

    private Player player; //Plmchild, no connect to Users

    public EventBoss(Player player) {
        this.player = player;
    }

    public void runEvent() {
        World world = player.getWorld();
        Location location = new Locations("Spawners").getBossLocation().add(0, 2, 0);
        playSound();
        Remain.sendActionBar(player, "&cA Boss has been summoned!");

        IronGolem ironGolem = world.spawn(location, IronGolem.class);
        customIronGolem(ironGolem);
        setActiveBoss(true);
    }

    protected void playSound() {
        CompSound.ENDERDRAGON_GROWL.play(player, 2f, -0.4f);
    }

    private void customIronGolem(IronGolem ironGolem) {
        ironGolem.setCustomName(ChatColor.RED + "BOSS");
        ironGolem.setCustomNameVisible(true);

        int count = 0;
        for (Entity entity : player.getNearbyEntities(15, 15, 15))
            if (entity instanceof Zombie)
                count++;
        int health = 50 + (count * 15);
        if (health > 2048)
            health = 2048;

        ironGolem.setMaxHealth(health);
        setBossMaxHealth(health);
        ironGolem.setHealth(health);
        setBossHealth(health);

        runPowerSlam(ironGolem);
    }

    private void runPowerSlam(IronGolem ironGolem) {
        Random random = new Random();
        final int[] ran = {100};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ironGolem.isDead()) {
                    cancel();
                    return;
                } else {
                    ran[0] = random.nextInt(120 - 60) + 60;
                    if (ironGolem.getNearbyEntities(5, 5, 5).size() > 2) {
                        ironGolem.setVelocity(new Vector(0, 0.9, 0));
                        CompSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR.play(ironGolem.getLocation(), 0.8f, 1.5f);
                        new BukkitRunnable() { //The Slam
                            @Override
                            public void run() {
                                if (ironGolem.isDead()) {
                                    cancel();
                                    return;
                                } else {
                                    ironGolem.setVelocity(new Vector(0, -0.9, 0));
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            boolean stop = whenBossTouchesGround(ironGolem);
                                            if (ironGolem.isDead() || stop) {
                                                cancel();
                                                return;
                                            }
                                        }
                                    }.runTaskTimer(TikTokLive.getInstance(), 0, 2);
                                }
                            }
                        }.runTaskLater(TikTokLive.getInstance(), 10);
                    }
                }
            }
        }.runTaskTimer(TikTokLive.getInstance(), 100, ran[0]);
    }

    private boolean whenBossTouchesGround(IronGolem ironGolem) {
        if (ironGolem.isOnGround()) {
            CompSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR.play(ironGolem.getLocation(), 0.8f, 0.5f);
            for (int i = 0; i < 20; i++) {
                CompParticle.SMOKE_LARGE.spawn(RandomUtil.nextLocation(ironGolem.getLocation(), 2, false));
                CompParticle.SMOKE_NORMAL.spawn(RandomUtil.nextLocation(ironGolem.getLocation(), 2, false));
            }
            for (Entity entity : ironGolem.getNearbyEntities(2, 2, 2))
                if (entity instanceof Zombie) {
                    Vector direction = entity.getLocation().toVector().subtract(ironGolem.getLocation().toVector());
                    direction.normalize().multiply(1.5);
                    entity.setVelocity(direction);
                    ((Zombie) entity).damage(4);
                }
            return true;
        }
        return false;
    }

    public static void setActiveBoss(boolean activeBoss) {
        EventBoss.activeBoss = activeBoss;
    }

    public static void setBossHealth(double bossHealth) {
        EventBoss.bossHealth = bossHealth;
    }

    public static void setBossMaxHealth(double bossMaxHealth) {
        EventBoss.bossMaxHealth = bossMaxHealth;
    }
}
