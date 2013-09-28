/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package net.milkbowl.localshops.listeners;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ShopsEntityListener implements Listener {

    private LocalShops plugin;

    public ShopsEntityListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
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
