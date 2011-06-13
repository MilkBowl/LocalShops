package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.ResourceManager;

import org.bukkit.command.CommandSender;


public class CommandAdminSet extends Command {

    public CommandAdminSet(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandAdminSet(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        // Check Permissions - Server Permissions
        if (!canUseCommand(CommandTypes.ADMIN_SERVER)) {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
            return true;
        }

        // Parse Arguments

        // Get Charge for shop
        Pattern pattern = Pattern.compile("(?i)(charge-for-shop)$");
        Matcher matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_CHARGE_FOR_SHOP));
            sender.sendMessage(key + "=" + (Config.getShopChargeCreate() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Charge for shop
        matcher.reset();
        pattern = Pattern.compile("(?i)(charge-for-shop)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setShopChargeCreate(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }
        
        // Get Global Shop
        matcher.reset();
        pattern = Pattern.compile("(?i)(global-shop)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_GLOBAL_SHOP));
            sender.sendMessage(key + "=" + (Config.getGlobalShopsEnabled() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Global Shop
        matcher.reset();
        pattern = Pattern.compile("(?i)(global-shop)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setGlobalShopsEnabled(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }
        
        // Get Shop width
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-width)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOP_WIDTH));
            sender.sendMessage(key + "=" + Config.getShopSizeDefWidth());
            return true;
        }
        
        // Set Shop width
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-width)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopSizeDefWidth(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }
        
        // Get Report stats
        matcher.reset();
        pattern = Pattern.compile("(?i)(report-stats)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_REPORT_STATS));
            sender.sendMessage(key + "=" + (Config.getSrvReport() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Report stats
        matcher.reset();
        pattern = Pattern.compile("(?i)(report-stats)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setSrvReport(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Max height
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-height)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_MAX_HEIGHT));
            sender.sendMessage(key + "=" + Config.getShopSizeMaxHeight());
            return true;
        }
        
        // Set Max height
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-height)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopSizeMaxHeight(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }
        
        // Get Max width
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-width)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_MAX_WIDTH));
            sender.sendMessage(key + "=" + Config.getShopSizeMaxWidth());
            return true;
        }
        
        // Set Max width
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-width)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopSizeMaxWidth(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shop transaction max size
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-max-size)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOPS_TRANS_MAX_SIZE));
            sender.sendMessage(key + "=" + Config.getShopTransactionMaxSize());
            return true;
        }
        
        // Set Shop transaction max size
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-max-size)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopTransactionMaxSize(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shop cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-cost)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOPS_COST));
            sender.sendMessage(key + "=" + Config.getShopChargeCreateCost());
            return true;
        }
        
        // Set Shop cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-cost)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                double x = Double.parseDouble(value);
                Config.setShopChargeCreateCost(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Find max distance
        matcher.reset();
        pattern = Pattern.compile("(?i)(find-max-distance)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_FIND_MAX_DISTANCE));
            sender.sendMessage(key + "=" + Config.getFindMaxDistance());
            return true;
        }
        
        // Set Find max distance
        matcher.reset();
        pattern = Pattern.compile("(?i)(find-max-distance)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setFindMaxDistance(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shops per player
        matcher.reset();
        pattern = Pattern.compile("(?i)(shops-per-player)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOPS_PER_PLAYER));
            sender.sendMessage(key + "=" + Config.getPlayerMaxShops());
            return true;
        }
        
        // Set Shops per player
        matcher.reset();
        pattern = Pattern.compile("(?i)(shops-per-player)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setPlayerMaxShops(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shop height
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-height)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOP_HEIGHT));
            sender.sendMessage(key + "=" + Config.getShopSizeDefHeight());
            return true;
        }
        
        // Set Shop height
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-height)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopSizeDefHeight(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Debug
        matcher.reset();
        pattern = Pattern.compile("(?i)(debug)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_DEBUG));
            sender.sendMessage(key + "=" + (Config.getSrvDebug() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Debug
        matcher.reset();
        pattern = Pattern.compile("(?i)(debug)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setSrvDebug(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Max damage
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-damage)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_MAX_DAMAGE));
            sender.sendMessage(key + "=" + Config.getItemMaxDamage());
            return true;
        }
        
        // Set Max damage
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-damage)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setItemMaxDamage(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Move cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(move-cost)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_MOVE_COST));
            sender.sendMessage(key + "=" + Config.getShopChargeMoveCost());
            return true;
        }
        
        // Set Move cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(move-cost)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                double x = Double.parseDouble(value);
                Config.setShopChargeMoveCost(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shop notification timer
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-notification-timer)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION_TIMER));
            sender.sendMessage(key + "=" + Config.getShopTransactionNoticeTimer());
            return true;
        }
        
        // Set Shop notification timer
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-notification-timer)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setShopTransactionNoticeTimer(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Shop transaction notice
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-notice)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_SHOP_NOTIFICATION));
            sender.sendMessage(key + "=" + (Config.getShopTransactionNotice() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Shop transaction notice
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-notice)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setShopTransactionNotice(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }
        
        // Get Chat max lines
        matcher.reset();
        pattern = Pattern.compile("(?i)(chat-max-lines)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_CHAT_MAX_LINES));
            sender.sendMessage(key + "=" + Config.getChatMaxLines());
            return true;
        }
        
        // Set Chat max lines
        matcher.reset();
        pattern = Pattern.compile("(?i)(chat-max-lines)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                int x = Integer.parseInt(value);
                Config.setChatMaxLines(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Get Log transactions
        matcher.reset();
        pattern = Pattern.compile("(?i)(log-transactions)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_ADM_SET_CFG_LOG_TRANSACTIONS));
            sender.sendMessage(key + "=" + (Config.getSrvLogTransactions() ? plugin.getResourceManager().getString(ResourceManager.BASE_TRUE) : plugin.getResourceManager().getString(ResourceManager.BASE_FALSE)));
            return true;
        }
        
        // Set Log transactions
        matcher.reset();
        pattern = Pattern.compile("(?i)(log-transactions)\\s+(.*)");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            try {
                boolean x = value.equalsIgnoreCase(plugin.getResourceManager().getString(ResourceManager.BASE_TRUE));
                Config.setSrvLogTransactions(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_INVALID_VALUE));
            }
            return true;
        }

        // Default return
        return adminHelp();
    }

    private boolean adminHelp() {
        // Display list of set commands & return
        sender.sendMessage("   " + "/" + commandLabel + " charge-for-shop <value>");
        sender.sendMessage("   " + "/" + commandLabel + " chat-max-lines <value>");
        sender.sendMessage("   " + "/" + commandLabel + " debug <value>");
        sender.sendMessage("   " + "/" + commandLabel + " find-max-distance <value>");
        sender.sendMessage("   " + "/" + commandLabel + " global-shop <value>");
        sender.sendMessage("   " + "/" + commandLabel + " log-transactions <value>");
        sender.sendMessage("   " + "/" + commandLabel + " max-damage <value>");
        sender.sendMessage("   " + "/" + commandLabel + " max-height <value>");
        sender.sendMessage("   " + "/" + commandLabel + " max-width <value>");
        sender.sendMessage("   " + "/" + commandLabel + " move-cost <value>");
        sender.sendMessage("   " + "/" + commandLabel + " report-stats <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-cost <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-height <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-width <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-notification-timer <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-transaction-max-size <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shop-transaction-notice <value>");
        sender.sendMessage("   " + "/" + commandLabel + " shops-per-player <value>");
        return true;
    }
}