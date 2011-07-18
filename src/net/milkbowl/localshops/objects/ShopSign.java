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

import net.milkbowl.localshops.exceptions.TypeNotFoundException;

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
    private int amount;
    private SignType type = null;
    public static enum SignType {
        INFO(0),
        BUY(1),
        SELL(2);
        
        private final int id;
        
        SignType(int id) {
            this.id = id;
        }
        
        public int getId () {
            return id;
        }
    }

    public ShopSign (World world, int x, int y, int z, String itemName, int typeId, int amount) throws TypeNotFoundException {
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        this.world = world;
        this.amount = amount;
        this.signWorld = world.getName();
        for(SignType t : SignType.values()) {
            if(t.getId() == typeId) {
                this.type = t;
                break;
            }
        }
        if(type == null) {
            // ruh roh!
            throw new TypeNotFoundException(String.format("Sign type %d not found.", typeId));
        }
    }
    
    
    public ShopSign(World world, int x, int y, int z, String itemName, int typeId) throws TypeNotFoundException {
    	this (world, x, y, z, itemName, typeId, 1);
    }
    
    public ShopSign (World world, int x, int y, int z, String itemName) throws TypeNotFoundException {
        this(world, x, y, z, itemName, SignType.INFO.getId(), 1);
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

    public ShopSign (Block block, String itemName) throws TypeNotFoundException {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName, 1);
    }
    
    public ShopSign (Block block, String itemName, int typeId) throws TypeNotFoundException {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName, typeId, 1);
    }
    
    public ShopSign (Location loc, String itemName) throws TypeNotFoundException {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), itemName, 0, 1);
    }
    
    public ShopSign(String signWorld, int x, int y, int z, String itemName, int typeId, int amount) throws TypeNotFoundException {
        this.signWorld = signWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        for(SignType t : SignType.values()) {
            if(t.getId() == typeId) {
                this.type = t;
                break;
            }
        }
        if(type == null) {
            // ruh roh!
            throw new TypeNotFoundException(String.format("Sign type %d not found.", typeId));
        }
    }
    
    public ShopSign(String signWorld, int x, int y, int z, String itemName, int typeId) throws TypeNotFoundException {
    	this(signWorld, x, y, z, itemName, typeId, 1);
    }

    
    public ShopSign(String signWorld, int x, int y, int z, String itemName) throws TypeNotFoundException {
        this(signWorld, x, y, z, itemName, SignType.INFO.getId(), 1);
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
        string.append(type.getId());
        string.append(",");
        string.append(amount);
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
    
    public int getAmount() {
		return amount;
	}


	public boolean equals (ShopSign sign) {
        if (this.getWorldName().equals(sign.getWorldName()) && this.x == sign.x && this.y == sign.y && this.z == sign.z && this.item.equals(sign.item)) 
            return true;
        
        return false;
    }
}
