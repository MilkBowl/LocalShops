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
        if (st != null && st.isAlive()) {
            try {
                st.setRun(false);
                st.join(2000);
            } catch (InterruptedException e) {
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
        if (nt != null && nt.isAlive()) {
            try {
                nt.setRun(false);
                nt.join(2000);
            } catch (InterruptedException e) {
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
        if (rt != null && rt.isAlive()) {
            try {
                rt.setRun(false);
                rt.join(2000);
            } catch (InterruptedException e) {
                // ruh roh
                log.warning(String.format("[%s] %s", plugin.getDescription().getName(), "ReportThread did not exit"));
            }
        }
    }

    public void dynamicStart() {
        if (dynamicThreadGroup.activeCount() == 0) {
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
