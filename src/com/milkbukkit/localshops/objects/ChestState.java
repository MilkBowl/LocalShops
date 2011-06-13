/**
 * 
 */
package com.milkbukkit.localshops.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * @author sleaker
 *
 */
public class ChestState {

    private ContainerBlock block = null;
    private ItemStack[] chestInvBackup = null;
    private ItemStack[] playerInvBackup = null;
    private Location playerLoc = null;
    private boolean active = false;
    
    public void set(Block block, Player player) {
        setBlock(block);
        this.playerLoc = player.getLocation();
        this.active = true;
        playerInvBackup = player.getInventory().getContents();
        chestInvBackup = ((ContainerBlock) block).getInventory().getContents();
    }
    
    public void reset() {
        active = false;
        block = null;
        chestInvBackup = null;
        playerInvBackup = null;
        playerLoc = null;
    }
    
    public Block getBlock() {
        return (Block) block;
    }
    
    public void setBlock(Block block) {
        this.block = (ContainerBlock) block;
    }
    public ItemStack[] getChestInvBackup() {
        return chestInvBackup;
    }
    public void setChestInvBackup(ItemStack[] chestInvBackup) {
        this.chestInvBackup = chestInvBackup;
    }
    public ItemStack[] getPlayerInvBackup() {
        return playerInvBackup;
    }
    public void setPlayerInvBackup(ItemStack[] playerInvBackup) {
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
