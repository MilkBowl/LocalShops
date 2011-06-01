package com.milkbukkit.localshops.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.comparator.InventoryItemSortByName;
import com.milkbukkit.localshops.objects.Shop;

public class CommandShopBrowse extends Command {

    public CommandShopBrowse(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopBrowse(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;
            
            shop = getCurrentShop(player);
            if (shop == null || (isGlobal && !Config.getGlobalShopsEnabled())) {
                sender.sendMessage("plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP)");
                return true;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.BROWSE)) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
                return true;
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
            return true;
        }

        if (shop.getItems().size() == 0) {
            sender.sendMessage(String.format("%s currently does not stock any items.", shop.getName()));
            return true;
        }

        int pageNumber = 1;

        // browse
        Pattern pattern = Pattern.compile("(?i)(bro|brow|brows|browse)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            printInventory(shop, "list", pageNumber);
            return true;
        }

        // browse (buy|sell) pagenum
        matcher.reset();
        pattern = Pattern.compile("(?i)bro.*\\s+(buy|sell|info)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String type = matcher.group(1);
            pageNumber = Integer.parseInt(matcher.group(2));
            printInventory(shop, type, pageNumber);
            return true;
        }

        // browse (buy|sell)
        matcher.reset();
        pattern = Pattern.compile("(?i)bro.*\\s+(buy|sell|info)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String type = matcher.group(1);
            printInventory(shop, type, pageNumber);
            return true;
        }

        // browse int
        matcher.reset();
        pattern = Pattern.compile("(?i)bro.*\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            pageNumber = Integer.parseInt(matcher.group(1));
            printInventory(shop, "list", pageNumber);
            return true;
        }

        return false;
    }
    
    /**
     * Prints shop inventory with default page # = 1
     * 
     * @param shop
     * @param player
     * @param buySellorList
     
    private void printInventory(Shop shop, String buySellorList) {
        printInventory(shop, buySellorList, 1);
    }
    */
    /**
     * Prints shop inventory list. Takes buy, sell, or list as arguments for
     * which format to print.
     * 
     * @param shop
     * @param player
     * @param buySellorList
     * @param pageNumber
     */
    private void printInventory(Shop shop, String buySellorList, int pageNumber) {
        String inShopName = shop.getName();
        List<InventoryItem> items = shop.getItems();
        Collections.sort(items, new InventoryItemSortByName());

        boolean buy = buySellorList.equalsIgnoreCase("buy");
        boolean sell = buySellorList.equalsIgnoreCase("sell");
        boolean list = buySellorList.equalsIgnoreCase("list");

        ArrayList<String> inventoryMessage = new ArrayList<String>();
        for (InventoryItem item : items) {

            String subMessage = "   " + item.getInfo().name;
            int maxStock = 0;

            // NOT list
            if (!list) {
                double price = 0;
                if (buy) {
                    // get buy price
                    price = item.getBuyPrice();
                }
                if (sell) {
                    price = item.getSellPrice();
                }
                if (price == 0) {
                    continue;
                }
                subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + plugin.getEconManager().format(price) + ChatColor.DARK_AQUA + "]";
                // get stack size
                int stack = 0;
                if (buy) {
                    stack = item.getBuySize();
                }
                if (sell) {
                    stack = item.getSellSize();
                    int stock = item.getStock();
                    maxStock = item.getMaxStock();

                    if (stock >= maxStock && !(maxStock == 0)) {
                        continue;
                    }
                }
                if (stack > 1) {
                    subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Bundle: " + stack + ChatColor.DARK_AQUA + "]";
                }
            }

            // get stock
            int stock = item.getStock();
            if (buy) {
                if (stock == 0 && !shop.isUnlimitedStock())
                    continue;
            }
            if (!shop.isUnlimitedStock()) {
                subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Stock: " + stock + ChatColor.DARK_AQUA + "]";

                maxStock = item.getMaxStock();
                if (maxStock > 0) {
                    subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Max Stock: " + maxStock + ChatColor.DARK_AQUA + "]";
                }
            }

            inventoryMessage.add(subMessage);
        }

        String message = ChatColor.DARK_AQUA + "The shop " + ChatColor.WHITE + inShopName + ChatColor.DARK_AQUA;

        if (buy) {
            message += " is selling:";
        } else if (sell) {
            message += " is buying:";
        } else {
            message += " trades in: ";
        }

        message += " (Page " + pageNumber + " of " + (int) Math.ceil((double) inventoryMessage.size() / (double) Config.getChatMaxLines()) + ")";

        sender.sendMessage(message);

        if(inventoryMessage.size() <= (pageNumber - 1) * Config.getChatMaxLines()) {
            sender.sendMessage(String.format("%s does not have this many pages!", shop.getName()));
            return;
        }

        int amount = (pageNumber > 0 ? (pageNumber - 1) * Config.getChatMaxLines() : 0);
        for (int i = amount; i < amount + Config.getChatMaxLines(); i++) {
            if (inventoryMessage.size() > i) {
                sender.sendMessage(inventoryMessage.get(i));
            }
        }

        if (!list) {
            String buySell = (buy ? "buy" : "sell");
            message = ChatColor.DARK_AQUA + "To " + buySell + " an item on the list type: " + ChatColor.WHITE + "/" + commandLabel + " " + buySell + " ItemName [amount]";
            sender.sendMessage(message);
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Type " + ChatColor.WHITE + "/" + commandLabel + " browse buy"  + ChatColor.DARK_AQUA + " or " + ChatColor.WHITE + "/" + commandLabel + " browse sell");
            sender.sendMessage(ChatColor.DARK_AQUA + "to see details about price and quantity.");
        }
    }

}