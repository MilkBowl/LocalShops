/**
 * 
 */
package com.milkbukkit.localshops;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author sleaker
 *
 */
public class ShopSign {
    private int x;
    private int y;
    private int z;
    private World world = null;
    private String signWorld = null;
    private String item;
    private SignTypes type = SignTypes.INFO;
    private static enum SignTypes {
        INFO(0),
        BUY(1),
        SELL(2);
        
        int id = -1;
        
        SignTypes(int id) {
            this.id = id;
        }
        
        @SuppressWarnings("unused")
        public int getId () {
            return id;
        }
        
    }

    public ShopSign (World world, int x, int y, int z, String itemName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        this.world = world;
        this.signWorld = world.getName();

    }

    public ShopSign (World world, String string) {
        String[] split = string.split(",");
        this.x = Integer.parseInt(split[0]);
        this.y = Integer.parseInt(split[1]);
        this.z = Integer.parseInt(split[2]);
        this.item = split[3];
        this.world = world;
        this.signWorld = world.getName();
    }

    public ShopSign (Block block, String itemName) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName);
    }

    public ShopSign (Location loc, String itemName) {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), itemName);
    }

    public ShopSign(String signWorld, int x, int y, int z, String itemName) {
        this.signWorld = signWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(signWorld);
        string.append(":");
        string.append(x);
        string.append(",");
        string.append(y);
        string.append(",");
        string.append(z);
        string.append(",");
        string.append(item);
        return string.toString();
    }

    public static String hashString(String worldName, int x, int y, int z) {
        StringBuilder string = new StringBuilder();
        string.append(worldName.hashCode());
        string.append(x);
        string.append(y);
        string.append(z);

        return string.toString();
    }

    public static String hashString(Location loc) {
        return hashString(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public String hashString() {
        return hashString(this.signWorld, this.x, this.y, this.z);
    }

    public boolean isValid() {
        if (world == null) 
            return true;
        
        world.loadChunk(world.getChunkAt(getLoc()));
        if (world.getBlockAt(getLoc()).getType() == Material.WALL_SIGN || world.getBlockAt(getLoc()).getType() == Material.SIGN_POST )
            return true;
        else
            return false;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public void setWorld(World world) {
        this.world = world;
    }
    
    public Location getLoc() {
        if (world == null) 
            return null;
        else
            return new Location(this.world, this.x, this.y, this.z);
    }
    
    public String getItemName() {
        return this.item;
    }
    
    public String getWorldName() {
        return this.signWorld;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public SignTypes getType() {
        return type;
    }
    
    public boolean equals (ShopSign sign) {
        if (this.hashString() == sign.hashString())
            return true;
        
        return false;
    }
}
