package org.mammothplugins.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mammothplugins.events.EventZombie;
import org.mammothplugins.users.FetchPlayer;
import org.mammothplugins.users.PlayerCache;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class UserCommands extends SimpleCommand {
    public UserCommands() {
        super("user");
    }

    @Override
    protected void onCommand() {
        // /user clovercraze [like,follow,mcAccount,gift]
        String tkUsername = args[0];
        String interaction = args[1];

        PlayerCache playerCache = PlayerCache.from(tkUsername);

        if (this.args.length == 2) {
            if ("like".equals(interaction)) {
                playerCache.addCurrentLikes();

                //Zombies When Liked
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EventZombie event = new EventZombie(player, tkUsername);
                    event.runEvent();
                }

            } else if ("follow".equals(interaction)) {
                playerCache.setIsFollowing(true);
                playerCache.addCurrentLikes(50);
            }
        } else if (this.args.length == 4) {
            if ("mcAccount".equals(interaction)) {
                String mc = args[2];
                if (!mc.startsWith("mc"))
                    return;
                String mcAccount = args[3];
                if (FetchPlayer.doesPlayerExist(mcAccount)) {
                    playerCache.setPlayerName(mcAccount);
                    Common.broadcast("User has connect to the mc account " + mcAccount);
                } else
                    Common.broadcast("&cMc Account " + mcAccount + " does not exist.");

            } else if ("gift".equals(interaction)) {

            }
        }
    }

    @Override
    protected List<String> tabComplete() {
        List<String> list1 = new ArrayList<>();
        list1.add("(tkUsername)");

        List<String> list2 = new ArrayList<>();
        list2.add("like");
        list2.add("follow");
        list2.add("mcAccount");
        list2.add("gift");

        List<String> list3 = new ArrayList<>();
        list3.add("(mcAccount)");

        List<String> list4 = new ArrayList<>();
        list3.add("(giftType)");

        List<String> next = this.args.length == 1 ? list1 : list2;
        List<String> third = this.args[0].equals("mcAccount") ? list3 : list4;
        return this.args.length > 2 ? third : next;
    }
}
