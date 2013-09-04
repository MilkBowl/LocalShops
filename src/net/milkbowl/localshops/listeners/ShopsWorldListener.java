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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopSign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;


public class ShopsWorldListener implements Listener {

    private LocalShops plugin;

    public ShopsWorldListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        //Loop through all shops
        for (Shop shop : plugin.getShopManager().getAllShops()) {
            //If the event world is different than the shop world skip
            String worldName = event.getWorld().getName();
            //ignore global shops
            if (shop instanceof GlobalShop) {
                continue;
            } else if (shop instanceof LocalShop) {
                if (!((LocalShop) shop).getWorld().equals(worldName)) {
                    continue;
                }
            }

            //Get an iterator from the shops signMap and loop through
            Set<ShopSign> addSet = new HashSet<ShopSign>();
            Iterator<ShopSign> iter = shop.getSigns().iterator();
            while (iter.hasNext()) {
                ShopSign sign = iter.next();
                //If event world and signWorld are the same, set the signworld and validate the sign.
                if (sign.getWorld() == null && sign.getWorldName().equals(event.getWorld().getName())) {
                    sign.setWorld(event.getWorld());
                    if (sign.isValid()) {
                        addSet.add(sign);
                        iter.remove();
                    } else {
                        iter.remove();
                    }
                }
            }
            plugin.getShopManager().updateSigns(shop, addSet);
            shop.getSigns().addAll(addSet);

        }

    }
}
