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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.localshops.commands.ShopCommandExecutor;
import net.milkbowl.localshops.listeners.ShopsBlockListener;
import net.milkbowl.localshops.listeners.ShopsEntityListener;
import net.milkbowl.localshops.listeners.ShopsPlayerListener;
import net.milkbowl.localshops.objects.ItemData;
import net.milkbowl.localshops.objects.PlayerData;
import net.milkbowl.localshops.objects.ShopSign;
import net.milkbowl.localshops.threads.ThreadManager;
import net.milkbowl.vault.Vault;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * Local Shops Plugin
 * 
 * @author Jonbas
 */
public class LocalShops extends JavaPlugin {
    // Listeners & Objects
    public ShopsPlayerListener playerListener = new ShopsPlayerListener(this);
    public ShopsBlockListener blockListener = new ShopsBlockListener(this);
    public ShopsEntityListener entityListener = new ShopsEntityListener(this);

    // Managers
    private ShopManager shopManager = new ShopManager(this);
    private DynamicManager dynamicManager = new DynamicManager(this);
    public ThreadManager threadManager = new ThreadManager(this);
    private ResourceManager resManager = null;

    // Logging
    private final Logger log = Logger.getLogger("Minecraft");

    private static ItemData itemList = new ItemData();
    private Map<String, PlayerData> playerData = Collections.synchronizedMap(new HashMap<String, PlayerData>());
    public static Vault VAULT = null;
    
    public void onLoad() {
    	Config.load();
    }

    public void onEnable() {
        resManager = new ResourceManager(getDescription(), new Locale(Config.getLocale()));
        log.info(resManager.getString(ResourceManager.MAIN_USING_LOCALE, new String[] { "%LOCALE%" }, new String[] { resManager.getLocale().getLanguage() } ));

        // add all the online users to the data trees
        for (Player player : this.getServer().getOnlinePlayers()) {
            getPlayerData().put(player.getName(), new PlayerData(this, player.getName()));
        }

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Priority.Normal, this);

        // Register Commands
        CommandExecutor cmdExec = new ShopCommandExecutor(this);
        getCommand("lshop").setExecutor(cmdExec);
        getCommand("lsadmin").setExecutor(cmdExec);
        getCommand("gshop").setExecutor(cmdExec);
        getCommand("buy").setExecutor(cmdExec);
        getCommand("sell").setExecutor(cmdExec);
        getCommand("gbuy").setExecutor(cmdExec);
        getCommand("gsell").setExecutor(cmdExec);

        // setup the file IO
        File folderDir = new File(Config.getDirPath());
        folderDir.mkdir();
        File shopsDir = new File(Config.getDirShopsActivePath());
        shopsDir.mkdir();

        // read the shops into memory
        getShopManager().loadShops(shopsDir);

        // update the console that we've started
        log.info(resManager.getString(ResourceManager.MAIN_LOAD, new String[] { "%NUM_SHOPS%" }, new Object[] { getShopManager().getNumShops() }));
        log.info(resManager.getString(ResourceManager.MAIN_ENABLE, new String[] { "%UUID%" }, new Object[] { Config.getSrvUuid().toString() }));

        // check which shops players are inside
        for (Player player : this.getServer().getOnlinePlayers()) {
            playerListener.checkPlayerPosition(player);
        }

        // Start reporting thread
        if(Config.getSrvReport()) {
            threadManager.reportStart();
        }

        // Start Notification thread
        if (Config.getShopTransactionNotice()) {
            threadManager.notificationStart();
        }

        // Start Scheduler thread
        threadManager.schedulerStart();
        
        // Obtain Vault
        Plugin x = this.getServer().getPluginManager().getPlugin("Vault");
        if(x != null & x instanceof Vault) {
            VAULT = (Vault) x;
            log.info(String.format("[%s] Hooked into %s %s", getDescription().getName(), VAULT.getDescription().getName(), VAULT.getDescription().getVersion()));
        } else {
            log.warning(String.format("[%s] Vault was NOT found! Disabling plugin.", getDescription().getName()));
            getPluginLoader().disablePlugin(this);
            return;
        }
    }

    public void onDisable() {
        // Save all shops
        getShopManager().saveAllShops();

        // Save config file
        Config.save();

        // Stop Reporting thread
        threadManager.reportStop();

        // Stop Scheduler thread
        threadManager.schedulerStop();

        // Stop Notification thread
        threadManager.notificationStop();

        // update the console that we've stopped
        log.info(resManager.getString(ResourceManager.MAIN_DISABLE, new String[] { }, new Object[] { }));
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public Map<String, PlayerData> getPlayerData() {
        return playerData;
    }

    public static ItemData getItemList() {
        return itemList;
    }

    public ThreadManager getThreadManager() {
        return threadManager;
    }

    public DynamicManager getDynamicManager() {
        return dynamicManager;
    }

    public ResourceManager getResourceManager() {
        return resManager;
    }

    //Workaround for Bukkits inability to update multiple Signs in the same Tick
    public void scheduleUpdate(ShopSign sign, int delay) {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new updateSignState(sign), delay);
    }

    public class updateSignState implements Runnable {
        private ShopSign sign = null;

        public updateSignState(ShopSign sign) {
            this.sign = sign;
        }

        @Override
        public void run() {
            sign.getLoc().getBlock().getState().update(true);
        }

    }
}
