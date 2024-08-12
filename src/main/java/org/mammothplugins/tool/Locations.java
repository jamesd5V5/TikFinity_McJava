package org.mammothplugins.tool;

import org.bukkit.Location;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Locations extends YamlConfig {

    private static final String FOLDER = "locations";

    private Location bossLocation;
    private Location mobLocation1;
    private Location mobLocation2;
    private Location mobLocation3;
    private Location playerLocation;

    public Locations(String name) {
        this.setHeader(
                Common.configLine(),
                "This file in-game locations.",
                Common.configLine() + "\n");
        this.loadConfiguration(NO_DEFAULT, FOLDER + "/" + name + ".yml");
    }

    @Override
    protected void onLoad() {
        this.bossLocation = getLocation("Boss_Location");
        this.mobLocation1 = getLocation("Mob_Location_1");
        this.mobLocation2 = getLocation("Mob_Location_2");
        this.mobLocation3 = getLocation("Mob_Location_3");
        this.playerLocation = getLocation("Player_Location");
    }

    @Override
    protected void onSave() {
        this.set("Boss_Location", this.bossLocation);
        this.set("Mob_Location_1", this.mobLocation1);
        this.set("Mob_Location_2", this.mobLocation2);
        this.set("Mob_Location_3", this.mobLocation3);
        this.set("Player_Location", this.playerLocation);
    }

    private static Random random = new Random();

    public Location pickRandomMobLocation() {

        int ran = random.nextInt(3);
        switch (ran) {
            case 0:
                return getMobLocation1();
            case 1:
                return getMobLocation2();
            case 2:
                return getMobLocation3();
        }
        return getMobLocation1();
    }

    public Location getBossLocation() {
        return bossLocation;
    }

    public Location getMobLocation1() {
        return mobLocation1;
    }

    public Location getMobLocation2() {
        return mobLocation2;
    }

    public Location getMobLocation3() {
        return mobLocation3;
    }

    public Location getPlayerLocation() {
        return playerLocation;
    }

    public void setBossLocation(Location bossLocation) {
        this.bossLocation = bossLocation;
        this.save();
    }

    public void setMobLocation1(Location mobLocation1) {
        this.mobLocation1 = mobLocation1;
        this.save();
    }

    public void setMobLocation2(Location mobLocation2) {
        this.mobLocation2 = mobLocation2;
        this.save();
    }

    public void setMobLocation3(Location mobLocation3) {
        this.mobLocation3 = mobLocation3;
        this.save();
    }

    public void setPlayerLocation(Location playerLocation) {
        this.playerLocation = playerLocation;
        this.save();
    }
}
