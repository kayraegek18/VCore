package net.kayega.core.util;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VHologram {
    List<ArmorStand> holograms;
    Location location;
    List<String> lines = new ArrayList<>();

    float lineSpace = 0.02F;

    public VHologram(Location location, List<String> lines) {
        this.location = location;
        this.lines = lines;
        this.holograms = new ArrayList<>();
    }

    public VHologram(Location location, String... lines) {
        this.location = location;
        this.lines.addAll(Arrays.asList(lines));
        this.holograms = new ArrayList<>();
    }

    public void spawn() throws Exception {
        if (location.getWorld() == null) {
            throw new Exception("VHologram: World required!");
        }
        for (int i = 0; i < lines.size(); i++) {
            ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location.add(0,
                    (-lineSpace * i), 0), EntityType.ARMOR_STAND);
            hologram.setVisible(false);
            hologram.setCustomNameVisible(true);
            hologram.setCustomName(lines.get(i));
            hologram.setGravity(false);
            holograms.add(hologram);
        }
    }

    public void remove() {
        for (ArmorStand hologram : holograms) {
            hologram.remove();
        }
        holograms.clear();
    }
}
