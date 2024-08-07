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
    private int rank;

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
        this.rank = getInteger("Rank", 0);
        this.isFollowing = getBoolean("Follows", false);
        this.totalLikes = getInteger("TotalLikes", 0);
        //this.currentLikes = getMap("TotalGifts")

    }

    @Override
    public void onSave() {
        this.set("Rank", this.rank);
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

    public Ranks getRanks() {
        for (Ranks r : Ranks.values()) {
            if (r.getRank() == rank)
                return r;
        }
        return null;
    }

    public int getTotalPointsToUpgrade() {
        return getRanks().getPoints();
    }

    public void advanceRank() {
        this.rank++;

        Common.broadcast("&6&l" + username + " just advanced to rank " + rank + "!");
        for (Player player : Bukkit.getOnlinePlayers())
            CompSound.VILLAGER_YES.play(player.getLocation());
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public void addCurrentLikes() {
        this.currentLikes++;
        this.totalLikes++;

        if (Rankings.canUpgradeWithLikes(totalLikes, rank) == true)
            advanceRank();
    }

    public void resetCurrentLikes() {
        this.currentLikes = 0;
    }

    public void resetCurrentGifts() {
        this.currentGifts.clear();
    }

    //Testing Methods
    public void resetRank() {
        this.rank = 0;
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
