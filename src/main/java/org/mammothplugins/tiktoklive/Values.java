package org.mammothplugins.tiktoklive;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

public class Values extends YamlConfig {

    private static final String FOLDER = "values";

    @Getter
    private int minLikesToInteract;

    public Values(String name) {
        this.setHeader(
                Common.configLine(),
                "Tiktok LiveStream Values.",
                Common.configLine() + "\n");
        this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + name + ".yml");
    }

    @Override
    protected void onLoad() {
        this.minLikesToInteract = getInteger("Min_Likes_To_Interact", 10);
    }

    @Override
    protected void onSave() {
        this.set("Min_Likes_To_Interact", this.minLikesToInteract);
    }


    public void setMinLikesToInteract(int minLikesToInteract) {
        this.minLikesToInteract = minLikesToInteract;
        this.save();
    }
}
