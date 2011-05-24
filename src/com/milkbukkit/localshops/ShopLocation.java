package com.milkbukkit.localshops;

import org.bukkit.Location;

public class ShopLocation {
    private int x = 0;
    private int y = 0;
    private int z = 0;

    public ShopLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ShopLocation(int[] locationB) {
        this.x = locationB[0];
        this.y = locationB[1];
        this.z = locationB[2];
    }
    
    public ShopLocation(Location loc) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int[] toArray() {
        return new int[] { x, y, z };
    }
    
    public double[] toDoubleArray() {
        return new double[] { x, y, z };
    }

    public String toString() {
        return String.format("%d, %d, %d", x, y, z);
    }
}
