package com.milkbukkit.localshops.objects;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class ShopLocation {
    private int x1 = 0;
    private int y1 = 0;
    private int z1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private int z2 = 0;
    private World world;

    public ShopLocation(int x1, int y1, int z1, int x2, int y2, int z2, World world) {
        //second point should always be set to the larger number
        if (x1 > x2) {
            this.x1 = x2;
            this.x2 = x1;
        } else {
            this.x1 = x1;
            this.x2 = x2;
        }
        if (y1 > y2) {
            this.y1 = y2;
            this.y2 = y1;
        } else {
            this.y1 = y1;
            this.y2 = y2;
        }
        if ( z1 > z2) {
            this.z1 = z2;
            this.z2 = z2;
        } else {
            this.z1 = z1;
            this.z2 = z2;
        }
        this.world = world;
    }

    public ShopLocation(int[] locationA, int[] locationB, World world) {
        this(locationA[0], locationA[1], locationA[2], locationB[0], locationB[1], locationB[2], world);
    }
    
    public ShopLocation(Location loc1, Location loc2) {
        this(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ(), loc1.getWorld());
    }

    //Returns the Location of the point with the lowest x,y,z values
    public Location getLocation1() {
        return new Location(world, x1, y1, z1);
    }
    
    //Returns the Location of the point with the highest x,y,z values.
    public Location getLocation2() {
        return new Location(world, x2, y2, z2);
    }

    //Returns center point of the shop location
    public Location getCenter() {
        int x = Math.abs((this.x2 - this.x1)/2) + this.x1;
        int y = Math.abs((this.y2 - this.y1)/2) + this.y1;
        int z = Math.abs((this.z2 - this.z1)/2) + this.z1;
        return new Location(world, x, y, z);
    }
    
    public World getWorld() {
        return world;
    }

    //Returns a Set of all Locations that are of the Material inside the shop
    public Set<Location> findBlocks(Material blockType) {
        Set<Location> foundBlocks = new HashSet<Location>(1);
            for (int i = this.x1; i >= this.x1 && i <= this.x2; i++) {
                for (int j = this.y1; j >= this.y1 && j <= this.y2; j++) {
                    for (int k = this.z1; k >= this.z1 && k <= this.z2; k++) {
                        Location loc = new Location(world, i, j, k);
                        if (!loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc)))
                                loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
                        if (loc.getBlock().getType().equals(blockType))
                            foundBlocks.add(loc);
                    }
                }
            }
        return foundBlocks;
    }
    
    //TODO: Fix String output
    public String toString() {
        return String.format("on %s at %d %d %d X %d %d %d", world.getName(), x1, y1, z1, x2, y2, z2);
    }
}
