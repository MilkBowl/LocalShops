package com.milkbukkit.localshops.threads;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.DynamicManager;
import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.util.GenericFunctions;

/*
 * Represents the Dynamic Shop Task which is run in a thread
 * 
 * @author sleaker
 * @params plugin
 * 
 */
public class DynamicThread extends Thread {
    private LocalShops plugin = null;


    public DynamicThread(ThreadGroup tgroup, String tname, LocalShops plugin) {
        super(tgroup, tname);
        this.plugin = plugin;
    }

    public void run() {
        Map<ItemInfo, List<Integer>> itemStockMap = new HashMap<ItemInfo, List<Integer>>();
        
        //Dump all the shop stock data into the map.
        for ( Shop shop : plugin.getShopManager().getAllShops() ) {
            for ( InventoryItem item : shop.getItems() ) {
                if (itemStockMap.containsKey(item.getInfo()))
                    itemStockMap.get(item.getInfo()).add(item.getStock());
                else
                    itemStockMap.put(item.getInfo(), Arrays.asList(item.getStock()));     
            }
        }
        for(ItemInfo item : itemStockMap.keySet()) {
            List<Integer> stockList = GenericFunctions.limitOutliers(itemStockMap.get(item));
            //remove the map before re-adding it
            if (DynamicManager.getPriceAdjMap().containsKey(item)) 
                DynamicManager.getPriceAdjMap().remove(item);
            
            //Get the overall stock change for a given item and then calculate the adjustment given the volatility
            int deltaStock = GenericFunctions.getSum(stockList) - Config.getGlobalBaseStock();
            DynamicManager.getPriceAdjMap().put(item, GenericFunctions.getAdjustment(Config.getGlobalVolatility(), deltaStock)); 
        }
           
    }

}
