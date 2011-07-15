/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
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

        //Check see if explosion happens inside a shop - if it does, cancel the event.
        for (Block block : event.blockList()) {
            Location blockLoc = block.getLocation();
            
            //Search for shop at block location
            Shop shop = plugin.getShopManager().getLocalShop(blockLoc);
            //If no shop found at the location skip to the next block
            if (shop == null) {
                continue;
            } else {
                //Just cancel the event if it's happening inside of a shop - don't try to cleanup an explosion event
                event.setCancelled(true);
                return;
            }
        }
    }
}
