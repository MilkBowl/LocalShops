/**
 * 
 */
package net.milkbowl.localshops.listeners;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopSign;

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
        //Loop through all shops
        for (Shop shop : plugin.getShopManager().getAllShops()) {
            //If the event world is different than the shop world skip
            String worldName = event.getWorld().getName();
            //ignore global shops
            if(shop instanceof GlobalShop) {
                    continue;
            } else if(shop instanceof LocalShop) {
                if(!shop.containsWorld(worldName)) {
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
