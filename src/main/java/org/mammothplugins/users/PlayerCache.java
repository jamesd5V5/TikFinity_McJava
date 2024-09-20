package org.mammothplugins.users;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
public final class PlayerCache extends YamlConfig {

    private static volatile Map<String, PlayerCache> cacheMap = new HashMap<>();
    private static int overallLikes;
    private static int unsavedCaches;

    private final String username;
    private String playerName;
    private int level;

    private boolean isFollowing;
    private int totalLikes;
    private int currentLikes;
    private HashMap<String, Integer> totalGifts;
    private HashMap<String, Integer> currentGifts;

    private final StrictMap<String, Object> tags = new StrictMap<>();

    private PlayerCache(String username) {
        this.username = username;
        this.setPathPrefix("Users." + username);
        this.loadConfiguration(NO_DEFAULT, FoConstants.File.DATA);
    }

    @Override
    protected void onLoad() {
        this.level = getInteger("Level", 0);
        this.playerName = getString("PlayerName", null);
        this.isFollowing = getBoolean("Follows", false);
        this.totalLikes = getInteger("TotalLikes", 0);
        //this.currentLikes = getMap("TotalGifts")

    }

    @Override
    public void onSave() {
        if (totalLikes > 0) {
            this.set("Level", this.level);
            this.set("PlayerName", this.playerName);
            this.set("Follows", this.isFollowing);
            this.set("TotalLikes", this.totalLikes);
            //this.set("TotalGifts", this.totalGifts);
        } else {
            unsavedCaches++;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerCache && ((PlayerCache) obj).getUsername().equals(this.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.username);
    }

    public boolean hasConnectedMinecraftAccount() {
        return playerName == null ? false : true;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Levels getLevels() {
        for (Levels l : Levels.values()) {
            if (l.getLvl() == level)
                return l;
        }
        return Levels.RANK6;
    }

    public int getTotalPointsToUpgrade() {
        return getLevels().getPoints();
    }

    public boolean advanceLvl() {
        return advanceLvl(1);
    }

    public boolean advanceLvl(int levels) {
        if (Rankings.isMaxedLvl(getLevel() + levels)) {
            this.level = Rankings.getMaxLevel();
            return false;
        }
        this.level += levels;

        Common.broadcast("&6&l" + username + " &7just advanced to lvl &6" + level + "&7!");
        for (Player player : Bukkit.getOnlinePlayers())
            CompSound.VILLAGER_YES.play(player.getLocation());
        return true;
    }

    public int getRank() {
        return getLevels().getRank();
    }

    public int getTierOfRank() {
        return getLevels().getTierOfRank();
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public void addCurrentLikes() {
        addCurrentLikes(1);
    }

    public void addCurrentLikes(int numberOfLikes) {
        this.currentLikes += numberOfLikes;
        this.totalLikes += numberOfLikes;
        overallLikes += numberOfLikes;

        if (Rankings.canUpgradeWithLikes(totalLikes, level) == true)
            advanceLvl();
    }

    public void resetCache() {
        this.level = 0;
        this.playerName = null;
        this.isFollowing = false;
        this.totalLikes = 0;
        this.currentLikes = 0;
        this.totalGifts = new HashMap<>();
        this.currentGifts = new HashMap<>();
    }

    /* ------------------------------------------------------------------------------- */
    /* Misc methods */
    /* ------------------------------------------------------------------------------- */

    @Nullable
    public OfflinePlayer getPlayer() {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        return player;
    }

    public void removeFromMemory() {
        synchronized (cacheMap) {
            cacheMap.remove(this.username);
        }
    }

    @Override
    public String toString() {
        return "PlayerCache{" + this.username + "}";
    }

    /* ------------------------------------------------------------------------------- */
    /* Static access */
    /* ------------------------------------------------------------------------------- */

    public static PlayerCache from(String username) {
        synchronized (cacheMap) {
            String sanitizedUsername = sanitizeUsername(username);

            PlayerCache cache = cacheMap.get(sanitizedUsername);

            if (cache == null) {
                cache = new PlayerCache(sanitizedUsername);

                cacheMap.put(sanitizedUsername, cache);
            }

            return cache;
        }
    }

    public static String sanitizeUsername(String username) {
        if (username == null)
            return username;
        else
            return username.replaceAll("§.", "");
    }

    public static Set<String> getUsernames() {
        return cacheMap.keySet();
    }

    public static int getOverallLikes() {
        return overallLikes;
    }

    public static int getUnsavedCaches() {
        return unsavedCaches;
    }

    public static void clearCaches() {
        synchronized (cacheMap) {
            cacheMap.clear();
        }
    }
}
