package com.milkbukkit.localshops.resources;

import java.util.ListResourceBundle;

import com.milkbukkit.localshops.ResourceManager;

public class StringLabel extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { ResourceManager.MAIN_LOAD, "[%PLUGIN_NAME%] Loaded with %NUM_SHOPS% shop(s)" },
            { ResourceManager.MAIN_USING_LOCALE, "[%PLUGIN_NAME%] Using language: %LOCALE%" },
            { ResourceManager.MAIN_ENABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is enabled: %UUID%" },
            { ResourceManager.MAIN_DISABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is disabled." },
            { ResourceManager.MAIN_ECONOMY_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No economic plugins found, please refer to the documentation." },
            { ResourceManager.MAIN_PERMISSION_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No permission plugins found, please refer to the documentation." },
            { ResourceManager.CMD_ISSUED_LOCAL, "[%PLUGIN_NAME%] %NAME% issued global command: %COMMAND%" },
            { ResourceManager.CMD_ISSUED_GLOBAL, "[%PLUGIN_NAME%] %NAME% issued local command: %COMMAND%" },
            { ResourceManager.GEN_USER_ACCESS_DENIED, "%CHAT_PREFIX%%DARK_AQUA%You don't have permission to use this command" },
            { ResourceManager.CMD_ADM_SET_CFG_CHARGE_FOR_SHOP, "%BLUE%charge-for-shop: %WHITE%[true/false] Charge for shop creation toggle." },
            { ResourceManager.CMD_ADM_SET_CFG_GLOBAL_SHOP, "%BLUE%global-shop: %WHITE%[true/false] Global Shop toggle." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_WIDTH, "%BLUE%shop-width: %WHITE%[number] Default shop width." },
            { ResourceManager.CMD_ADM_SET_CFG_REPORT_STATS, "%BLUE%report-stats: %WHITE%[number] Allows anonymous usage statistics." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_HEIGHT, "%BLUE%max-height: %WHITE%[number] Maximum height of a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_WIDTH, "%BLUE%max-width: %WHITE%[number] Maximum width of a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE, "%BLUE%shops-transaction-max-size: %WHITE%[number] Maximum transactions to store in memory used by notifications." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_COST, "%BLUE%shops-cost: %WHITE%[decimal] Cost of creating a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_FIND_MAX_DISTANCE, "%BLUE%find-max-distance: %WHITE%[number] Maximum distance between player and shop for /shop find command." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOPS_PER_PLAYER, "%BLUE%shops-per-player: %WHITE%[number] Maximum number of shops a player can own." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_HEIGHT, "%BLUE%shop-height: %WHITE%[number] Default height of shops." },
            { ResourceManager.CMD_ADM_SET_CFG_DEBUG, "%BLUE%debug: %WHITE%[true/false] Debugging mode, may degrade performance or be annoying." },
            { ResourceManager.CMD_ADM_SET_CFG_MAX_DAMAGE, "%BLUE%max-damage: %WHITE%[number] Number between 0 and 100, percent of damage an item is allowed to have, not remaining durablity." },
            { ResourceManager.CMD_ADM_SET_CFG_MOVE_COST, "%BLUE%move-cost: %WHITE%[decimal] Cost for moving a shop." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER, "%BLUE%shop-notification-timer: %WHITE%[number] Interval between transaction notifications." },
            { ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION, "%BLUE%shop-transaction-notice: %WHITE%[true/false] Notification of transactions to shop owners." },
            { ResourceManager.CMD_ADM_SET_CFG_CHAT_MAX_LINES, "%BLUE%chat-max-lines: %WHITE%[number] Number of lines displayed in paginated output." },
            { ResourceManager.CMD_ADM_SET_CFG_LOG_TRANSACTIONS, "%BLUE%log-transactions: %WHITE%[true/false] Transaction logging to transactions.log file." },
            { ResourceManager.GEN_NOT_IN_SHOP, "%DARK_AQUA%You are not in a shop!" },
            { ResourceManager.GEN_MUST_BE_SHOP_OWNER, "%DARK_AQUA%You must be the shop owner or a manager." },
            { ResourceManager.GEN_CURR_OWNER_IS, "%DARK_AQUA%The current shop owner is %WHITE%%OWNER%" },
            { ResourceManager.CMD_SHP_ADD_TOO_DAM, "%DARK_AQUA%Your %WHITE%%ITEMNAME%%DARK_AQUA% is too damaged to add to stock!" },
            { ResourceManager.CMD_SHP_ADD_DMG_LESS_THAN, "%DARK_AQUA%Items must be damaged less than %WHITE%%DAMAGEVALUE%%" },
            { ResourceManager.CMD_SHP_ADD_USAGE, "%WHITE%   /%CMDLABEL% add [itemname] [number] %DARK_AQUA%- Add this item to inventory." },
            { ResourceManager.CMD_SHP_ADD_LOG, "Add %AMOUNT% of %ITEMNAME% to %SHOPNAME%" },
            { ResourceManager.CMD_SHP_ADD_INSUFFICIENT_INV, "%DARK_AQUA%You only have %WHITE%%ITEMCOUNT%%DARK_AQUA% in your inventory that can be added." },
            { ResourceManager.CMD_SHP_ADD_UNLIM_STOCK, "%SHOPNAME% has unlimited stock and already carries %ITEMNAME%!" },
            { ResourceManager.CMD_SHP_ADD_ALREADY_HAS, "%SHOPNAME% already carries %ITEMNAME%!" },
            { ResourceManager.CMD_SHP_ADD_SUCCESS, "%DARK_AQUA%Successfully added %WHITE%%ITEMNAME%%DARK_AQUA% to the shop." },
            { ResourceManager.CMD_SHP_ADD_STOCK_SUCCESS, "%DARK_AQUA%Successfully added %WHITE%%ITEMNAME%%DARK_AQUA% to the shop. Stock is now %WHITE%%STOCK%" },
            { ResourceManager.CMD_SHP_ADD_READY0, "%DARK_AQUA%%ITEMNAME% is almost ready to be purchased or sold!" },
            { ResourceManager.CMD_SHP_ADD_READY1, "%DARK_AQUA%Use %WHITE%\"/shop set sell %ITEMNAME% price bundle\"%DARK_AQUA% to sell this item!" },
            { ResourceManager.CMD_SHP_ADD_READY2, "%DARK_AQUA%Use %WHITE%\"/shop set  buy %ITEMNAME% price bundle\"%DARK_AQUA% to  buy this item!" },
            { ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED, "Console is not implemented for this command." },
            { ResourceManager.GEN_ITEM_NOT_FOUND, "Could not find an item." },
            { ResourceManager.GEN_INVALID_VALUE, "Invalid value" },
    };

}