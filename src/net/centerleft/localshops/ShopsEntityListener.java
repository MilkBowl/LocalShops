/**
 * 
 */
package net.centerleft.localshops;

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
        Shop shop = null;
        if (event.isCancelled()) {
            return;
        }
        //Check the blocks to see if we need to remove a sign from the shopdata
        for (Block block : event.blockList()) {
            //Skip checking this block if it's not a sign
            if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST ) {
                continue;
            }
            
            //Search for Shops near event block and add them to the array list.
            shop = plugin.getShopManager().getShop(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());

            if (shop == null) {
                return;
            }
            
            Location blockLoc = block.getLocation();
            Iterator<Location> iter = shop.getSignMap().keySet().iterator();
            while (iter.hasNext()) {
                Location signLoc = iter.next();
                if ( signLoc.getBlockX() == blockLoc.getBlockX() && signLoc.getBlockY() == blockLoc.getBlockY() && signLoc.getBlockZ() == blockLoc.getBlockZ()) {
                    iter.remove();
                    //Only 1 match possible per block - Break loop if we found one.
                    break;
                }
            }
        }
    }
    
}
