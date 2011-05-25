package com.milkbukkit.localshops.threads;

import java.util.Random;
import java.util.logging.Logger;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.DynamicManager;
import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;

/*
 * Represents the Dynamic Shop Task which is run in a thread
 * 
 * @author sleaker
 * @params plugin
 * 
 */
public class DynamicThread extends Thread {
    private LocalShops plugin = null;


    public DynamicThread(LocalShops plugin) {
        this.plugin = plugin;
    }


    public void run() {
        plugin.getDynamicManager().updateMap();
           
    }

}
