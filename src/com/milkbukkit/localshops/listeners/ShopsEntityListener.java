/**
 * 
 */
package com.milkbukkit.localshops.listeners;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.objects.Shop;
import com.milkbukkit.localshops.objects.ShopSign;

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
            Shop shop = plugin.getShopManager().getLocalShop(blockLoc);
            //If no shop found at the location skip to the next block
            if (shop == null) {
                continue;
            } else {
                Iterator<ShopSign> iter = shop.getSignSet().iterator();
                while (iter.hasNext()) {
                    ShopSign sign = iter.next();
                    if (sign.getLoc().equals(block.getLocation())) {
                        //Remove the object from the map - Only 1 match per loop possible
                        iter.remove();
                        break;
                    }
                } 
            }
        }
    }
}
