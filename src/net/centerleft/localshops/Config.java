package net.centerleft.localshops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Config {

    // Shop Size settings
    public static int SHOP_SIZE_DEF_WIDTH = 5;
    public static int SHOP_SIZE_DEF_HEIGHT = 3;
    public static int SHOP_SIZE_MAX_WIDTH = 30;
    public static int SHOP_SIZE_MAX_HEIGHT = 10;
    
    // Shop Charge settings
    public static double SHOP_CHARGE_CREATE_COST = 100;
    public static double SHOP_CHARGE_MOVE_COST = 10;
    public static boolean SHOP_CHARGE_CREATE = true;
    public static boolean SHOP_CHARGE_MOVE = true;
    public static boolean SHOP_TRANSACTION_NOTICE = true;
    public static int SHOP_TRANSACTION_NOTICE_TIMER = 300;
    public static int SHOP_TRANSACTION_MAX_SIZE = 100;
    
    // Find Settings
    public static int FIND_MAX_DISTANCE = 150;
    
    // Chat Settings
    public static int CHAT_MAX_LINES = 7;
    
    // Server Settings
    public static boolean SRV_LOG_TRANSACTIONS = true;
    public static boolean SRV_DEBUG = false;
    public static UUID SRV_UUID = null;
    public static boolean SRV_REPORT = true;
    public static boolean GLOBAL_SHOP = false;
    public static UUID GLOBAL_SHOP_UUID = null;
    
    // Player Settings
    public static int PLAYER_MAX_SHOPS = -1;        // Anything < 0 = unlimited player shops.
    
    // Item Settings
    public static int ITEM_MAX_DAMAGE = 35;
    
    // UUID settings
    public static int UUID_MIN_LENGTH = 1;
    protected static List<String> UUID_LIST = Collections.synchronizedList(new ArrayList<String>());
    
    // Other
    public static final String DIR_PATH = "plugins/LocalShops/";
    public static final String DIR_SHOPS_ACTIVE = "shops/";
    public static final String DIR_SHOPS_BROKEN = "shops-broken/";
    public static final String DIR_SHOPS_CONVERTED = "shops-converted/";
    
    public static void loadProperties() {
        PropertyHandler properties = new PropertyHandler(Config.DIR_PATH + "localshops.properties");
        properties.load();
        
        if (properties.keyExists("charge-for-shop")) {
            SHOP_CHARGE_CREATE = properties.getBoolean("charge-for-shop");
            SHOP_CHARGE_MOVE = properties.getBoolean("charge-for-shop");
        } else {
            properties.setBoolean("charge-for-shop", SHOP_CHARGE_CREATE);
        }

        if (properties.keyExists("shop-cost")) {
            SHOP_CHARGE_CREATE_COST = properties.getDouble("shop-cost");
        } else {
            properties.setDouble("shop-cost", SHOP_CHARGE_CREATE_COST);
        }

        if (properties.keyExists("move-cost")) {
            SHOP_CHARGE_MOVE_COST = properties.getDouble("move-cost");
        } else {
            properties.setDouble("move-cost", SHOP_CHARGE_MOVE_COST);
        }

        if (properties.keyExists("shop-width")) {
            SHOP_SIZE_DEF_WIDTH = properties.getInt("shop-width");
        } else {
            properties.setLong("shop-width", SHOP_SIZE_DEF_WIDTH);
        }

        if (properties.keyExists("shop-height")) {
            SHOP_SIZE_DEF_HEIGHT = properties.getInt("shop-height");
        } else {
            properties.setLong("shop-height", SHOP_SIZE_DEF_HEIGHT);
        }

        if (properties.keyExists("max-width")) {
            SHOP_SIZE_MAX_WIDTH = properties.getInt("max-width");
        } else {
            properties.setLong("max-width", SHOP_SIZE_MAX_WIDTH);
        }

        if (properties.keyExists("max-height")) {
            SHOP_SIZE_MAX_HEIGHT = properties.getInt("max-height");
        } else {
            properties.setLong("max-height", SHOP_SIZE_MAX_HEIGHT);
        }
        if (properties.keyExists("shops-per-player")) {
            PLAYER_MAX_SHOPS = properties.getInt("shops-per-player");
        } else {
            properties.setInt("shops-per-player", PLAYER_MAX_SHOPS);
        }

        if (properties.keyExists("log-transactions")) {
            SRV_LOG_TRANSACTIONS = properties.getBoolean("log-transactions");
        } else {
            properties.setBoolean("log-transactions", SRV_LOG_TRANSACTIONS);
        }

        if (properties.keyExists("max-damage")) {
            ITEM_MAX_DAMAGE = properties.getInt("max-damage");
            if (ITEM_MAX_DAMAGE < 0)
                ITEM_MAX_DAMAGE = 0;
        } else {
            properties.setInt("max-damage", ITEM_MAX_DAMAGE);
        }
        
        if(properties.keyExists("uuid")) {
            SRV_UUID = properties.getUuid("uuid");
        } else {
            SRV_UUID = UUID.randomUUID();
            properties.setUuid("uuid", SRV_UUID);
        }
        
        if(properties.keyExists("report-stats")) {
            SRV_REPORT = properties.getBoolean("report-stats");
        } else {
            properties.setBoolean("report-stats", SRV_REPORT);
        }
        
        if(properties.keyExists("debug")) {
            SRV_DEBUG = properties.getBoolean("debug");
        } else {
            properties.setBoolean("debug", SRV_DEBUG);
        }
        
        if(properties.keyExists("find-max-distance")) {
            FIND_MAX_DISTANCE = properties.getInt("find-max-distance");
        } else {
            properties.setInt("find-max-distance", FIND_MAX_DISTANCE);
        }
        
        if(properties.keyExists("shop-transaction-notice")) {
            SHOP_TRANSACTION_NOTICE = properties.getBoolean("shop-notification");
        } else {
            properties.setBoolean("shop-notification", SHOP_TRANSACTION_NOTICE);
        }
        
        if(properties.keyExists("shop-transactin-notice-timer")) {
            SHOP_TRANSACTION_NOTICE_TIMER = properties.getInt("shop-notification-timer");
        } else {
            properties.setInt("shop-notification-timer", SHOP_TRANSACTION_NOTICE_TIMER);
        }
        
        if(properties.keyExists("shop-transaction-max-size")) {
            SHOP_TRANSACTION_MAX_SIZE = properties.getInt("shop-transaction-max-size");
        } else {
            properties.setInt("shop-transaction-max-size", SHOP_TRANSACTION_MAX_SIZE);
        }
        
        if(properties.keyExists("global-shop")) {
            GLOBAL_SHOP = properties.getBoolean("global-shop");
        } else {
            properties.setBoolean("global-shop", GLOBAL_SHOP);
        }
        if(properties.keyExists("global-shop-uuid")) {
            GLOBAL_SHOP_UUID = properties.getUuid("global-shop-uuid");
        } else {
            GLOBAL_SHOP_UUID = UUID.randomUUID();
            properties.setUuid("global-shop-uuid", GLOBAL_SHOP_UUID);
        }
        if(properties.keyExists("chat-max-lines")) {
            CHAT_MAX_LINES = properties.getInt("chat-max-lines");
        } else {
            properties.setInt("chat-max-lines", CHAT_MAX_LINES);
        }
        
        properties.save();
    }
}
