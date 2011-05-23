package net.centerleft.localshops;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import net.centerleft.localshops.commands.ShopCommandExecutor;
import net.centerleft.localshops.modules.economy.EconomyManager;
import net.centerleft.localshops.modules.permission.PermissionManager;
import net.centerleft.localshops.threads.NotificationThread;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cuboidLocale.QuadTree;

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
    private ShopManager shopManager = new ShopManager(this);
    public PluginDescriptionFile pdfFile = null;
    protected ReportThread reportThread = null;
    protected NotificationThread notificationThread = null;
    private EconomyManager econManager = null;
    private PermissionManager permManager = null;

    // Logging
    private final Logger log = Logger.getLogger("Minecraft");

    // Constants
    public static final String CHAT_PREFIX = ChatColor.DARK_AQUA + "[" + ChatColor.WHITE + "Shop" + ChatColor.DARK_AQUA + "] ";

    // TBD
    private static QuadTree cuboidTree = new QuadTree();
    
    //
    static final String folderPath = "plugins/LocalShops/";
    static final String shopsPath = "shops/";
    static File shopsDir;
    static File folderDir;
    static List<World> foundWorlds;

    static PropertyHandler properties;

    private static ItemData itemList = new ItemData();
    private Map<String, PlayerData> playerData; // synchronized player hash

    public void onEnable() {
        pdfFile = getDescription();
        setPlayerData(Collections.synchronizedMap(new HashMap<String, PlayerData>()));

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
        getCommand("shop").setExecutor(new ShopCommandExecutor(this));

        // setup the file IO
        folderDir = new File(folderPath);
        folderDir.mkdir();
        shopsDir = new File(folderPath + shopsPath);
        shopsDir.mkdir();

        properties = new PropertyHandler(folderPath + "localshops.properties");
        properties.load();
        loadProperties(properties);
        properties.save();

        foundWorlds = getServer().getWorlds();
        // read the shops into memory
        getShopManager().loadShops(shopsDir);

        // update the console that we've started
        log.info(String.format("[%s] %s", pdfFile.getName(), "Loaded with " + getShopManager().getNumShops() + " shop(s)"));
        log.info(String.format("[%s] %s", pdfFile.getName(), "Version " + pdfFile.getVersion() + " is enabled: " + Config.SRV_UUID.toString()));

        // check which shops players are inside
        for (Player player : this.getServer().getOnlinePlayers()) {
            playerListener.checkPlayerPosition(player);
        }
        
        // Start reporting thread
        if(Config.SRV_REPORT) {
            reportThread = new ReportThread(this, Config.SRV_UUID, false);
            reportThread.start();
        }
        
        // Start Notification thread
        if (Config.SHOP_TRANSACTION_NOTICE) {
            notificationThread = new NotificationThread(this);
            notificationThread.start();
        }
        
        setEconManager(new EconomyManager(this));
        if(!getEconManager().loadEconomies()) {
            // No valid economies, display error message and disables
            log.warning(String.format("[%s] FATAL: No economic plugins found, please refer to the documentation.", pdfFile.getName()));
            getPluginLoader().disablePlugin(this);
        }
        
        setPermManager(new PermissionManager(this));
        if(!getPermManager().load()) {
            // no valid permissions, display error message and disables
            log.warning(String.format("[%s] FATAL: No permission plugins found, please refer to the documentation.", pdfFile.getName()));
            getPluginLoader().disablePlugin(this);
        }
    }

    public void onDisable() {
        // Save all shops
        getShopManager().saveAllShops();
        
        // Stop Reporting thread
        if(Config.SRV_REPORT && reportThread != null && reportThread.isAlive()) {
            try {
                reportThread.setRun(false);
                reportThread.join(2000);
            } catch (InterruptedException e) {
                // hmm, thread didn't die
                log.warning(String.format("[%s] %s", pdfFile.getName(), "ReportThread did not exit"));
            }
        }
        
        // Stop notification thread
        if(Config.SHOP_TRANSACTION_NOTICE && notificationThread != null && notificationThread.isAlive()) {
            try {
                notificationThread.setRun(false);
                notificationThread.join(2000);
            } catch (InterruptedException e) {
                // hmm, thread didn't die
                log.warning(String.format("[%s] %s", pdfFile.getName(), "NotificationThread did not exit"));
            }
        }
        
        // update the console that we've stopped
        log.info(String.format("[%s] %s", pdfFile.getName(), "Version " + pdfFile.getVersion() + " is disabled!"));
    }

    private void loadProperties(PropertyHandler properties) {
        if (properties.keyExists("charge-for-shop")) {
            Config.SHOP_CHARGE_CREATE = properties.getBoolean("charge-for-shop");
            Config.SHOP_CHARGE_MOVE = properties.getBoolean("charge-for-shop");
        } else {
            properties.setBoolean("charge-for-shop", Config.SHOP_CHARGE_CREATE);
        }

        if (properties.keyExists("shop-cost")) {
            Config.SHOP_CHARGE_CREATE_COST = properties.getDouble("shop-cost");
        } else {
            properties.setDouble("shop-cost", Config.SHOP_CHARGE_CREATE_COST);
        }

        if (properties.keyExists("move-cost")) {
            Config.SHOP_CHARGE_MOVE_COST = properties.getDouble("move-cost");
        } else {
            properties.setDouble("move-cost", Config.SHOP_CHARGE_MOVE_COST);
        }

        if (properties.keyExists("shop-width")) {
            Config.SHOP_SIZE_DEF_WIDTH = properties.getLong("shop-width");
        } else {
            properties.setLong("shop-width", Config.SHOP_SIZE_DEF_WIDTH);
        }

        if (properties.keyExists("shop-height")) {
            Config.SHOP_SIZE_DEF_HEIGHT = properties.getLong("shop-height");
        } else {
            properties.setLong("shop-height", Config.SHOP_SIZE_DEF_HEIGHT);
        }

        if (properties.keyExists("max-width")) {
            Config.SHOP_SIZE_MAX_WIDTH = properties.getLong("max-width");
        } else {
            properties.setLong("max-width", Config.SHOP_SIZE_MAX_WIDTH);
        }

        if (properties.keyExists("max-height")) {
            Config.SHOP_SIZE_MAX_HEIGHT = properties.getLong("max-height");
        } else {
            properties.setLong("max-height", Config.SHOP_SIZE_MAX_HEIGHT);
        }
        if (properties.keyExists("shops-per-player")) {
            Config.PLAYER_MAX_SHOPS = properties.getInt("shops-per-player");
        } else {
            properties.setInt("shops-per-player", Config.PLAYER_MAX_SHOPS);
        }

        if (properties.keyExists("log-transactions")) {
            Config.SRV_LOG_TRANSACTIONS = properties.getBoolean("log-transactions");
        } else {
            properties.setBoolean("log-transactions", Config.SRV_LOG_TRANSACTIONS);
        }

        if (properties.keyExists("max-damage")) {
            Config.ITEM_MAX_DAMAGE = properties.getInt("max-damage");
            if (Config.ITEM_MAX_DAMAGE < 0)
                Config.ITEM_MAX_DAMAGE = 0;
        } else {
            properties.setInt("max-damage", Config.ITEM_MAX_DAMAGE);
        }
        
        if(properties.keyExists("uuid")) {
            Config.SRV_UUID = properties.getUuid("uuid");
        } else {
            Config.SRV_UUID = UUID.randomUUID();
            properties.setUuid("uuid", Config.SRV_UUID);
        }
        
        if(properties.keyExists("report-stats")) {
            Config.SRV_REPORT = properties.getBoolean("report-stats");
        } else {
            properties.setBoolean("report-stats", Config.SRV_REPORT);
        }
        
        if(properties.keyExists("debug")) {
            Config.SRV_DEBUG = properties.getBoolean("debug");
        } else {
            properties.setBoolean("debug", Config.SRV_DEBUG);
        }
        
        if(properties.keyExists("find-max-distance")) {
            Config.FIND_MAX_DISTANCE = properties.getInt("find-max-distance");
        } else {
            properties.setInt("find-max-distance", Config.FIND_MAX_DISTANCE);
        }
        
        if(properties.keyExists("shop-transaction-notice")) {
            Config.SHOP_TRANSACTION_NOTICE = properties.getBoolean("shop-notification");
        } else {
            properties.setBoolean("shop-notification", Config.SHOP_TRANSACTION_NOTICE);
        }
        
        if(properties.keyExists("shop-transactin-notice-timer")) {
            Config.SHOP_TRANSACTION_NOTICE_TIMER = properties.getInt("shop-notification-timer");
        } else {
            properties.setInt("shop-notification-timer", Config.SHOP_TRANSACTION_NOTICE_TIMER);
        }
        
        if(properties.keyExists("shop-transaction-max-size")) {
            Config.SHOP_TRANSACTION_MAX_SIZE = properties.getInt("shop-transaction-max-size");
        } else {
            properties.setInt("shop-transaction-max-size", Config.SHOP_TRANSACTION_MAX_SIZE);
        }
        
        if(properties.keyExists("global-shop")) {
            Config.GLOBAL_SHOP = properties.getBoolean("global-shop");
        } else {
            properties.setBoolean("global-shop", Config.GLOBAL_SHOP);
        }
        if(properties.keyExists("global-shop-uuid")) {
            Config.GLOBAL_SHOP_UUID = properties.getUuid("global-shop-uuid");
        } else {
            Config.GLOBAL_SHOP_UUID = UUID.randomUUID();
            properties.setUuid("global-shop-uuid", Config.GLOBAL_SHOP_UUID);
        }
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

    public static void setCuboidTree(QuadTree cuboidTree) {
        LocalShops.cuboidTree = cuboidTree;
    }

    public static QuadTree getCuboidTree() {
        return cuboidTree;
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
}