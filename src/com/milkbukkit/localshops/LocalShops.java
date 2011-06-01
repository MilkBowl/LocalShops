package com.milkbukkit.localshops;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.milkbukkit.localshops.commands.ShopCommandExecutor;
import com.milkbukkit.localshops.listeners.ShopsBlockListener;
import com.milkbukkit.localshops.listeners.ShopsEntityListener;
import com.milkbukkit.localshops.listeners.ShopsPlayerListener;
import com.milkbukkit.localshops.modules.economy.EconomyManager;
import com.milkbukkit.localshops.modules.permission.PermissionManager;
import com.milkbukkit.localshops.threads.ThreadManager;

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
    private EconomyManager econManager = null;
    private PermissionManager permManager = null;
    private ResourceManager resManager = null;

    // Logging
    private final Logger log = Logger.getLogger("Minecraft");

    // Constants
    public static final String CHAT_PREFIX = ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Shop" + ChatColor.DARK_AQUA + "] ";

    
    private static ItemData itemList = new ItemData();
    private Map<String, PlayerData> playerData; // synchronized player hash
    
    public LocalShops() {
        Config.load();
    }

    public void onEnable() {
        setPlayerData(Collections.synchronizedMap(new HashMap<String, PlayerData>()));
        resManager = new ResourceManager(getDescription(), new Locale("pirate"));
        log.info(resManager.getString(ResourceManager.USING_LOCALE, new String[] { "%LOCALE%" }, new String[] { resManager.getLocale().toString() } ));

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
        log.info(resManager.getString(ResourceManager.LOAD, new String[] { "%NUM_SHOPS%" }, new Object[] { getShopManager().getNumShops() }));
        log.info(resManager.getString(ResourceManager.ENABLE, new String[] { "%UUID%" }, new Object[] { Config.getSrvUuid().toString() }));

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
        
        
        setEconManager(new EconomyManager(this));
        if(!getEconManager().loadEconomies()) {
            // No valid economies, display error message and disables
            log.warning(resManager.getString(ResourceManager.ECONOMY_NOT_FOUND, new String[] { }, new Object[] { }));
            getPluginLoader().disablePlugin(this);
        }
        
        setPermManager(new PermissionManager(this));
        if(!getPermManager().load()) {
            // no valid permissions, display error message and disables
            log.warning(resManager.getString(ResourceManager.PERMISSION_NOT_FOUND, new String[] { }, new Object[] { }));
            getPluginLoader().disablePlugin(this);
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
        log.warning(resManager.getString(ResourceManager.DISABLE, new String[] { }, new Object[] { }));
    }

    public void setShopData(ShopManager shopData) {
        this.shopManager = shopData;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public void setPlayerData(Map<String, PlayerData> playerData) {
        this.playerData = playerData;
    }

    public Map<String, PlayerData> getPlayerData() {
        return playerData;
    }

    public static void setItemList(ItemData itemList) {
        LocalShops.itemList = itemList;
    }

    public static ItemData getItemList() {
        return itemList;
    }

    public void setEconManager(EconomyManager econManager) {
        this.econManager = econManager;
    }

    public EconomyManager getEconManager() {
        return econManager;
    }

    public void setPermManager(PermissionManager permManager) {
        this.permManager = permManager;
    }

    public PermissionManager getPermManager() {
        return permManager;
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
}
