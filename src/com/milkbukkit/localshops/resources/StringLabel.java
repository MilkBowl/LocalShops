package com.milkbukkit.localshops.resources;

import java.util.ListResourceBundle;

import org.bukkit.ChatColor;

import com.milkbukkit.localshops.ResourceManager;

public class StringLabel extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { ResourceManager.LOAD, "[%PLUGIN_NAME%] Loaded with %NUM_SHOPS% shop(s)" },
            { ResourceManager.USING_LOCALE, "[%PLUGIN_NAME%] Using language: %LOCALE%" },
            { ResourceManager.ENABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is enabled: %UUID%" },
            { ResourceManager.DISABLE, "[%PLUGIN_NAME%] Version %PLUGIN_VERSION% is disabled." },
            { ResourceManager.ECONOMY_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No economic plugins found, please refer to the documentation." },
            { ResourceManager.PERMISSION_NOT_FOUND, "[%PLUGIN_NAME%] FATAL: No permission plugins found, please refer to the documentation." },
            { ResourceManager.COMMAND_ISSUED_LOCAL, "[%PLUGIN_NAME%] %NAME% issued global command: %COMMAND%" },
            { ResourceManager.COMMAND_ISSUED_GLOBAL, "[%PLUGIN_NAME%] %NAME% issued local command: %COMMAND%" },
            { ResourceManager.USER_ACCESS_DENIED, "%CHAT_PREFIX%%DARK_AQUA%You don't have permission to use this command" },
            { ResourceManager.ADMIN_CFG_CHARGE_FOR_SHOP, "%BLUE%charge-for-shop: %WHITE%[true/false] Charge for shop creation toggle." },
            { ResourceManager.ADMIN_CFG_GLOBAL_SHOP, "%BLUE%global-shop: %WHITE%[true/false] Global Shop toggle." },
            { ResourceManager.ADMIN_CFG_SHOP_WIDTH, "%BLUE%shop-width: %WHITE%[number] Default shop width." },
            { ResourceManager.ADMIN_CFG_REPORT_STATS, "%BLUE%report-stats: %WHITE%[number] Allows anonymous usage statistics." },
            { ResourceManager.ADMIN_CFG_MAX_HEIGHT, "%BLUE%max-height: %WHITE%[number] Maximum height of a shop." },
            { ResourceManager.ADMIN_CFG_MAX_WIDTH, "%BLUE%max-width: %WHITE%[number] Maximum width of a shop." },
            { ResourceManager.ADMIN_CFG_SHOPS_TRANS_MAX_SIZE, "%BLUE%shops-transaction-max-size: %WHITE%[number] Maximum transactions to store in memory used by notifications." },
            { ResourceManager.ADMIN_CFG_SHOPS_COST, "%BLUE%shops-cost: %WHITE%[decimal] Cost of creating a shop." },
            { ResourceManager.ADMIN_CFG_FIND_MAX_DISTANCE, "%BLUE%find-max-distance: %WHITE%[number] Maximum distance between player and shop for /shop find command." },
            { ResourceManager.ADMIN_CFG_SHOPS_PER_PLAYER, "%BLUE%shops-per-player: %WHITE%[number] Maximum number of shops a player can own." },
            { ResourceManager.ADMIN_CFG_SHOP_HEIGHT, "%BLUE%shop-height: %WHITE%[number] Default height of shops." },
            { ResourceManager.ADMIN_CFG_DEBUG, "%BLUE%debug: %WHITE%[true/false] Debugging mode, may degrade performance or be annoying." },
            { ResourceManager.ADMIN_CFG_MAX_DAMAGE, "%BLUE%max-damage: %WHITE%[number] Number between 0 and 100, percent of damage an item is allowed to have, not remaining durablity." },
            { ResourceManager.ADMIN_CFG_MOVE_COST, "%BLUE%move-cost: %WHITE%[decimal] Cost for moving a shop." },
            { ResourceManager.ADMIN_CFG_SHOP_NOTIFICATION_TIMER, "%BLUE%shop-notification-timer: %WHITE%[number] Interval between transaction notifications." },
            { ResourceManager.ADMIN_CFG_SHOP_NOTIFICATION, "%BLUE%shop-transaction-notice: %WHITE%[true/false] Notification of transactions to shop owners." },
            { ResourceManager.ADMIN_CFG_CHAT_MAX_LINES, "%BLUE%chat-max-lines: %WHITE%[number] Number of lines displayed in paginated output." },
            { ResourceManager.ADMIN_CFG_LOG_TRANSACTIONS, "%BLUE%log-transactions: %WHITE%[true/false] Transaction logging to transactions.log file." },
            { ResourceManager.INVALID_VALUE, "Invalid value" },
    };

}