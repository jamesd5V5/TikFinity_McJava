package org.mammothplugins.users;

public class Rankings {

    private static Levels[] ranks = Levels.values();

    public static boolean canUpgradeWithLikes(int totalLikes, int currentLvl) {
        if (isMaxedLvl(currentLvl))
            return false;
        Levels nextRank = ranks[currentLvl + 1];
        //Common.broadcast("Current Rank: " + currentLvl + "Next Points: " + nextRank.getPoints());
        if (totalLikes >= nextRank.getPoints())
            return true;
        return false;
    }

    public static boolean canUpgradeWithGifts(int currentLvl) {
        if (isMaxedLvl(currentLvl))
            return false;

        return false;
    }

    public static boolean isMaxedLvl(int currentLvl) {
        //Common.broadcast("Ranks Length: " + ranks.length + " ==== Current: " + currentLvl + " Bool: " + (currentLvl >= ranks.length));
        return currentLvl >= ranks.length - 1 ? true : false;
    }
}
