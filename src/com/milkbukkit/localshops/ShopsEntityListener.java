/**
 * 
 */
package com.milkbukkit.localshops;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

/**
 * @author sleaker
 *
 */
public class ShopsEntityListener extends EntityListener {
    private LocalShops plugin;


    public ShopsEntityListener(LocalShops plugin) {
        this.plugin = plugin;
    }


    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        //Check the blocks to see if we need to remove a sign from the shopdata
        for (Block block : event.blockList()) {
            Location blockLoc = block.getLocation();
            //Skip checking this block if it's not a sign
            if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST ) {
                continue;
            }

            //Search for shop at block location
            Shop shop = plugin.getShopManager().getShop(blockLoc);
            //If no shop found at the location skip to the next block
            if (shop == null) {
                continue;
            } else {
                Iterator<Location> iter = shop.getSignMap().keySet().iterator();
                while (iter.hasNext()) {
                    Location signLoc = iter.next();
                    if ( signLoc.getBlockX() == blockLoc.getBlockX() && signLoc.getBlockY() == blockLoc.getBlockY() && signLoc.getBlockZ() == blockLoc.getBlockZ()) {
                        //Remove the object from the map and Only 1 match possible per block so Break loop if we found one.
                        iter.remove();
                        break;
                    }
                }
            }
        }
    }
}
