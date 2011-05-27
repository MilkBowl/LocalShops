package com.milkbukkit.localshops.commands;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.ShopSign;
import com.milkbukkit.localshops.util.GenericFunctions;

public class CommandShopSet extends Command {

    public CommandShopSet(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopSet(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        // Check Permissions
        if (!canUseCommand(CommandTypes.SET)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }

        log.info(String.format("[%s] Command issued: %s", plugin.pdfFile.getName(), command));

        // Parse Arguments
        if (command.matches("(?i)set\\s+sell.*")) {
            return shopSetSell();
        } else if (command.matches("(?i)set\\s+buy.*")) {
            return shopSetBuy();
        } else if (command.matches("(?i)set\\s+max.*")) {
            return shopSetMax();
        } else if (command.matches("(?i)set\\s+unlimited.*")) {
            return shopSetUnlimited();
        } else if (command.matches("(?i)set\\s+manager.*")) {
            return shopSetManager();
        } else if (command.matches("(?i)set\\s+minbalance.*")) {
            return shopSetMinBalance();
        } else if (command.matches("(?i)set\\s+notification.*")) {
            return shopSetNotification();
        } else if (command.matches("(?i)set\\s+owner.*")) {
            return shopSetOwner();
        } else if (command.matches("(?i)set\\s+name.*")) {
            return shopSetName();
        } else if (command.matches("(?i)set\\s+dynamic.*")) {
            return shopSetDynamic();
        } else {
            return shopSetHelp();
        }
    }

    private boolean shopSetBuy() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);

            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // Command matching

        // set sell int int int
        Pattern pattern = Pattern.compile("(?i)set\\s+buy\\s+(\\d+)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            double price = Double.parseDouble(matcher.group(2));
            int size = Integer.parseInt(matcher.group(7));
            return shopSetBuy(shop, item, price, size);
        }

        // set sell int:int int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+buy\\s+(\\d+):(\\d+)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            double price = Double.parseDouble(matcher.group(3));
            int size = Integer.parseInt(matcher.group(8));
            return shopSetBuy(shop, item, price, size);
        }

        // set sell int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+buy\\s+(\\d+)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            double price = Double.parseDouble(matcher.group(2));
            return shopSetBuy(shop, item, price);
        }

        // set sell int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+buy\\s+(\\d+):(\\d+)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            double price = Double.parseDouble(matcher.group(3));
            return shopSetBuy(shop, item, price);
        }

        // set sell (chars) int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+buy\\s+(.*)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            double price = Double.parseDouble(matcher.group(2));
            int size = Integer.parseInt(matcher.group(7));
            return shopSetBuy(shop, item, price, size);
        }

        // set sell (chars) int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+buy\\s+(.*)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            double price = Double.parseDouble(matcher.group(2));
            return shopSetBuy(shop, item, price);
        }

        // show set sell usage
        sender.sendMessage("   " + "/" + commandLabel + " set buy [item name] [price] <bundle size>");
        return true;
    }

    private boolean shopSetBuy(Shop shop, ItemInfo item, double price) {
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        // Warn about negative items
        if (price < 0) {
            sender.sendMessage("[WARNING] This shop will loose money with negative values!");
        }

        // Set new values
        shop.setItemSellPrice(item.name, price);

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Result
        sender.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is now buying " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " for " + ChatColor.WHITE + plugin.getEconManager().format(price));
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);
        
        return true;
    }

    private boolean shopSetBuy(Shop shop, ItemInfo item, double price, int size) {
        // Check for valid item
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }
        
        // Check for valid size
        if(size < 1) {
            sender.sendMessage("Bundles must be at least 1!");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        // Warn about negative items
        if (price < 0) {
            sender.sendMessage("[WARNING] This shop will loose money with negative values!");
        }
        if (size < 0) {
            sender.sendMessage("[ERROR] Stacks cannot be negative!");
            return true;
        }

        // Set new values
        shop.setItemSellAmount(item.name, size);
        shop.setItemSellPrice(item.name, price);

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Result
        sender.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is now buying "+ item.name + ChatColor.DARK_AQUA + " for " + ChatColor.WHITE + plugin.getEconManager().format(price) + ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Bundle: " + size + ChatColor.DARK_AQUA + "]");
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);
        
        return true;
    }

    private boolean shopSetSell() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // Command matching

        // set buy int int int
        Pattern pattern = Pattern.compile("(?i)set\\s+sell\\s+(\\d+)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            double price = Double.parseDouble(matcher.group(2));
            int size = Integer.parseInt(matcher.group(7));
            return shopSetSell(shop, item, price, size);
        }

        // set buy int:int int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+sell\\s+(\\d+):(\\d+)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            double price = Double.parseDouble(matcher.group(3));
            int size = Integer.parseInt(matcher.group(8));
            return shopSetSell(shop, item, price, size);
        }

        // set buy int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+sell\\s+(\\d+)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            double price = Double.parseDouble(matcher.group(2));
            return shopSetSell(shop, item, price);
        }

        // set buy int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+sell\\s+(\\d+):(\\d+)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            double price = Double.parseDouble(matcher.group(3));
            return shopSetSell(shop, item, price);
        }

        // set buy (chars) int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+sell\\s+(.*)\\s+("+DECIMAL_REGEX+")\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            double price = Double.parseDouble(matcher.group(2));
            int size = Integer.parseInt(matcher.group(7));
            return shopSetSell(shop, item, price, size);
        }

        // set buy (chars) int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+sell\\s+(.*)\\s+("+DECIMAL_REGEX+")");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            double price = Double.parseDouble(matcher.group(2));
            return shopSetSell(shop, item, price);
        }

        // show set buy usage
        sender.sendMessage("   " + "/" + commandLabel + " set sell [item name] [price] <bundle size>");
        return true;
    }

    private boolean shopSetSell(Shop shop, ItemInfo item, double price, int size) {
        // Check for valid item
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }
        
        // Check for valid size
        if(size < 1) {
            sender.sendMessage("Bundles must be at least 1!");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        // Warn about negative items
        if (price < 0) {
            sender.sendMessage("[WARNING] This shop will loose money with negative values!");
        }
        if (size < 0) {
            sender.sendMessage("[ERROR] Stacks cannot be negative!");
            return true;
        }

        // Set new values
        shop.setItemBuyAmount(item.name, size);
        shop.setItemBuyPrice(item.name, price);

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Result
        sender.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is now selling "+ item.name + ChatColor.DARK_AQUA + " for " + ChatColor.WHITE + plugin.getEconManager().format(price) + ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Bundle: " + size + ChatColor.DARK_AQUA + "]");
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);
        return true;
    }

    private boolean shopSetSell(Shop shop, ItemInfo item, double price) {
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        // Warn about negative items
        if (price < 0) {
            sender.sendMessage("[WARNING] This shop will loose money with negative values!");
        }

        // Set new values
        shop.setItemBuyPrice(item.name, price);

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Result
        sender.sendMessage(ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " now sells for "+ ChatColor.WHITE + plugin.getEconManager().format(price));
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);
        return true;
    }

    private boolean shopSetMax() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // Command matching

        // shop set max int int
        Pattern pattern = Pattern.compile("(?i)set\\s+max\\s+(\\d+)\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            int max = Integer.parseInt(matcher.group(2));
            return shopSetMax(shop, item, max);
        }

        // shop set max int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+max\\s+(\\d+):(\\d+)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            int max = Integer.parseInt(matcher.group(3));
            return shopSetMax(shop, item, max);
        }

        // shop set max chars int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+max\\s+(.*)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            int max = Integer.parseInt(matcher.group(2));
            return shopSetMax(shop, item, max);
        }

        // show set buy usage
        sender.sendMessage("   " + "/" + commandLabel + " set max [item name] [max number]");
        return true;
    }

    private boolean shopSetMax(Shop shop, ItemInfo item, int max) {
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        // Check negative values
        if (max < 0) {
            sender.sendMessage("Only positive values allowed");
            return true;
        }

        // Set new values
        shop.setItemMaxStock(item.name, max);
        
        //Update our signs for this item
        shop.updateSigns(item.name);
        
        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Message
        sender.sendMessage(item.name + " maximum stock is now " + max);

        return true;
    }

    private boolean shopSetUnlimited() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.ADMIN)) {
                player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be a shop admin to do this.");
                return true;
            }            
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // Command matching

        // shop set max int int
        Pattern pattern = Pattern.compile("(?i)set\\s+max\\s+(\\d+)\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            int max = Integer.parseInt(matcher.group(2));
            return shopSetMax(shop, item, max);
        }

        // shop set unlimited money
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+unlimited\\s+money");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            shop.setUnlimitedMoney(!shop.isUnlimitedMoney());
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Unlimited money was set to " + ChatColor.WHITE + shop.isUnlimitedMoney());
            plugin.getShopManager().saveShop(shop);
            return true;
        }

        // shop set unlimited stock
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+unlimited\\s+stock");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            shop.setUnlimitedStock(!shop.isUnlimitedStock());
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Unlimited stock was set to " + ChatColor.WHITE + shop.isUnlimitedStock());
           //Update signs after setting unlimited stock
            for (ShopSign sign : shop.getSignSet() )
                shop.updateSign(sign);
            
            plugin.getShopManager().saveShop(shop);
            return true;
        }

        // show set buy usage
        sender.sendMessage("   " + "/" + commandLabel + " set unlimited money");
        sender.sendMessage("   " + "/" + commandLabel + " set unlimited stock");
        return true;
    }

    private boolean shopSetManager() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!shop.getOwner().equalsIgnoreCase(player.getName())) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // set manager +name -name ...
        Pattern pattern = Pattern.compile("(?i)set\\s+manager\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String names = matcher.group(1);
            String[] args = names.split(" ");

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.matches("\\+.*")) {
                    // add manager
                    shop.addManager(arg.replaceFirst("\\+", ""));
                } else if (arg.matches("\\-.*")) {
                    // remove manager
                    shop.removeManager(arg.replaceFirst("\\-", ""));
                } 
            }

            // Save Shop
            plugin.getShopManager().saveShop(shop);

            notifyPlayers(shop, new String[] { ChatColor.DARK_AQUA + "The shop managers have been updated. The current managers are:", GenericFunctions.join(shop.getManagers(), ", ") } );
            return true;            
        }

        // show set manager usage
        sender.sendMessage("   " + "/" + commandLabel + " set manager +[playername] -[playername2]");
        return true;
    }

    private boolean shopSetNotification() {
        Shop shop = null;

        // Get current shop
        if(sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if(shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!shop.getOwner().equalsIgnoreCase(player.getName())) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to set this.");
                sender.sendMessage(ChatColor.DARK_AQUA + " The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // set notification
        shop.setNotification(!shop.getNotification());

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Output
        sender.sendMessage(String.format(ChatColor.DARK_AQUA + "Notices for " + ChatColor.WHITE + "%s" + ChatColor.DARK_AQUA + " are now " + ChatColor.WHITE + "%s", shop.getName(), shop.getNotification() ? "on" : "off"));
        return true;
    }

    private boolean shopSetMinBalance() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!shop.getOwner().equalsIgnoreCase(player.getName())) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to set this.");
                sender.sendMessage(ChatColor.DARK_AQUA + " The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // set minbalance amount
        Pattern pattern = Pattern.compile("(?i)set\\s+minbalance\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            double min = Double.parseDouble(matcher.group(1));
            shop.setMinBalance(min);
            // Save Shop
            plugin.getShopManager().saveShop(shop);

            sender.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " now has a minimum balance of "+ ChatColor.WHITE + plugin.getEconManager().format(min));
            return true;
        }

        sender.sendMessage(" " + "/" + commandLabel + " set minbalance [amount]");
        return true;
    }

    private boolean shopSetOwner() {
        Shop shop = null;
        boolean reset = false;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;
            }

            // Check if Player can Modify
            if (!canUseCommand(CommandTypes.ADMIN) && !shop.getOwner().equalsIgnoreCase(player.getName())) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to set this.");
                sender.sendMessage(ChatColor.DARK_AQUA + "  The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }

            if (!canUseCommand(CommandTypes.SET_OWNER) && !canUseCommand(CommandTypes.ADMIN)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
                return true;
            }

            if(!canUseCommand(CommandTypes.ADMIN)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + shop.getName() + " is no longer buying items.");
                reset = true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }

        // set owner name
        Pattern pattern = Pattern.compile("(?i)set\\s+owner\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            if (!canUseCommand(CommandTypes.SET_OWNER)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You do not have permission to do this.");
                return true;
            }  else if ( !canCreateShop(name) ) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "that player already has the maximum number of shops!");
                return true;
            } else {
                shop.setOwner(name);

                // Save Shop
                plugin.getShopManager().saveShop(shop);

                // Reset buy prices (0)
                if(reset) {
                    Iterator<InventoryItem> it = shop.getItems().iterator();
                    while(it.hasNext()) {
                        InventoryItem item = it.next();
                        item.setSellPrice(0);
                    }
                }

                notifyPlayers(shop, new String[] { LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + shop.getName() + " is now under new management!  The new owner is " + ChatColor.WHITE + shop.getOwner() } );
                return true;
            }
        }

        sender.sendMessage("   " + "/" + commandLabel + " set owner [player name]");
        return true;
    }

    private boolean shopSetName() {
        Shop shop = null;

        // Get current shop
        if(sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            shop = getCurrentShop(player);
            if(shop == null) {
                sender.sendMessage("You are not in a shop!");
                return true;                
            }

            // Check if Player can Modify  
            if(!canModifyShop(shop)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to set this.");
                sender.sendMessage(ChatColor.DARK_AQUA + "  The current shop owner is " + ChatColor.WHITE + shop.getOwner());                
                return true;
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;            
        }

        Pattern pattern = Pattern.compile("(?i)set\\s+name\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1).trim();
            shop.setName(name);
            plugin.getShopManager().saveShop(shop);
            notifyPlayers(shop, new String[] { LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Shop name is now " + ChatColor.WHITE + shop.getName() } );
            return true;
        }

        sender.sendMessage("   " + "/" + commandLabel + " set name [shop name]");
        return true;
    }
    private boolean shopSetDynamic(Shop shop, ItemInfo item) {
        if (item == null) {
            sender.sendMessage("Item was not found.");
            return true;
        }

        // Check if Shop has item
        if (!shop.containsItem(item)) {
            // nicely message user
            sender.sendMessage(String.format("This shop does not carry %s!", item.name));
            return true;
        }

        //Set new value
        shop.setItemDynamic(item.name);

        // Save Shop
        plugin.getShopManager().saveShop(shop);

        // Send Result
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Dynamic pricing for " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " is now " + shop.isItemDynamic(item.name));
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);
        return true;    
    }

    private boolean shopSetDynamic() {
        log.info("shopSetDynamic");
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;
            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage("You are not in a shop!");
                return false;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.ADMIN)) {
                player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You do not have permission to set dynamic shops.");
                return false;
            }            
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return false;
        }

        // Command matching


        //shop set dynamic int
        Pattern pattern = Pattern.compile("(?i)set\\s+dynamic\\s+(\\d+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            return shopSetDynamic(shop, item);
        }
        // set dynamic int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+dynamic\\s+(\\d+):(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            return shopSetDynamic(shop, item);
        }
        matcher.reset();
        //shop set dynamic (char)
        pattern = Pattern.compile("(?i)set\\s+dynamic\\s+(.*)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo item = Search.itemByName(name);
            return shopSetDynamic(shop, item);
        }

        // shop set dynamic
        matcher.reset();
        pattern = Pattern.compile("(?i)set\\s+dynamic");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            shop.setDynamicPrices(!shop.isDynamicPrices());
            sender.sendMessage(ChatColor.DARK_AQUA + "Dynamic pricing for " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " was set to " + ChatColor.WHITE + shop.isDynamicPrices());
            plugin.getShopManager().saveShop(shop);
            return true;
        }


        // show set dynamic usage
        sender.sendMessage("   " + "/" + commandLabel + " set dynamic");
        sender.sendMessage("   " + "/" + commandLabel + " set dynamic item");
        sender.sendMessage("   " + "/" + commandLabel + " set dynamic id");
        sender.sendMessage("   " + "/" + commandLabel + " set dynamic id:id");
        return true;
    }

    private boolean shopSetHelp() {
        // Display list of set commands & return
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "The following set commands are available: ");
        sender.sendMessage("   " + "/" + commandLabel + " set buy [item name] [price] <bundle size>");
        sender.sendMessage("   " + "/" + commandLabel + " set sell [item name] [price] <bundle size>");
        sender.sendMessage("   " + "/" + commandLabel + " set max [item name] [max number]");
        sender.sendMessage("   " + "/" + commandLabel + " set manager +[playername] -[playername2]");
        sender.sendMessage("   " + "/" + commandLabel + " set minbalance [amount]");
        sender.sendMessage("   " + "/" + commandLabel + " set name [shop name]");
        sender.sendMessage("   " + "/" + commandLabel + " set owner [player name]");
        if (canUseCommand(CommandTypes.ADMIN)) {
            sender.sendMessage("   " + "/" + commandLabel + " set unlimited money");
            sender.sendMessage("   " + "/" + commandLabel + " set unlimited stock");
            sender.sendMessage("   " + "/" + commandLabel + " set dynamic <id>");
        }
        return true;
    }
}