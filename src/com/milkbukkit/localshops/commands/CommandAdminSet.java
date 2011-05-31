package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;

public class CommandAdminSet extends Command {

    public CommandAdminSet(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandAdminSet(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        // Check Permissions
        if (!canUseCommand(CommandTypes.ADMIN)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }

        log.info(String.format("[%s] Command issued: %s", plugin.getDescription().getName(), command));

        // Parse Arguments

        // Get Charge for shop
        Pattern pattern = Pattern.compile("(?i)(charge-for-shop)$");
        Matcher matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "charge-for-shop:" + ChatColor.WHITE + " [true/false] Charge for shop creation toggle.");
            sender.sendMessage(key + "=" + Config.getShopChargeCreate());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setShopChargeCreate(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
            }
            return true;
        }
        
        // Get Global Shop
        matcher.reset();
        pattern = Pattern.compile("(?i)(global-shop)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "global-shop:" + ChatColor.WHITE + " [true/false] Global Shop toggle.");
            sender.sendMessage(key + "=" + Config.getGlobalShopsEnabled());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setGlobalShopsEnabled(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
            }
            return true;
        }
        
        // Get Shop width
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-width)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shop-width:" + ChatColor.WHITE + " [number] Default shop width.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }
        
        // Get Report stats
        matcher.reset();
        pattern = Pattern.compile("(?i)(report-stats)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "report-stats:" + ChatColor.WHITE + " [number] Allows anonymous usage statistics.");
            sender.sendMessage(key + "=" + Config.getSrvReport());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setSrvReport(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Max height
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-height)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "max-height:" + ChatColor.WHITE + " [number] Maximum height of a shop.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }
        
        // Get Max width
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-width)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "max-width:" + ChatColor.WHITE + " [number] Maximum width of a shop.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shop transaction max size
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-max-size)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shops-transaction-max-size:" + ChatColor.WHITE + " [number] Maximum transactions to store in memory used by notifications.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shop cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-cost)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shops-cost:" + ChatColor.WHITE + " [decimal] Cost of creating a shop.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Find max distance
        matcher.reset();
        pattern = Pattern.compile("(?i)(find-max-distance)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "find-max-distance:" + ChatColor.WHITE + " [number] Maximum distance between player and shop for /shop find command.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shops per player
        matcher.reset();
        pattern = Pattern.compile("(?i)(shops-per-player)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shops-per-player:" + ChatColor.WHITE + " [number] Maximum number of shops a player can own.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shop height
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-height)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shop-height:" + ChatColor.WHITE + " [number] Default height of shops.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Debug
        matcher.reset();
        pattern = Pattern.compile("(?i)(debug)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "debug:" + ChatColor.WHITE + " [true/false] Debugging mode, may degrade performance or be annoying.");
            sender.sendMessage(key + "=" + Config.getSrvDebug());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setSrvDebug(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Max damage
        matcher.reset();
        pattern = Pattern.compile("(?i)(max-damage)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "max-damage:" + ChatColor.WHITE + " [number] Number between 0 and 100, percent of damage an item is allowed to have, not remaining durablity.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Move cost
        matcher.reset();
        pattern = Pattern.compile("(?i)(move-cost)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "move-cost:" + ChatColor.WHITE + " [decimal] Cost for moving a shop.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shop notification timer
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-notification-timer)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shop-notification-timer:" + ChatColor.WHITE + " [number] Interval between transaction notifications.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Shop transaction notice
        matcher.reset();
        pattern = Pattern.compile("(?i)(shop-transaction-notice)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "shop-transaction-notice:" + ChatColor.WHITE + " [true/false] Notification of transactions to shop owners.");
            sender.sendMessage(key + "=" + Config.getShopTransactionNotice());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setShopTransactionNotice(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
            }
            return true;
        }
        
        // Get Chat max lines
        matcher.reset();
        pattern = Pattern.compile("(?i)(chat-max-lines)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "chat-max-lines:" + ChatColor.WHITE + " [number] Number of lines displayed in paginated output.");
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
                sender.sendMessage("Invalid value");
            }
            return true;
        }

        // Get Log transactions
        matcher.reset();
        pattern = Pattern.compile("(?i)(log-transactions)$");
        matcher = pattern.matcher(command);
        if(matcher.find()) {
            String key = matcher.group(1);
            sender.sendMessage(ChatColor.BLUE + "log-transactions:" + ChatColor.WHITE + " [true/false] Transaction logging to transactions.log file.");
            sender.sendMessage(key + "=" + Config.getSrvLogTransactions());
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
                boolean x = Boolean.parseBoolean(value);
                Config.setSrvLogTransactions(x);
                sender.sendMessage(key + "=" + value);
            } catch(Exception e) {
                sender.sendMessage("Invalid value");
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