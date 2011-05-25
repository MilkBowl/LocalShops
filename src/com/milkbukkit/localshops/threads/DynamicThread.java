package com.milkbukkit.localshops.threads;

import com.milkbukkit.localshops.LocalShops;

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
