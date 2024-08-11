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
        this.set("Level", this.level);
        this.set("PlayerName", this.playerName);
        this.set("Follows", this.isFollowing);
        this.set("TotalLikes", this.totalLikes);
        //this.set("TotalGifts", this.totalGifts);
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
        if (Rankings.isMaxedLvl(getLevel()))
            return false;
        this.level++;

        Common.broadcast("&6&l" + username + " just advanced to lvl " + level + "!");
        for (Player player : Bukkit.getOnlinePlayers())
            CompSound.VILLAGER_YES.play(player.getLocation());
        return true;
    }

    public void setLevel(int level) {
        this.level = level;
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
        this.currentLikes++;
        this.totalLikes++;

        if (Rankings.canUpgradeWithLikes(totalLikes, level) == true)
            advanceLvl();
    }

    public void resetCurrentLikes() {
        this.currentLikes = 0;
    }

    public void resetCurrentGifts() {
        this.currentGifts.clear();
    }

    //Testing Methods
    public void resetLvl() {
        this.level = 0;
    }

    public void resetTotalLikes() {
        this.currentLikes = 0;
        this.totalLikes = 0;
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
            final String playerName = username;

            PlayerCache cache = cacheMap.get(username);

            if (cache == null) {
                cache = new PlayerCache(playerName);

                cacheMap.put(playerName, cache);
            }

            return cache;
        }
    }

    public static Set<String> getUsernames() {
        return cacheMap.keySet();
    }

    public static void clearCaches() {
        synchronized (cacheMap) {
            cacheMap.clear();
        }
    }
}
