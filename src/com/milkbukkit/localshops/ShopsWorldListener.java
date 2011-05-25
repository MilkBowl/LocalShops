/**
 * 
 */
package com.milkbukkit.localshops;

import java.util.Iterator;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

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
        for (Shop shop : plugin.getShopManager().getAllShops()) {
            if (shop.getWorld() != event.getWorld().getName())
                continue;
            Iterator<String> iter = shop.getSignMap().keySet().iterator();
            while (iter.hasNext()) {
                String signId = iter.next();
                ShopSign sign = shop.getSignMap().get(signId);
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
