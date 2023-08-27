package net.kayega.core.border;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.ArrayList;

public class VBorderApi {
    private ArrayList<VBorder> borders = new ArrayList<VBorder>();
    private static VBorderApi instance;
    public VBorderApi(){
        instance = this;
    }

    public static VBorderApi getAPI(){
        return instance;
    }

    public VBorder createBorder(World world, Location center, double radius){
        VBorder border = new VBorder(world, center, radius);
        borders.add(border);
        return border;
    }

    public VBorder createBorder(World world, WorldBorder worldBorder){
        VBorder border = new VBorder(world, worldBorder);
        borders.add(border);
        return border;
    }

    public VBorder createBorder(World world, double centerX, double centerZ, double radius){
        VBorder border = new VBorder(world, centerX, centerZ, radius);
        borders.add(border);
        return border;
    }

    public VBorder getBorder(World world){
        if(borders.isEmpty())return null;
        for(VBorder b : borders){
            if(b.getWorld().getName().equals(world.getName()))return b;
        }
        return null;
    }
}
