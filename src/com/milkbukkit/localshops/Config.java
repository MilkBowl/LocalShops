package com.milkbukkit.localshops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    
    // File paths
    private static final String dirPath = "plugins/LocalShops/";
    private static final String dirShopsActive = "shops/";
    private static final String dirShopsBroken = "shops-broken/";
    private static final String dirShopsConverted = "shops-converted/";
    private static final String fileTransactionLog = "transactions.log";
    
    // Properties file
    private static Properties properties = null;

    // Shop Size settings
    private static int shopSizeDefWidth = 5;
    private static int shopSizeDefHeight = 3;
    private static int shopSizeMaxWidth = 30;
    private static int shopSizeMaxHeight = 10;
    
    // Shop Charge settings
    private static double shopChargeCreateCost = 100;
    private static double shopChargeMoveCost = 10;
    private static boolean shopChargeCreate = true;
    private static boolean shopChargeMove = true;
    private static boolean shopTransactionNotice = true;
    private static int shopTransactionNoticeTimer = 300;
    private static int shopTransactionMaxSize = 100;
    
    // Find Settings
    private static int findMaxDistance = 150;
    
    // Chat Settings
    private static int chatMaxLines = 7;
    
    // Server Settings
    private static boolean srvLogTransactions = true;
    private static boolean srvDebug = false;
    private static UUID srvUuid = null;
    private static boolean srvReport = true;
    private static String srvReportUrl = "http://stats.cereal.sh/";
    private static int srvReportInterval = 21600;
    
    // Dynamic Shop Price Change variables
    private static int globalBaseStock = 0;
    private static double globalVolatility = 50.0;
    /*
     * TODO: implement event system/vars
    private static int dynamicInterval = 900;
    private static int dynamicMaxPriceChange = 50;
    private static int dynamicMinPriceChange = 1;
    private static int dynamicChance = 50;
    */
    // Global Shops
    private static Map<String, UUID> globalShops = Collections.synchronizedMap(new HashMap<String, UUID>(2));
    private static boolean globalShopsEnabled = false;
    
    // Player Settings
    private static int playerMaxShops = -1;        // Anything < 0 = unlimited player shops.
    
    // Item Settings
    private static int itemMaxDamage = 35;
    
    // UUID settings
    private static int uuidMinLength = 1;
    private static List<String> uuidList = Collections.synchronizedList(new ArrayList<String>());
    
    public static void save() {
        properties.setProperty("charge-for-shop", String.valueOf(shopChargeCreate));
        properties.setProperty("charge-for-shop", String.valueOf(shopChargeCreate));
        properties.setProperty("shop-cost", String.valueOf(shopChargeCreateCost));
        properties.setProperty("move-cost", String.valueOf(shopChargeMoveCost));
        properties.setProperty("shop-width", String.valueOf(shopSizeDefWidth));
        properties.setProperty("shop-height", String.valueOf(shopSizeDefHeight));
        properties.setProperty("max-width", String.valueOf(shopSizeMaxWidth));
        properties.setProperty("max-height", String.valueOf(shopSizeMaxHeight));
        properties.setProperty("shop-transaction-notice", String.valueOf(shopTransactionNotice));
        properties.setProperty("shop-notification-timer", String.valueOf(shopTransactionNoticeTimer));
        properties.setProperty("shop-transaction-max-size", String.valueOf(shopTransactionMaxSize));
        properties.setProperty("shops-per-player", String.valueOf(playerMaxShops));
        
        properties.setProperty("global-base-stock", String.valueOf(globalBaseStock));
        properties.setProperty("global-volatility", String.valueOf(globalVolatility));
        /*
         * Disabled - for future use with event system
        properties.setProperty("dynamic-interval", String.valueOf(dynamicInterval));
        properties.setProperty("dynamic-max-price-change", String.valueOf(dynamicMaxPriceChange));
        properties.setProperty("dynamic-min-price-change", String.valueOf(dynamicMinPriceChange));
        properties.setProperty("dynamic-chance", String.valueOf(dynamicChance));
        */
        properties.setProperty("max-damage", String.valueOf(itemMaxDamage));
        
        properties.setProperty("log-transactions", String.valueOf(srvLogTransactions));
        properties.setProperty("uuid", UUID.randomUUID().toString());
        properties.setProperty("report-stats", String.valueOf(srvReport));
        properties.setProperty("debug", String.valueOf(srvDebug));
        
        properties.setProperty("find-max-distance", String.valueOf(findMaxDistance));
        
        properties.setProperty("global-shops", String.valueOf(globalShopsEnabled));
        
        properties.setProperty("chat-max-lines", String.valueOf(chatMaxLines));
        
        Iterator<String> it = globalShops.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            UUID value = globalShops.get(key);
            
            properties.setProperty(String.format("%s-shop-UUID", key), value.toString());
        }
        
        try {
            properties.store(new FileOutputStream(Config.dirPath + "localshops.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void load() {
        new File(Config.dirPath).mkdir();
        
        boolean save = false;
        properties = new Properties();
        File file = new File(Config.dirPath + "localshops.properties");
        if(file.exists()) {
            try {
                properties.load(new FileInputStream(Config.dirPath + "localshops.properties"));
            } catch (IOException e) {
                save = true;
            }
        } else {
            save = true;
        }
        
        shopChargeCreate = Boolean.parseBoolean(properties.getProperty("charge-for-shop", String.valueOf(shopChargeCreate)));
        shopChargeCreate = Boolean.parseBoolean(properties.getProperty("charge-for-shop", String.valueOf(shopChargeCreate)));
        shopChargeCreateCost = Double.parseDouble(properties.getProperty("shop-cost", String.valueOf(shopChargeCreateCost)));
        shopChargeMoveCost = Double.parseDouble(properties.getProperty("move-cost", String.valueOf(shopChargeMoveCost)));
        shopSizeDefWidth = Integer.parseInt(properties.getProperty("shop-width", String.valueOf(shopSizeDefWidth)));
        shopSizeDefHeight = Integer.parseInt(properties.getProperty("shop-height", String.valueOf(shopSizeDefHeight)));
        shopSizeMaxWidth = Integer.parseInt(properties.getProperty("max-width", String.valueOf(shopSizeMaxWidth)));
        shopSizeMaxHeight = Integer.parseInt(properties.getProperty("max-height", String.valueOf(shopSizeMaxHeight)));
        shopTransactionNotice = Boolean.parseBoolean(properties.getProperty("shop-transaction-notice", String.valueOf(shopTransactionNotice)));
        shopTransactionNoticeTimer = Integer.parseInt(properties.getProperty("shop-notification-timer", String.valueOf(shopTransactionNoticeTimer)));
        shopTransactionMaxSize = Integer.parseInt(properties.getProperty("shop-transaction-max-size", String.valueOf(shopTransactionMaxSize)));

        globalBaseStock = Integer.parseInt(properties.getProperty("global-base-stock", String.valueOf(globalBaseStock)));
        globalVolatility = Integer.parseInt(properties.getProperty("global-volatility", String.valueOf(globalVolatility)));
        /*
         * Disabled - save for future use with event system
        dynamicInterval = Integer.parseInt(properties.getProperty("dynamic-interval", String.valueOf(dynamicInterval)));
        dynamicMaxPriceChange = Integer.parseInt(properties.getProperty("dynamic-max-price-change", String.valueOf(dynamicMaxPriceChange)));
        dynamicMinPriceChange = Integer.parseInt(properties.getProperty("dynamic-min-price-change", String.valueOf(dynamicMinPriceChange)));
        dynamicChance = Integer.parseInt(properties.getProperty("dynamic-chance", String.valueOf(dynamicChance)));
        */
        
        playerMaxShops = Integer.parseInt(properties.getProperty("shops-per-player", String.valueOf(playerMaxShops)));
        
        itemMaxDamage = Integer.parseInt(properties.getProperty("max-damage", String.valueOf(itemMaxDamage)));
        if(itemMaxDamage < 0) {
            itemMaxDamage = 0;
        }
        
        srvLogTransactions = Boolean.parseBoolean(properties.getProperty("log-transactions", String.valueOf(srvLogTransactions)));
        srvUuid = UUID.fromString(properties.getProperty("uuid", UUID.randomUUID().toString()));
        srvReport = Boolean.parseBoolean(properties.getProperty("report-stats", String.valueOf(srvReport)));
        srvDebug = Boolean.parseBoolean(properties.getProperty("debug", String.valueOf(srvDebug)));
        
        
        findMaxDistance = Integer.parseInt(properties.getProperty("find-max-distance", String.valueOf(findMaxDistance)));
        
        globalShopsEnabled = Boolean.parseBoolean(properties.getProperty("global-shops", String.valueOf(globalShopsEnabled)));
        
        chatMaxLines = Integer.parseInt(properties.getProperty("chat-max-lines", String.valueOf(chatMaxLines)));
        
        Pattern pattern = Pattern.compile("(?i)(.*)-shop-UUID$");
        Iterator<Object> it = properties.keySet().iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof String) {
                String key = (String) o;
                Matcher matcher = pattern.matcher(key);
                if(matcher.find()) {
                    // This is a global shop key
                    String world = matcher.group(1);
                    UUID uuid = UUID.fromString(properties.getProperty(key));
                    
                    globalShops.put(world, uuid);
                }
            } else {
                // gtfo
                continue;
            }
        }
        
        if(save) {
            save();
        }
    }
    
    /**
     * Get shop default width in blocks
     * @return default width
     */
    public static int getShopSizeDefWidth() { return shopSizeDefWidth; }
    
    /**
     * Get shop default height in blocks
     * @return default height
     */
    public static int getShopSizeDefHeight() { return shopSizeDefHeight; }
    
    /**
     * Get shop maximum width in blocks
     * @return maximum width
     */
    public static int getShopSizeMaxWidth() { return shopSizeMaxWidth; }
    
    /**
     * Get shop maximum height in blocks
     * @return maximum height
     */
    public static int getShopSizeMaxHeight() { return shopSizeMaxHeight; }
    
    /**
     * Set shop default width in blocks
     * @param size blocks
     */
    public static void setShopSizeDefWidth(int size) {
        shopSizeDefWidth = size;
    }
    
    /**
     * Set shop default height in blocks
     * @param size blocks
     */
    public static void setShopSizeDefHeight(int size) {
        shopSizeDefHeight = size;
    }
    
    /**
     * Set shop maximum width in blocks
     * @param size blocks
     */
    public static void setShopSizeMaxWidth(int size) {
        shopSizeMaxWidth = size;
    }
    
    /**
     * Set shop maximum height in blocks
     * @param size blocks
     */
    public static void setShopSizeMaxHeight(int size) {
        shopSizeMaxHeight = size;
    }
    
    /**
     * Get main directory path
     * 
     * @return
     */
    public static String getDirPath() {
        return dirPath;
    }

    /**
     * Get active shops path
     * 
     * @return
     */
    public static String getDirShopsActivePath() {
        return dirPath + dirShopsActive;
    }

    /**
     * Get broken shops path
     * 
     * @return
     */
    public static String getDirShopsBrokenPath() {
        return dirPath + dirShopsBroken;
    }

    /**
     * Get converted shops path
     * 
     * @return
     */
    public static String getDirShopsConvertedPath() {
        return dirPath + dirShopsConverted;
    }
    
    /**
     * Get transaction log path
     * @return
     */
    public static String getFileTransactionLog() {
        return dirPath + fileTransactionLog;
    }

    /**
     * Get shop create charge value, check ShopChargeCreate first
     * @return
     */
    public static double getShopChargeCreateCost() {
        return shopChargeCreateCost;
    }

    /**
     * Get shop move charge value, check ShopChargeMove first
     * @return
     */
    public static double getShopChargeMoveCost() {
        return shopChargeMoveCost;
    }

    /**
     * Get if charge for shop create
     * @return
     */
    public static boolean getShopChargeCreate() {
        return shopChargeCreate;
    }

    /**
     * Get if charge for shop move
     * @return
     */
    public static boolean getShopChargeMove() {
        return shopChargeMove;
    }

    /**
     * Get if notify shop owners on transactions
     * @return
     */
    public static boolean getShopTransactionNotice() {
        return shopTransactionNotice;
    }

    /**
     * Get transaction notice timer in seconds
     * @return
     */
    public static int getShopTransactionNoticeTimer() {
        return shopTransactionNoticeTimer;
    }

    /**
     * Get shop transaction maximum size
     * @return
     */
    public static int getShopTransactionMaxSize() {
        return shopTransactionMaxSize;
    }

    /**
     * Set shop charge cost
     * @param cost
     */
    public static void setShopChargeCreateCost(double cost) {
        shopChargeCreateCost = cost;
    }

    /**
     * Set shop move cost
     * @param cost
     */
    public static void setShopChargeMoveCost(double cost) {
        shopChargeMoveCost = cost;
    }
    
    /**
     * Set if shop charges for create
     * @param charge
     */
    public static void setShopChargeCreate(boolean charge) {
        shopChargeCreate = charge;
    }
    
    /**
     * Set if shop charges for move
     * @param charge
     */
    public static void setShopChargeMove(boolean charge) {
        shopChargeMove = charge;
    }
    
    /**
     * Set if server notifies shop owners of transactions
     * @param notify
     */
    public static void setShopTransactionNotice(boolean notify) {
        shopTransactionNotice = notify;
    }
    
    /**
     * Set notification interval in seconds
     * @param interval
     */
    public static void setShopTransactionNoticeTimer(int interval) {
        shopTransactionNoticeTimer = interval;
    }
    
    /**
     * Set shop maximum transaction size
     * Requires plugin to be reloaded to take effect!
     * @param size
     */
    public static void setShopTransactionMaxSize(int size) {
        shopTransactionMaxSize = size;
    }
    
    /**
     * Get maximum find distance in blocks, 0 is disabled, negative is unlimited
     * @return
     */
    public static int getFindMaxDistance() {
        return findMaxDistance;
    }
    
    /**
     * Set maximum find distance in blocks, 0 is disabled, negative is unlimited
     * @param distance
     */
    public static void setFindMaxDistance(int distance) {
        findMaxDistance = distance;
    }
    
    /**
     * Get maximum number of lines per page on chat
     * @return
     */
    public static int getChatMaxLines() {
        return chatMaxLines;
    }
    
    /**
     * Set maximum number of lines per page on chat
     * @param lines
     */
    public static void setChatMaxLines(int lines) {
        chatMaxLines = lines;
    }
    
    /**
     * Get server log transactions setting
     * @return
     */
    public static boolean getSrvLogTransactions() {
        return srvLogTransactions;
    }
    
    /**
     * Get server debug setting
     * @return
     */
    public static boolean getSrvDebug() {
        return srvDebug;
    }
    
    /**
     * Get server UUID
     * @return
     */
    public static UUID getSrvUuid() {
        return srvUuid;
    }
    
    /**
     * Get server report setting
     * @return
     */
    public static boolean getSrvReport() {
        return srvReport;
    }
    
    /**
     * Set if server logs transactions
     * @param log
     */
    public static void setSrvLogTransactions(boolean log) {
        srvLogTransactions = log;
    }
    
    /**
     * Set if server provides debug output to the logger (console)
     * @param debug
     */
    public static void setSrvDebug(boolean debug) {
        srvDebug = debug;
    }
    
    @Deprecated
    public static void setSrvUuid(UUID uuid) {
        // do nothing, intentionally unimplemented as is read-only parameter!
    }
    
    /**
     * Set if server sends anonymous reports to the developers
     * @param report
     */
    public static void setSrvReport(boolean report) {
        srvReport = report;
    }
    
    /**
     * Get reporting thread destination url
     * @return
     */
    public static String getSrvReportUrl() {
        return srvReportUrl;
    }
    
    /**
     * Set reporting thread destination url (including http://)
     * @param url
     */
    public static void setSrvReportUrl(String url) {
        srvReportUrl = url;
    }
    
    /**
     * Get reporting thread interval
     * @return
     */
    public static int getSrvReportInterval() {
        return srvReportInterval;
    }
    
    /**
     * Set reporting thread interval
     * @param interval
     */
    public static void setSrvReportInterval(int interval) {
        srvReportInterval = interval;
    }
    
    /**
     * Get global shop UUID
     * @param worldName World name
     * @return Shop Unique ID
     */
    public static UUID getGlobalShopUuid(String worldName) {
        return globalShops.get(worldName);
    }
    
    /**
     * Add world shop
     * @param worldName World name
     * @param uuid Shop Unique ID
     */
    public static void globalShopsAdd(String worldName, UUID uuid) {
        globalShops.put(worldName, uuid);
    }
    
    /**
     * Remove world shop
     * @param worldName World name
     */
    public static void globalShopsRemove(String worldName) {
        globalShops.remove(worldName);
    }
    
    /**
     * Does Global Shops contain a world?
     */
    public static boolean globalShopsContainsKey(String worldName) {
        return globalShops.containsKey(worldName);
    }
    
    /**
     * Get global shops setting
     * @return
     */
    public static boolean getGlobalShopsEnabled() {
        return globalShopsEnabled;
    }
    
    /**
     * Set global shops setting
     * @param enabled
     */
    public static void setGlobalShopsEnabled(boolean enabled) {
        globalShopsEnabled = enabled;
    }
    
    /**
     * Get maximum number of shops per player
     * @return
     */
    public static int getPlayerMaxShops() {
        return playerMaxShops;
    }
    
    /**
     * Set maximum number of shops per player
     * @param shops
     */
    public static void setPlayerMaxShops(int shops) {
        playerMaxShops = shops;
    }
    
    /**
     * Get maximum item damage (percent)
     * @return
     */
    public static int getItemMaxDamage() {
        return itemMaxDamage;
    }
    
    /**
     * Set maximum item damage (percent)
     * @param damage
     */
    public static void setItemMaxDamage(int damage) {
        if(damage >= 0 && damage <= 100) {
            itemMaxDamage = damage;
        } else if(damage < 0) {
            itemMaxDamage = 0;
        } else if(damage > 100) {
            itemMaxDamage = 100;
        }
    }
    
    /**
     * Get UUID minimum length
     * @return
     */
    public static int getUuidMinLength() {
        return uuidMinLength;
    }
    
    /**
     * Increment UUID minimum length
     */
    public static void incrementUuidMinLength() {
        uuidMinLength++;
    }
    
    /**
     * Decrement UUID minimum length
     */
    public static void decrementUuidMinLength() {
        uuidMinLength--;
    }
    
    /**
     * Set UUID minimum length
     * @param length
     */
    public static void setUuidMinLength(int length) {
        uuidMinLength = length;
    }
    
    /**
     * Get UUID List
     * @return
     */
    public static List<String> getUuidList() {
        return uuidList;
    }
    
    /**
     * Get if UUID list contains an short UUID (string)
     * @param uuid
     * @return
     */
    public static boolean uuidListContains(String uuid) {
        if(uuidList.contains(uuid)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Add short UUID to UUID List
     * @param uuid
     */
    public static void addUuidList(String uuid) {
        uuidList.add(uuid);
    }
    
    /**
     * Remove short UUID from UUID List
     * @param uuid
     */
    public static void removeUuidList(String uuid) {
        uuidList.remove(uuid);
    }
    
    /**
     * Empty UUID List
     */
    public static void clearUuidList() {
        uuidList.clear();
    }
    
    /*
     * TODO: event system
    public static int getDynamicInterval() {
        return dynamicInterval;
    }

    public static void setDynamicInterval(int dynamicInterval) {
        Config.dynamicInterval = dynamicInterval;
    }

    public static int getDynamicMaxPriceChange() {
        return dynamicMaxPriceChange;
    }

    public static void setDynamicMaxPriceChange(int maxPriceChange) {
        Config.dynamicMaxPriceChange = maxPriceChange;
    }

    public static int getDynamicMinPriceChange() {
        return dynamicMinPriceChange;
    }

    public static void setDynamicMinPriceChange(int minPriceChange) {
        Config.dynamicMinPriceChange = minPriceChange;
    }

    public static int getDynamicChance() {
        return dynamicChance;
    }

    public static void setDynamicChance(int dynamicChance) {
        Config.dynamicChance = dynamicChance;
    }
    */
    
    public static int getGlobalBaseStock() {
        return globalBaseStock;
    }

    public static void setGlobalBaseStock(int globalBaseStock) {
        Config.globalBaseStock = globalBaseStock;
    }

    public static void verifyGlobalShops(ShopManager shopManager) {
        Iterator<String> iter = globalShops.keySet().iterator();
        while (iter.hasNext())
        {
            String worldName = iter.next();
            if (shopManager.getShop(globalShops.get(worldName)) == null) {
                iter.remove();
            }
            
        }
    }

    public static void setGlobalVolatility(int globalVolatility) {
        Config.globalVolatility = globalVolatility;
    }

    public static double getGlobalVolatility() {
        return globalVolatility;
    }
}