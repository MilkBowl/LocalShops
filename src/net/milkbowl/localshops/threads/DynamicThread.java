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

package net.milkbowl.localshops.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.DynamicManager;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.Item;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.Bukkit;


/*
 * Represents the Dynamic Shop Task which is run in a thread
 * 
 * @author sleaker
 * @params plugin
 * 
 */
public class DynamicThread extends Thread {
    private LocalShops plugin = null;

    protected static final Logger log = Logger.getLogger("Minecraft");

    public DynamicThread(ThreadGroup tgroup, String tname, LocalShops plugin) {
        super(tgroup, tname);
        this.plugin = plugin;
    }

    public void run() {
        Map<Item, List<Integer>> itemStockMap = Collections.synchronizedMap(new HashMap<Item, List<Integer>>());

        //Dump all the shop stock data into the map.
        for ( Shop shop : plugin.getShopManager().getAllShops() ) {
            for ( Item item : shop.getItems() ) {
                if (itemStockMap.containsKey(item))
                    itemStockMap.get(item).add(shop.getItem(item).getStock());
                else {
                    List<Integer> intList = new ArrayList<Integer>();
                    intList.add(shop.getItem(item).getStock());
                    itemStockMap.put(item, intList);
                }
            }
        }
        for(Item item : itemStockMap.keySet()) {
            List<Integer> stockList = GenericFunctions.limitOutliers(itemStockMap.get(item));

            //Get the overall stock change for a given item and then calculate the adjustment given the volatility
            int deltaStock = GenericFunctions.getSum(stockList) - Config.getGlobalBaseStock();
            double priceAdj = GenericFunctions.getAdjustment(Config.getGlobalVolatility(), deltaStock);
            DynamicManager.getPriceAdjMap().put(item, priceAdj);
        }

        Bukkit.getServer().getScheduler().callSyncMethod(plugin, plugin.getShopManager().updateSigns());
    }

}
