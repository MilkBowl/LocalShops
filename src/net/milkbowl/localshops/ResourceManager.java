package net.milkbowl.localshops;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;


public class ResourceManager {
    // Constants
    public final static String BASE_SHOP = "Base.Shop";
    public final static String BASE_CHAT_PREFIX = "Base.ChatPrefix";
    public final static String BASE_TRUE = "Base.True";
    public final static String BASE_FALSE = "Base.False";
    
    public final static String MAIN_LOAD = "Main.Load";
    public final static String MAIN_USING_LOCALE = "Main.UsingLocale";
    public final static String MAIN_ENABLE = "Main.Enable";
    public final static String MAIN_DISABLE = "Main.Disable";
    public final static String MAIN_ECONOMY_NOT_FOUND = "Main.EconomyNotFound";
    public final static String MAIN_PERMISSION_NOT_FOUND = "Main.PermissionNotFound";
    
    public final static String GEN_INVALID_VALUE = "Generic.InvalidValue";
    public final static String GEN_USER_ACCESS_DENIED = "Generic.UserAccessDenied";
    public final static String GEN_NOT_IN_SHOP = "Generic.UserNotInShop";
    public final static String GEN_MUST_BE_SHOP_OWNER = "Generic.UserMustBeShopOwner";
    public final static String GEN_CURR_OWNER_IS = "Generic.CurrentOwnerIs";
    public final static String GEN_ITEM_NOT_FOUND = "Generic.ItemNotFound";
    public final static String GEN_CONSOLE_NOT_IMPLEMENTED = "Generic.ConsoleNotImplemented";
    public final static String GEN_UNEXPECTED_MONEY_ISSUE = "Generic.UnexpectedMoneyIssue";
    public final static String GEN_ITEM_NOT_CARRIED = "Generic.ItemNotCarried";
    
    public final static String CMD_ISSUED_LOCAL = "Command.IssuedLocal";
    public final static String CMD_ISSUED_GLOBAL = "Command.IssuedGlobal";
    
    public final static String CMD_ADM_SET_CFG_CHARGE_FOR_SHOP = "Command.Admin.SetConfig.ChargeForShop";
    public final static String CMD_ADM_SET_CFG_GLOBAL_SHOP = "Command.Admin.SetConfig.GlobalShop";
    public final static String CMD_ADM_SET_CFG_SHOP_WIDTH = "Command.Admin.SetConfig.ShopWidth";
    public final static String CMD_ADM_SET_CFG_REPORT_STATS = "Command.Admin.SetConfig.ReportStats";
    public final static String CMD_ADM_SET_CFG_MAX_HEIGHT = "Command.Admin.SetConfig.MaxHeight";
    public final static String CMD_ADM_SET_CFG_MAX_WIDTH = "Command.Admin.SetConfig.MaxWidth";
    public final static String CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE = "Command.Admin.SetConfig.ShopsTransactionsMaxSize";
    public final static String CMD_ADM_SET_CFG_SHOPS_COST = "Command.Admin.SetConfig.ShopsCost";
    public final static String CMD_ADM_SET_CFG_FIND_MAX_DISTANCE = "Command.Admin.SetConfig.FindMaxDistance";
    public final static String CMD_ADM_SET_CFG_SHOPS_PER_PLAYER = "Command.Admin.SetConfig.ShopsPerPlayer";
    public final static String CMD_ADM_SET_CFG_SHOP_HEIGHT = "Command.Admin.SetConfig.ShopHeight";
    public final static String CMD_ADM_SET_CFG_DEBUG = "Command.Admin.SetConfig.Debug";
    public final static String CMD_ADM_SET_CFG_MAX_DAMAGE = "Command.Admin.SetConfig.MaxDamage";
    public final static String CMD_ADM_SET_CFG_MOVE_COST = "Command.Admin.SetConfig.MoveCost";
    public final static String CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER = "Command.Admin.SetConfig.ShopNotificationTimer";
    public final static String CMD_ADM_SET_CFG_SHOP_NOTIFICATION = "Command.Admin.SetConfig.ShopNotification";
    public final static String CMD_ADM_SET_CFG_CHAT_MAX_LINES = "Command.Admin.SetConfig.ChatMaxLines";
    public final static String CMD_ADM_SET_CFG_LOG_TRANSACTIONS = "Command.Admin.SetConfig.LogTransactions";
    
    public final static String CMD_SHP_ADD_TOO_DAM = "Command.Shop.Add.TooDamaged";
    public final static String CMD_SHP_ADD_DMG_LESS_THAN = "Command.Shop.Add.DamageLessThan";
    public final static String CMD_SHP_ADD_USAGE = "Command.Shop.Add.Usage";
    public final static String CMD_SHP_ADD_LOG = "Command.Shop.Add.Log";
    public final static String CMD_SHP_ADD_INSUFFICIENT_INV = "Command.Shop.Add.InsufficientInventory";
    public final static String CMD_SHP_ADD_UNLIM_STOCK = "Command.Shop.Add.UnlimitedStock";
    public final static String CMD_SHP_ADD_ALREADY_HAS = "Command.Shop.Add.AlreadyHas";
    public final static String CMD_SHP_ADD_SUCCESS = "Command.Shop.Add.Success";
    public final static String CMD_SHP_ADD_STOCK_SUCCESS = "Command.Shop.Add.SuccessWithStock";
    public final static String CMD_SHP_ADD_READY0 = "Command.Shop.Add.Ready0";
    public final static String CMD_SHP_ADD_READY1 = "Command.Shop.Add.Ready1";
    public final static String CMD_SHP_ADD_READY2 = "Command.Shop.Add.Ready2";
    
    public final static String CMD_SHP_BUY_NO_ITEM_IN_HAND = "Command.Shop.Buy.NoItemInHand";
    public final static String CMD_SHP_BUY_MINIMUM_ONE = "Command.Shop.Buy.MinimumOfOne";
    public final static String CMD_SHP_BUY_USAGE = "Command.Shop.Buy.Usage";
    public final static String CMD_SHP_BUY_PLAYERS_ONLY = "Command.Shop.Buy.PlayersOnly";
    public final static String CMD_SHP_BUY_SHOP_NOT_SELLING = "Command.Shop.Buy.ShopNotSellingItem";
    public final static String CMD_SHP_BUY_SHOP_SOLD_OUT = "Command.Shop.Buy.ShopSoldOutItem";
    public final static String CMD_SHP_BUY_SHOP_HAS_QTY = "Command.Shop.Buy.ShopHasQuantity";
    public final static String CMD_SHP_BUY_ORDER_REDUCED = "Command.Shop.Buy.OrderReduced";
    public final static String CMD_SHP_BUY_PLAYER_INSUFFICIENT_ROOM = "Command.Shop.Buy.PlayerInsufficientRoom";
    public final static String CMD_SHP_BUY_PLAYER_AFFORD_NONE = "Command.Shop.Buy.PlayerCanAffordNone";
    public final static String CMD_SHP_BUY_PLAYER_AFFORD_QTY = "Command.Shop.Buy.PlayerCanAffordQuantity";
    public final static String CMD_SHP_BUY_REMOVED_QTY = "Command.Shop.Buy.RemovedQuantity";
    public final static String CMD_SHP_BUY_PURCHASED_QTY = "Command.Shop.Buy.PurchasedQuantity";
    
    public final static String CMD_SHP_CREATE_MAX_NUM_SHOPS = "Command.Shop.Create.MaximumNumberShops";
    public final static String CMD_SHP_CREATE_SELECTION_PROB_SIZE = "Command.Shop.Create.SelectionSizeProblem";
    public final static String CMD_SHP_CREATE_SELECTION_PROB_ONLY_ONE_POINT = "Command.Shop.Create.SelectionOnePointProblem";
    public final static String CMD_SHP_CREATE_SHOP_EXISTS = "Command.Shop.Create.ShopExists";
    public final static String CMD_SHP_CREATE_WORLD_HAS_GLOBAL = "Command.Shop.Create.WorldShopExists";
    public final static String CMD_SHP_CREATE_INSUFFICIENT_FUNDS = "Command.Shop.Create.InsufficientFunds";
    public final static String CMD_SHP_CREATE_LOG = "Command.Shop.Create.Log";
    public final static String CMD_SHP_CREATE_SUCCESS = "Command.Shop.Create.Success";
    public final static String CMD_SHP_CREATE_FAIL = "Command.Shop.Create.Fail";
    
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
            bundle = ResourceBundle.getBundle("props/Messages");
        } else {
            bundle = ResourceBundle.getBundle("props/Messages", l);
        }
    }
    
    // Get locale
    public Locale getLocale() {
        return bundle.getLocale();
    }
    
    // Get String
    public String getString(String key) {
        return parsePluginData(parseColors(parseBase(bundle.getString(key))));
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
        
        //Parse any other values leftover
        s = parseBase(s);
        s = parseColors(s);
        s = parsePluginData(s);
        
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
    
    // Parse Base
    private String parseBase(String s) {
        s = s.replaceAll("%BASESHOP%", bundle.getString(BASE_SHOP));
        s = s.replaceAll("%CHAT_PREFIX%", bundle.getString(BASE_CHAT_PREFIX));
        s = s.replaceAll("%TRUE%", bundle.getString(BASE_TRUE));
        s = s.replaceAll("%FALSE%", bundle.getString(BASE_FALSE));
        
        return s;
    }
    
    // Get Chat Prefix
    public String getChatPrefix() {
        return getString(BASE_CHAT_PREFIX);
    }
}
