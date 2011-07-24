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
        while (true) {
            if (!run) {
                break;
            }

            long worldTime = plugin.getServer().getWorlds().get(0).getTime();

            // Launch Dynamic Thread (5pm)
            if (worldTime >= 9000 && worldTime < (9000 + (interval * TICKS_PER_SECOND))) {
                plugin.getThreadManager().dynamicStart();
            }

            for (int i = 0; i < interval; i++) {
                if (!run) {
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // care
                }
            }

        }

        log.info(String.format("[%s] SchedulerThread exited safely.", plugin.getDescription().getName()));
    }
}
