package org.mammothplugins.users;

import org.mineacademy.fo.Common;

public class Rankings {

    private static Ranks[] ranks = Ranks.values();

    public static boolean canUpgradeWithLikes(int totalLikes, int currentRank) {
        if (isMaxedRank(currentRank))
            return false;
        Ranks nextRank = ranks[currentRank + 1];
        //Common.broadcast("Current Rank: " + currentRank + "Next Points: " + nextRank.getPoints());
        if (totalLikes >= nextRank.getPoints())
            return true;
        return false;
    }

    public static boolean canUpgradeWithGifts(int currentRank) {
        if (isMaxedRank(currentRank))
            return false;

        return false;
    }

    public static boolean isMaxedRank(int currentRank) {
        //Common.broadcast("Ranks Length: " + ranks.length + " ==== Current: " + currentRank + " Bool: " + (currentRank >= ranks.length));
        return currentRank >= ranks.length - 1 ? true : false;
    }
}
