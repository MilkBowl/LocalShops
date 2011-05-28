/**
 * 
 */
package com.milkbukkit.localshops;

import java.util.HashMap;
import java.util.Map;

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
    private SignType type = SignType.INFO;
    public static enum SignType {
        INFO(0),
        BUY(1),
        SELL(2);
        
        private final int id;
        private static final Map<Integer, SignType> lookupType = new HashMap<Integer, SignType>(3);
        private static final Map<SignType, Integer> lookupId = new HashMap<SignType, Integer>(3);
        
        SignType(int id) {
            this.id = id;
        }
        
        public int getId () {
            return id;
        }
        
        public static int getSignId(SignType type) {
            return lookupId.get(type);
        }
        
        public static SignType getSignType(final int id) {
            return lookupType.get(id);
        }
        
        static {
            for (SignType signType : values()) {
                lookupType.put(signType.getId(), signType);
                lookupId.put(signType, signType.getId());
            }
        }
    }

    public ShopSign(World world, int x, int y, int z, String itemName, int typeId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        this.world = world;
        this.signWorld = world.getName();
        this.type = SignType.getSignType(typeId);
    }
    
    public ShopSign (World world, int x, int y, int z, String itemName) {
        this(world, x, y, z, itemName, 0);
    }

    public ShopSign (World world, String string) {
        String[] split = string.split(",");
        this.x = Integer.parseInt(split[0]);
        this.y = Integer.parseInt(split[1]);
        this.z = Integer.parseInt(split[2]);
        this.item = split[3];
        this.world = world;
        this.signWorld = world.getName();
        this.type = SignType.INFO;
    }

    public ShopSign (Block block, String itemName) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName);
    }
    
    public ShopSign (Block block, String itemName, int typeId) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName, typeId);
    }
    
    public ShopSign (Location loc, String itemName) {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), itemName, 0);
    }
    
    public ShopSign(String signWorld, int x, int y, int z, String itemName, int typeId) {
        this.signWorld = signWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        this.type = SignType.getSignType(typeId);
    }

    
    public ShopSign(String signWorld, int x, int y, int z, String itemName) {
        this(signWorld, x, y, z, itemName, 0);
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
        string.append(",");
        string.append(SignType.getSignId(type));
        return string.toString();
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
    
    public SignType getType() {
        return type;
    }
    
    public boolean equals (ShopSign sign) {
        if (this.getWorldName().equals(sign.getWorldName()) && this.x == sign.x && this.y == sign.y && this.z == sign.z && this.item.equals(sign.item)) 
            return true;
        
        return false;
    }
}
