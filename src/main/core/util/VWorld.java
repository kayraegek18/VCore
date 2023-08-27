package net.kayega.core.util;

import org.bukkit.*;

public class VWorld {
    World world;

    public VWorld(String worldName) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        world = wc.createWorld();
        if (world == null)
            return;
        world.setAutoSave(true);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setTime(6100);
    }

    public VWorld(String worldName, World.Environment environment) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(environment);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        world = wc.createWorld();
        if (world == null)
            return;
        world.setAutoSave(true);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setTime(6100);
    }

    public VWorld(String worldName, World.Environment environment, WorldType type) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(environment);
        wc.type(type);
        world = wc.createWorld();
        if (world == null)
            return;
        world.setAutoSave(true);
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setTime(6100);
    }

    public World getWorld() {
        return world;
    }
}
