package com.milkbukkit.localshops.threads;

import java.util.logging.Logger;

import com.milkbukkit.localshops.LocalShops;

public class SchedulerThread extends Thread {
    protected final Logger log = Logger.getLogger("Minecraft");
    private int interval = 5;
    private boolean run = true;
    private static final int TICKS_PER_SECOND = 20;
    
    private LocalShops plugin = null;
    
    public SchedulerThread(LocalShops plugin) {
        this.plugin = plugin;
    }
    
    public void setRun(boolean run) {
        this.run = run;
    }
    
    public void run() {
        // Initial Actions
        // Start Dynamic Thread to base shop values
        plugin.getThreadManager().dynamicStart();
        while(true) {
            if(!run) {
                break;
            }
            
            long worldTime = plugin.getServer().getWorlds().get(0).getTime();
            
            // Launch Dynamic Thread (5pm)
            if(worldTime >= 9000 && worldTime < (9000 + (interval * TICKS_PER_SECOND))) {
                plugin.getThreadManager().dynamicStart();
            }
         
            for(int i = 0; i < interval; i++) {
                if(!run) {
                    break;
                }
                
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    // care
                }
            }
            
        }
        
        log.info(String.format("[%s] SchedulerThread exited safely.", plugin.getDescription().getName()));
    }
    
}
