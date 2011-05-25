/**
 * 
 */
package com.milkbukkit.localshops;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sleaker
 *
 */
public class DynamicManager {
    private LocalShops plugin = null;
    private static Map<ItemInfo, Double> priceAdjMap = Collections.synchronizedMap(new HashMap<ItemInfo, Double>());
        
    public DynamicManager(LocalShops plugin) {
        this.plugin = plugin;
    }

    public static Map<ItemInfo, Double> getPriceAdjMap() {
        return priceAdjMap;
    }
    
    public void updateMap() {
        for (ItemInfo item : priceAdjMap.keySet()) {
            for (Shop shop : plugin.getShopManager().getAllShops() ) {
                if (shop.containsItem(item)) {
                  //TODO: run Util code to update the item price adjustments
                }
            }
        }
    }
    

}
