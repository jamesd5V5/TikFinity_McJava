package org.mammothplugins.users;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;

public enum Levels {

    RANK0(),
    RANK1_TIER1(), RANK1_TIER2(), RANK1_TIER3(), RANK1_TIER4(),
    RANK2_TIER1(), RANK2_TIER2(), RANK2_TIER3(), RANK2_TIER4(),
    RANK3_TIER1(), RANK3_TIER2(), RANK3_TIER3(), RANK3_TIER4(),
    RANK4_TIER1(), RANK4_TIER2(), RANK4_TIER3(), RANK4_TIER4(),
    RANK5_TIER1(), RANK5_TIER2(), RANK5_TIER3(), RANK5_TIER4(),
    RANK6;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public int getLvl() {
        return this.ordinal();
    }

    public int getTierOfRank() {
        //4 Tiers in Each Rank
        if (getLvl() == 0)
            return 0;
        int mod = getLvl() % 4;
        if (mod == 0)
            return 4;
        else
            return mod;
    }

    //Ranges from 0 to 247. 2,430 pts total
    public int getPoints() {
        //Please Change in TikTokLive as well to mimic levels
        if (getLvl() == 0)
            return 0;
        else if (getLvl() == 1)
            return 10;
        else {
            return (int) (getLvl() * 1.02);
        }
    }

    public ChatColor getChatColor() {
        switch (getRank()) {
            case 0:
                return ChatColor.GREEN;
            case 1:
                return ChatColor.GREEN;
            case 2:
                return ChatColor.GOLD;
            case 3:
                return ChatColor.WHITE;
            case 4:
                return ChatColor.AQUA;
            case 5:
                return ChatColor.DARK_GREEN;
            case 6:
                return ChatColor.BOLD;
        }
        return ChatColor.GRAY;
    }

    public int getRank() {
        if (getLvl() == 0)
            return 0;
        if (getLvl() <= 4)
            return 1;
        else if (getLvl() <= 8) {
            return 2;
        } else if (getLvl() <= 12) {
            return 3;
        } else if (getLvl() <= 16) {
            return 4;
        } else if (getLvl() <= 20) {
            return 5;
        } else if (getLvl() > 20) {
            return 6;
        } else {
            return 0;
        }
    }
}
