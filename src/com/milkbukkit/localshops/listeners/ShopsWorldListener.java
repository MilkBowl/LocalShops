/**
 * 
 */
package com.milkbukkit.localshops.listeners;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.ShopSign;
import com.milkbukkit.localshops.objects.GlobalShop;
import com.milkbukkit.localshops.objects.LocalShop;
import com.milkbukkit.localshops.objects.Shop;

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
        for (Shop shop : plugin.getShopManager().getAllLocalShops()) {
            //If the event world is different than the shop world skip
            String worldName = event.getWorld().getName();
            if(shop instanceof GlobalShop) {
                GlobalShop gShop = (GlobalShop) shop;
                if(!gShop.containsWorld(worldName)) {
                    continue;
                }
            } else if(shop instanceof LocalShop) {
                LocalShop lShop = (LocalShop) shop;
                if(!lShop.getWorld().equals(worldName)) {
                    continue;
                }
            }
            
            //Get an iterator from the shops signMap and loop through
            Set<ShopSign> addSet = new HashSet<ShopSign>();
            Iterator<ShopSign> iter = shop.getSignSet().iterator();
            while (iter.hasNext()) {
                ShopSign sign = iter.next();
                //If event world and signWorld are the same, set the signworld and validate the sign.
                if (sign.getWorld() == null && sign.getWorldName() == event.getWorld().getName())
                {
                    sign.setWorld(event.getWorld());
                    if (sign.isValid()) {
                        addSet.add(sign);
                        iter.remove();
                    }
                    else
                        iter.remove();
                }
            }
            shop.updateSigns(addSet);
            shop.getSignSet().addAll(addSet);
            
        }
        
    }
    
}
