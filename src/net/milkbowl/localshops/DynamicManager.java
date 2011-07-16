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

package net.milkbowl.localshops;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.milkbowl.localshops.objects.Item;


/**
 * @author sleaker
 *
 */
public class DynamicManager {
    @SuppressWarnings("unused")
    private LocalShops plugin = null;
    private static Map<Item, Double> priceAdjMap = Collections.synchronizedMap(new HashMap<Item, Double>());
        
    public DynamicManager(LocalShops plugin) {
        this.plugin = plugin;
    }

    public static Map<Item, Double> getPriceAdjMap() {
        return priceAdjMap;
    } 

}
