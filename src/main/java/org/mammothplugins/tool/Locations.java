package org.mammothplugins.tool;

import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;

public class Locations extends YamlConfig {

    private static final String FOLDER = "locations";
    private Location spawnLocation;

    public Locations(String name) {
        this.setHeader(
                Common.configLine(),
                "This file stores information about a single game.",
                Common.configLine() + "\n");

        this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + name + ".yml");
    }

    @Override
    protected void onLoad() {
        this.spawnLocation = getLocation("Spawn_Location");
    }

    @Override
    protected void onSave() {
        this.set("Spawn_Location", this.spawnLocation);
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        this.save();
    }
}
