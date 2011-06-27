/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
 * 
 */

package net.milkbowl.localshops.objects;

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

    public ShopLocation(int x1, int y1, int z1, int x2, int y2, int z2) {
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
            this.z2 = z1;
        } else {
            this.z1 = z1;
            this.z2 = z2;
        }
    }

    public ShopLocation(int[] locationA, int[] locationB) {
        this(locationA[0], locationA[1], locationA[2], locationB[0], locationB[1], locationB[2]);
    }

    public ShopLocation(Location loc1, Location loc2) {
        this(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    //Returns the Location of the point with the lowest x,y,z values
    public int[] getLocation1() {
        return new int[] {x1, y1, z1};
    }

    //Returns the Location of the point with the highest x,y,z values.
    public int[] getLocation2() {
        return new int[] {x2, y2, z2};
    }

    //Returns center point of the shop location
    public Location getCenter(World world) {
        int x = Math.abs((this.x2 - this.x1)/2) + this.x1;
        int y = Math.abs((this.y2 - this.y1)/2) + this.y1;
        int z = Math.abs((this.z2 - this.z1)/2) + this.z1;
        return new Location(world, x, y, z);
    }

    //Returns a Set of all Locations that are of the Material inside the shop
    public Set<Location> findBlocks(Material blockType, World world) {
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

    public boolean contains (Location loc) {
        if (loc.getBlockX() >= x1 && loc.getBlockX() <= x2 && loc.getBlockY() >= y1 && loc.getBlockY() <= y2 && loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2)
            return true;
        else
            return false;
    }

    public boolean equals (ShopLocation shopLoc) {
        int[] xyz1 = shopLoc.getLocation1();
        int[] xyz2 = shopLoc.getLocation2();
        if (xyz1[0] == this.x1 && xyz1[1] == y1 && xyz1[2] == z1 && xyz2[0] == x2 && xyz2[1] == y2 && xyz2[2] == z2)
            return true;
        else
            return false;

    }

    public String toString() {
        return String.format("%d, %d, %d, %d, %d, %d", x1, y1, z1, x2, y2, z2);
    }
}
