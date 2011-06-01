package com.milkbukkit.localshops;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import com.milkbukkit.localshops.util.GenericFunctions;

public class ResourceManager {
    // Constants
    public final static String MAIN_LOAD = "MAIN_LOAD";
    public final static String MAIN_USING_LOCALE = "MAIN_USING_LOCALE";
    public final static String MAIN_ENABLE = "MAIN_ENABLE";
    public final static String MAIN_DISABLE = "MAIN_DISABLE";
    public final static String MAIN_ECONOMY_NOT_FOUND = "MAIN_ECONOMY_NOT_FOUND";
    public final static String MAIN_PERMISSION_NOT_FOUND = "MAIN_PERMISSION_NOT_FOUND";
    
    public final static String GEN_INVALID_VALUE = "GEN_INVALID_VALUE";
    public final static String GEN_USER_ACCESS_DENIED = "GEN_USER_ACCESS_DENIED";
    public final static String GEN_NOT_IN_SHOP = "GEN_NOT_IN_SHOP";
    public final static String GEN_MUST_BE_SHOP_OWNER = "GEN_MUST_BE_SHOP_OWNER";
    public final static String GEN_CURR_OWNER_IS = "GEN_CURR_OWNER_IS";
    public final static String GEN_ITEM_NOT_FOUND = "GEN_ITEM_NOT_FOUND";
    public final static String GEN_CONSOLE_NOT_IMPLEMENTED = "GEN_CONSOLE_NOT_IMPLEMENTED";
    
    public final static String CMD_ISSUED_LOCAL = "CMD_ISSUED_LOCAL";
    public final static String CMD_ISSUED_GLOBAL = "CMD_ISSUED_GLOBAL";
    
    public final static String CMD_ADM_SET_CFG_CHARGE_FOR_SHOP = "CMD_ADM_SET_CFG_CHARGE_FOR_SHOP";
    public final static String CMD_ADM_SET_CFG_GLOBAL_SHOP = "CMD_ADM_SET_CFG_GLOBAL_SHOP";
    public final static String CMD_ADM_SET_CFG_SHOP_WIDTH = "CMD_ADM_SET_CFG_SHOP_WIDTH";
    public final static String CMD_ADM_SET_CFG_REPORT_STATS = "CMD_ADM_SET_CFG_REPORT_STATS";
    public final static String CMD_ADM_SET_CFG_MAX_HEIGHT = "CMD_ADM_SET_CFG_MAX_HEIGHT";
    public final static String CMD_ADM_SET_CFG_MAX_WIDTH = "CMD_ADM_SET_CFG_MAX_WIDTH";
    public final static String CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE = "CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE";
    public final static String CMD_ADM_SET_CFG_SHOPS_COST = "CMD_ADM_SET_CFG_SHOPS_COST";
    public final static String CMD_ADM_SET_CFG_FIND_MAX_DISTANCE = "CMD_ADM_SET_CFG_FIND_MAX_DISTANCE";
    public final static String CMD_ADM_SET_CFG_SHOPS_PER_PLAYER = "CMD_ADM_SET_CFG_SHOPS_PER_PLAYER";
    public final static String CMD_ADM_SET_CFG_SHOP_HEIGHT = "CMD_ADM_SET_CFG_SHOP_HEIGHT";
    public final static String CMD_ADM_SET_CFG_DEBUG = "CMD_ADM_SET_CFG_DEBUG";
    public final static String CMD_ADM_SET_CFG_MAX_DAMAGE = "CMD_ADM_SET_CFG_MAX_DAMAGE";
    public final static String CMD_ADM_SET_CFG_MOVE_COST = "CMD_ADM_SET_CFG_MOVE_COST";
    public final static String CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER = "CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER";
    public final static String CMD_ADM_SET_CFG_SHOP_NOTIFICATION = "CMD_ADM_SET_CFG_SHOP_NOTIFICATION";
    public final static String CMD_ADM_SET_CFG_CHAT_MAX_LINES = "CMD_ADM_SET_CFG_CHAT_MAX_LINES";
    public final static String CMD_ADM_SET_CFG_LOG_TRANSACTIONS = "CMD_ADM_SET_CFG_LOG_TRANSACTIONS";
    
    public final static String CMD_SHP_ADD_TOO_DAM = "CMD_SHP_ADD_TOO_DAM";
    public final static String CMD_SHP_ADD_DMG_LESS_THAN = "CMD_SHP_ADD_DMG_LESS_THAN";
    public final static String CMD_SHP_ADD_USAGE = "CMD_SHP_ADD_USAGE";
    public final static String CMD_SHP_ADD_LOG = "CMD_SHP_LOG";
    public final static String CMD_SHP_ADD_INSUFFICIENT_INV = "CMD_SHP_ADD_INSUFFICIENT_INV";
    public final static String CMD_SHP_ADD_UNLIM_STOCK = "CMD_SHP_ADD_UNLIM_STOCK";
    public final static String CMD_SHP_ADD_ALREADY_HAS = "CMD_SHP_ADD_ALREADY_HAS";
    public final static String CMD_SHP_ADD_SUCCESS = "CMD_SHP_ADD_SUCCESS";
    public final static String CMD_SHP_ADD_STOCK_SUCCESS = "CMD_SHP_ADD_STOCK_SUCCESS";
    public final static String CMD_SHP_ADD_READY0 = "CMD_SHP_ADD_READY0";
    public final static String CMD_SHP_ADD_READY1 = "CMD_SHP_ADD_READY1";
    public final static String CMD_SHP_ADD_READY2 = "CMD_SHP_ADD_READY2";
    
    // Objects
    private PluginDescriptionFile pdf;
    private ResourceBundle bundle;
    
    // Color Map
    private static final Map<String, ChatColor> COLOR_MAP = new HashMap<String, ChatColor>();
    static {
        COLOR_MAP.put("%AQUA%", ChatColor.AQUA);
        COLOR_MAP.put("%BLACK%", ChatColor.BLACK);
        COLOR_MAP.put("%BLUE%", ChatColor.BLUE);
        COLOR_MAP.put("%DARK_AQUA%", ChatColor.DARK_AQUA);
        COLOR_MAP.put("%DARK_BLUE%", ChatColor.DARK_BLUE);
        COLOR_MAP.put("%DARK_GRAY%", ChatColor.DARK_GRAY);
        COLOR_MAP.put("%DARK_GREEN%", ChatColor.DARK_GREEN);
        COLOR_MAP.put("%DARK_PURPLE%", ChatColor.DARK_PURPLE);
        COLOR_MAP.put("%DARK_RED%", ChatColor.DARK_RED);
        COLOR_MAP.put("%GOLD%", ChatColor.GOLD);
        COLOR_MAP.put("%GRAY%", ChatColor.GRAY);
        COLOR_MAP.put("%GREEN%", ChatColor.GREEN);
        COLOR_MAP.put("%LIGHT_PURPLE%", ChatColor.LIGHT_PURPLE);
        COLOR_MAP.put("%RED%", ChatColor.RED);
        COLOR_MAP.put("%WHITE%", ChatColor.WHITE);
        COLOR_MAP.put("%YELLOW%", ChatColor.YELLOW);
    }
    
    public ResourceManager(PluginDescriptionFile p, Locale l) {
        pdf = p;
        if(l == null) {
            bundle = ResourceBundle.getBundle("com.milkbukkit.localshops.resources.StringLabel");
        } else {
            bundle = ResourceBundle.getBundle("com.milkbukkit.localshops.resources.StringLabel", l);
        }
    }
    
    // Get locale
    public Locale getLocale() {
        return bundle.getLocale();
    }
    
    // Get String
    public String getString(String key) {
        return parsePluginData(parseColors(bundle.getString(key))).replaceAll("%CHAT_PREFIX%", LocalShops.CHAT_PREFIX);
    }
    
    // Get String w/ Data Values (replace key array and replace value array MUST match lengths!
    public String getString(String key, String[] replaceKeys, Object[] replaceValues) {
        if(replaceKeys.length != replaceValues.length) {
            return null;
        }
        
        String s = getString(key);
        for(int i = 0; i < replaceKeys.length; i++) {
            s = s.replaceAll(replaceKeys[i], replaceValues[i].toString());
        }
        return s;
    }
    
    // Parse color data
    private String parseColors(String s) {
        for(String key : COLOR_MAP.keySet()) {
            s = s.replaceAll(key, COLOR_MAP.get(key).toString());
        }
        return s;
    }
    
    // Parse Plugin Data
    private String parsePluginData(String s) {
        s = s.replaceAll("%PLUGIN_NAME%", pdf.getName());
        s = s.replaceAll("%PLUGIN_VERSION%", pdf.getVersion());
        s = s.replaceAll("%PLUGIN_AUTHORS%", GenericFunctions.join(pdf.getAuthors(), ", "));
        return s;
    }
}
