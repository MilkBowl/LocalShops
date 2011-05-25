/**
 * 
 */
package com.milkbukkit.localshops.listeners;

import java.util.Iterator;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.ShopSign;

/**
 * @author sleaker
 *
 */
public class ShopsWorldListener extends WorldListener {
    private LocalShops plugin;


    public ShopsWorldListener(LocalShops plugin) {
        this.plugin = plugin;
    }
    
    public void onWorldLoad (WorldLoadEvent event) {
        //Loop through all shops
        for (Shop shop : plugin.getShopManager().getAllShops()) {
            //If the event world is different than the shop world skip
            if (shop.getWorld() != event.getWorld().getName())
                continue;
            //Get an iterator from the shops signMap and loop through
            Iterator<String> iter = shop.getSignMap().keySet().iterator();
            while (iter.hasNext()) {
                String signId = iter.next();
                ShopSign sign = shop.getSignMap().get(signId);
                //If event world and signWorld are the same, set the signworld and validate the sign.
                if (sign.getWorld() == null && sign.getWorldName() == event.getWorld().getName())
                {
                    sign.setWorld(event.getWorld());
                    if (sign.isValid()) 
                        shop.updateSign(sign);
                    else
                        iter.remove();
                }
            }
            
        }
        
    }
    
}
