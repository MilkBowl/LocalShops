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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;

import org.bukkit.plugin.java.JavaPlugin;

public class ReportThread extends Thread {

    protected static final Logger log = Logger.getLogger("Minecraft");
    private boolean run = true;
    private JavaPlugin plugin = null;

    public ReportThread(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        // Obtain values
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        String java = System.getProperty("java.vendor") + " " + System.getProperty("java.version");
        String pluginName = plugin.getDescription().getName();
        String pluginVersion = plugin.getDescription().getVersion();
        String bukkitVersion = plugin.getServer().getVersion();

        if (Config.getSrvDebug()) {
            log.info(String.format("[%s] Start of ReportThread", pluginName));
        }

        while (true) {
            try {
                URL statsUrl = new URL(String.format(
                        "%s?uuid=%s&plugin=%s&version=%s&bVersion=%s&osName=%s&osArch=%s&osVersion=%s&java=%s",
                        Config.getSrvReportUrl(),
                        URLEncoder.encode(Config.getSrvUuid().toString(), "ISO-8859-1"),
                        URLEncoder.encode(pluginName, "ISO-8859-1"),
                        URLEncoder.encode(pluginVersion, "ISO-8859-1"),
                        URLEncoder.encode(bukkitVersion, "ISO-8859-1"),
                        URLEncoder.encode(osName, "ISO-8859-1"),
                        URLEncoder.encode(osArch, "ISO-8859-1"),
                        URLEncoder.encode(osVersion, "ISO-8859-1"),
                        URLEncoder.encode(java, "ISO-8859-1")));
                URLConnection conn = statsUrl.openConnection();
                conn.setRequestProperty("User-Agent", String.format("%s %s:%s", "BukkitReport", pluginName, pluginVersion));
                String inputLine;
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                if (Config.getSrvDebug()) {
                    // Output the contents -- wee
                    log.info(String.format("[%s] StatsURL: %s", pluginName, statsUrl.toString()));
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equals("<pre>") || inputLine.equals("</pre>")) {
                            continue;
                        }
                        log.info(String.format("[%s] %s", pluginName, inputLine));
                    }
                }

                in.close();

            } catch (MalformedURLException e) {
                // Ignore it...really its just not important
                return;
            } catch (IOException e) {
                // Ignore it...really its just not important
                return;
            }

            // Sleep for 6 hours...
            for (int i = 0; i < Config.getSrvReportInterval(); i++) {
                if (!Config.getSrvReport() || !run) {
                    if (Config.getSrvDebug()) {
                        log.info(String.format("[%s] Graceful quit of ReportThread", pluginName));
                    }
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // stuff
                }
            }
        }
    }
}
