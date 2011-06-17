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
import net.milkbowl.localshops.objects.InventoryItem;
import net.milkbowl.localshops.objects.ItemInfo;
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
        Map<ItemInfo, List<Integer>> itemStockMap = Collections.synchronizedMap(new HashMap<ItemInfo, List<Integer>>());

        //Dump all the shop stock data into the map.
        for ( Shop shop : plugin.getShopManager().getAllShops() ) {
            for ( InventoryItem item : shop.getItems() ) {
                if (itemStockMap.containsKey(item.getInfo()))
                    itemStockMap.get(item.getInfo()).add(item.getStock());
                else {
                    List<Integer> intList = new ArrayList<Integer>();
                    intList.add(item.getStock());
                    itemStockMap.put(item.getInfo(), intList);
                }
            }
        }
        for(ItemInfo item : itemStockMap.keySet()) {
            List<Integer> stockList = GenericFunctions.limitOutliers(itemStockMap.get(item));

            //Get the overall stock change for a given item and then calculate the adjustment given the volatility
            int deltaStock = GenericFunctions.getSum(stockList) - Config.getGlobalBaseStock();
            double priceAdj = GenericFunctions.getAdjustment(Config.getGlobalVolatility(), deltaStock);
            DynamicManager.getPriceAdjMap().put(item, priceAdj);
        }

        Bukkit.getServer().getScheduler().callSyncMethod(plugin, plugin.getShopManager().updateSigns());
    }

}
