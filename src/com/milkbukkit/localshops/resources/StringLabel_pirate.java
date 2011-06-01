package com.milkbukkit.localshops.resources;

import java.util.ListResourceBundle;

import com.milkbukkit.localshops.ResourceManager;

public class StringLabel_pirate extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }
    
    static final Object[][] contents = {
            { ResourceManager.MAIN_LOAD, "[%PLUGIN_NAME%] Loaded with %NUM_SHOPS% shop(s)" },
            { ResourceManager.MAIN_USING_LOCALE, "[%PLUGIN_NAME%] Usin' language: %LOCALE%" },
            { ResourceManager.MAIN_ENABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% be enabled: %UUID%" },
            { ResourceManager.MAIN_DISABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% be disabled." },
            { ResourceManager.MAIN_ECONOMY_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No economic plugins found, please refer t' t' documentation." },
            { ResourceManager.MAIN_PERMISSION_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No permission plugins found, please refer t' t' documentation." },
            { ResourceManager.CMD_ISSUED_LOCAL, "[%PLUGIN_NAME%] %NAME% issued global command: %COMMAND%" },
            { ResourceManager.CMD_ISSUED_GLOBAL, "[%PLUGIN_NAME%] %NAME% issued local command: %COMMAND%" },
            { ResourceManager.GEN_USER_ACCESS_DENIED, "%CHAT_PREFIX%%DARK_AQUA%You don't have permission t' use this command." },
            { ResourceManager.CMD_ADM_SET_CFG_CHARGE_FOR_SHOP, "%BLUE%charge-for-shop: %WHITE%[true/false] Charge for shop creation toggle." },
            { ResourceManager.CMD_ADM_SET_CFG_GLOBAL_SHOP, "%BLUE%global-shop: %WHITE%[true/false] Global Shop toggle." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_WIDTH, "%BLUE%shop-width: %WHITE%[number] Default shop width." },
            { ResourceManager.CMD_ADM_SET_CFG_REPORT_STATS, "%BLUE%report-stats: %WHITE%[number] Allows anonymous usage statistics." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_HEIGHT, "%BLUE%max-height: %WHITE%[number] Maximum height o' a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_WIDTH, "%BLUE%max-width: %WHITE%[number] Maximum width o' a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE, "%BLUE%shops-transaction-max-size: %WHITE%[number] Maximum transactions t' store in memory used by notifications." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_COST, "%BLUE%shops-cost: %WHITE%[decimal] Cost o' creatin' a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_FIND_MAX_DISTANCE, "%BLUE%find-max-distance: %WHITE%[number] Maximum distance between player and shop for /shop find command." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_PER_PLAYER, "%BLUE%shops-per-player: %WHITE%[number] Maximum number o' shops a player can own." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_PER_PLAYER, "%BLUE%shop-height: %WHITE%[number] Default height o' shops." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_PER_PLAYER, "%BLUE%debug: %WHITE%[true/false] Debuggin' mode, may degrade performance or be annoyin'." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_DAMAGE, "%BLUE%max-damage: %WHITE%[number] Number between 0 and 100, percent o' damage an item is allowed t' have, not remainin' durablity." },
            { ResourceManager.CMD_ADM_SET_CFG_MOVE_COST, "%BLUE%move-cost: %WHITE%[decimal] Cost for movin' a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER, "%BLUE%hop-notification-timer: %WHITE%[number] Interval between transaction notifications." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION, "%BLUE%shop-transaction-notice: %WHITE%[true/false] Notification o' transactions t' shop owners." },
            { ResourceManager.CMD_ADM_SET_CFG_CHAT_MAX_LINES, "%BLUE%chat-max-lines: %WHITE%[number] Number o' lines displayed in paginated output." },
            { ResourceManager.CMD_ADM_SET_CFG_LOG_TRANSACTIONS, "%BLUE%log-transactions: %WHITE%[true/false] Transaction loggin' t' transactions.log file." },
            { ResourceManager.GEN_NOT_IN_SHOP, "%DARK_AQUA%You be not in a shop!" },
            { ResourceManager.GEN_MUST_BE_SHOP_OWNER, "%DARK_AQUA%You must be t' shop owner or a manager." },
            { ResourceManager.GEN_CURR_OWNER_IS, "%DARK_AQUA%T' current shop owner be %WHITE%%OWNER%" },
            { ResourceManager.CMD_SHP_ADD_TOO_DAM, "%DARK_AQUA%Your %WHITE%%ITEMNAME%%DARK_AQUA% be too damaged t' add t' stock!" },
            { ResourceManager.CMD_SHP_ADD_DMG_LESS_THAN, "%DARK_AQUA%Items must be damaged less than %WHITE%%DAMAGEVALUE%%" },
            { ResourceManager.CMD_SHP_ADD_USAGE, "%WHITE%   /%CMDLABEL% add [itemname] [number] %DARK_AQUA%- Add this item to inventory." },
            { ResourceManager.CMD_SHP_ADD_LOG, "Add %AMOUNT% of %ITEMNAME% to %SHOPNAME%" },
            { ResourceManager.CMD_SHP_ADD_INSUFFICIENT_INV, "%DARK_AQUA%You only have %WHITE%%ITEMCOUNT%%DARK_AQUA% in your inventory that can be added." },
            { ResourceManager.CMD_SHP_ADD_UNLIM_STOCK, "%SHOPNAME% has unlimited stock and already carries %ITEMNAME%!" },
            { ResourceManager.CMD_SHP_ADD_ALREADY_HAS, "%SHOPNAME% already carries %ITEMNAME%!" },
            { ResourceManager.CMD_SHP_ADD_SUCCESS, "%DARK_AQUA%Successfully added %WHITE%%ITEMNAME%%DARK_AQUA% t' t' shop." },
            { ResourceManager.CMD_SHP_ADD_STOCK_SUCCESS, "%DARK_AQUA%Successfully added %WHITE%%ITEMNAME%%DARK_AQUA% t' t' shop. Stock be now %WHITE%%STOCK%" },
            { ResourceManager.CMD_SHP_ADD_READY0, "%DARK_AQUA%%ITEMNAME% is almost ready t' be purchased or sold!" },
            { ResourceManager.CMD_SHP_ADD_READY1, "%DARK_AQUA%Use %WHITE%\"/shop set sell %ITEMNAME% price bundle\"%DARK_AQUA% t' sell this item!" },
            { ResourceManager.CMD_SHP_ADD_READY2, "%DARK_AQUA%Use %WHITE%\"/shop set  buy %ITEMNAME% price bundle\"%DARK_AQUA% t'  buy this item!" },
            { ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED, "Console be not implemented for this command." },
            { ResourceManager.GEN_ITEM_NOT_FOUND, "Could not find an item." },
            { ResourceManager.GEN_INVALID_VALUE, "Invalid value" },
    };
}
