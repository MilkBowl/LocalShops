/**
 * 
 */
package com.milkbukkit.localshops.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;


/**
 * @author sleaker
 *
 */
public class ChestState {

    private Block block = null;
    private ArrayList<ItemStack[]> chestInvBackup = null;
    private ArrayList<ItemStack[]> playerInvBackup = null;
    private Location playerLoc = null;
    private boolean active = false;
    
    ChestState() {
    }
    
    public Block getBlock() {
        return block;
    }
    public void setBlock(Block block) {
        this.block = block;
    }
    public ArrayList<ItemStack[]> getChestInvBackup() {
        return chestInvBackup;
    }
    public void setChestInvBackup(ArrayList<ItemStack[]> chestInvBackup) {
        this.chestInvBackup = chestInvBackup;
    }
    public ArrayList<ItemStack[]> getPlayerInvBackup() {
        return playerInvBackup;
    }
    public void setPlayerInvBackup(ArrayList<ItemStack[]> playerInvBackup) {
        this.playerInvBackup = playerInvBackup;
    }
    public Location getPlayerLoc() {
        return playerLoc;
    }
    public void setPlayerLoc(Location playerLoc) {
        this.playerLoc = playerLoc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
