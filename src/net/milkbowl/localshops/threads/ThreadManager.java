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

package net.milkbowl.localshops.threads;

import java.util.logging.Logger;

import net.milkbowl.localshops.LocalShops;


public class ThreadManager {
    LocalShops plugin = null;
    
    // Threads
    SchedulerThread st = null;
    NotificationThread nt = null;
    ReportThread rt = null;
    
    // Thread Groups
    private ThreadGroup dynamicThreadGroup = new ThreadGroup("dynamic");
    
    // Logging
    private final Logger log = Logger.getLogger("Minecraft");
    
    public ThreadManager(LocalShops plugin) {
        this.plugin = plugin;
    }
    
    public void schedulerStart() {
        if (st == null || !st.isAlive()) {
            st = new SchedulerThread(plugin);
            st.start();
        }
    }
    
    public void schedulerStop() {
        if(st != null && st.isAlive()) {
            try {
                st.setRun(false);
                st.join(2000);
            } catch(InterruptedException e) {
                // ruh roh
                log.warning(String.format("[%s] %s", plugin.getDescription().getName(), "NotificationThread did not exit"));
            }
        }
    }
    
    public void notificationStart() {
        if (nt == null || !nt.isAlive()) {
            nt = new NotificationThread(plugin);
            nt.start();
        }
    }
    
    public void notificationStop() {
        if(nt != null && nt.isAlive()) {
            try {
                nt.setRun(false);
                nt.join(2000);
            } catch(InterruptedException e) {
                // ruh roh
                log.warning(String.format("[%s] %s", plugin.getDescription().getName(), "NotificationThread did not exit"));
            }
        }
    }
    
    public void reportStart() {
        if (rt == null || !rt.isAlive()) {
            rt = new ReportThread(plugin);
            rt.start();
        }
    }
    
    public void reportStop() {
        if(rt != null && rt.isAlive()) {
            try {
                rt.setRun(false);
                rt.join(2000);
            } catch(InterruptedException e) {
                // ruh roh
                log.warning(String.format("[%s] %s", plugin.getDescription().getName(), "ReportThread did not exit"));
            }
        }
    }
    
    public void dynamicStart() {
        if(dynamicThreadGroup.activeCount() == 0) {
            log.info(String.format("[%s] Launching Dynamic Thread", plugin.getDescription().getName()));
            DynamicThread dt = new DynamicThread(dynamicThreadGroup, "dynamic", plugin);
            dt.start();
        } else {
            log.info(String.format("[%s] Dynamic Thread already running", plugin.getDescription().getName()));
        }
    }
    
    public void dynamicStop() {
        // does nothing, there is no need to stop the Dynamic Thread since the thread quits on its own
    }
}
