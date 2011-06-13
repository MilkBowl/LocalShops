/**
 * 
 */
package net.milkbowl.localshops.listeners;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.Location;
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
            /*
            //Skip checking this block if it's not a sign
            if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST && block.getType() != Material.CHEST) {
                continue;
            }
            */
            //Search for shop at block location
            Shop shop = plugin.getShopManager().getLocalShop(blockLoc);
            //If no shop found at the location skip to the next block
            if (shop == null) {
                continue;
            } else {
                //Just cancel the event if it's happening inside of a shop - don't try to cleanup an explosion event
                event.setCancelled(true);
                return;
               /* Iterator<ShopSign> iter = shop.getSignSet().iterator();
                while (iter.hasNext()) {
                    ShopSign sign = iter.next();
                    if (sign.getLoc().equals(block.getLocation())) {
                        //Remove the object from the map - Only 1 match per loop possible
                        iter.remove();
                        break;
                    }
                } 
                */
            }
        }
    }
}
