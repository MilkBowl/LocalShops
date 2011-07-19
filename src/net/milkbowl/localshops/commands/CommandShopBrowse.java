/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
 * 
 */

package net.milkbowl.localshops.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.comparator.InventoryItemSortByName;
import net.milkbowl.localshops.objects.Item;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.vault.Vault;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
                return true;
            }

            // Check Permissions
            if ((!canUseCommand(PermType.BROWSE) && !isGlobal) || (isGlobal && !canUseCommand(PermType.GLOBAL_BROWSE))) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            }
            
            if (!shop.hasAccess(player)) {
            	sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
            	return true;
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
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
        List<Item> items = shop.getItems();
        Collections.sort(items, new InventoryItemSortByName());

        boolean buy = buySellorList.equalsIgnoreCase("buy");
        boolean sell = buySellorList.equalsIgnoreCase("sell");
        boolean list = buySellorList.equalsIgnoreCase("list");

        ArrayList<String> inventoryMessage = new ArrayList<String>();
        for (Item item : items) {

            String subMessage = "   " + item.getName();
            int maxStock = 0;

            // NOT list
            if (!list) {
                double price = 0;
                if (buy) {
                    // get buy price
                    price = shop.getItem(item).getBuyPrice();
                }
                if (sell) {
                    price = shop.getItem(item).getSellPrice();
                }
                if (price == 0) {
                    continue;
                }
                subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + Vault.getEconomy().format(price) + ChatColor.DARK_AQUA + "]";
                // get stack size
                if (sell) {
                    int stock = shop.getItem(item).getStock();
                    maxStock = shop.getItem(item).getMaxStock();

                    if (stock >= maxStock && !(maxStock == 0)) {
                        continue;
                    }
                }
            }

            // get stock
            int stock = shop.getItem(item).getStock();
            if (buy) {
                if (stock == 0 && !shop.isUnlimitedStock())
                    continue;
            }
            if (!shop.isUnlimitedStock()) {
                subMessage += ChatColor.DARK_AQUA + " [" + ChatColor.WHITE + "Stock: " + stock + ChatColor.DARK_AQUA + "]";

                maxStock = shop.getItem(item).getMaxStock();
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