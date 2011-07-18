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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CommandShopRemove extends Command {

    public CommandShopRemove(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopRemove(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;
            
            shop = getCurrentShop(player);
            if (shop == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
                return false;
            }

            // Check Permissions
            if (!canUseCommand(PermType.REMOVE)) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return false;
            }            

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_MUST_BE_SHOP_OWNER));
                player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CURR_OWNER_IS, new String[] { "%OWNER%" }, new String[] { shop.getOwner() }));
                return true;
            }

            // remove (player only command)
            Pattern pattern = Pattern.compile("(?i)remove$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    return false;
                }
                ItemInfo item = Search.itemByStack(itemStack);
                if(item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                    return false;
                }
                return shopRemove(shop, item);
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
            return false;
        }

        // Command matching

        // remove int
        Pattern pattern = Pattern.compile("(?i)remove\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            return shopRemove(shop, item);
        }

        // remove int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)remove\\s+(\\d+):(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            return shopRemove(shop, item);
        }

        // remove name
        matcher.reset();
        pattern = Pattern.compile("(?i)remove\\s+(.*)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            return shopRemove(shop, item);
        }        


        // Show usage
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " remove [itemname]" + ChatColor.DARK_AQUA + " - Stop selling item in shop.");
        return true;
    }
    
    private boolean shopRemove(Shop shop, ItemInfo item) {
        if (item == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Item not found.");
            return false;
        }

        if(!shop.containsItem(item)) {
            sender.sendMessage(ChatColor.DARK_AQUA + "The shop is not selling " + ChatColor.WHITE + item.getName());
            return true;
        }

        sender.sendMessage(ChatColor.WHITE + item.getName() + ChatColor.DARK_AQUA + " removed from the shop. ");
        if (!shop.isUnlimitedStock()) {
            int amount = shop.getItem(item.getName()).getStock();

            if (sender instanceof Player) {
                Player player = (Player) sender;
                // log the transaction
                plugin.getShopManager().logItems(player.getName(), shop.getName(), "remove-item", item.getName(), amount, amount, 0);

                givePlayerItem(item, amount);
                player.sendMessage("" + ChatColor.WHITE + amount + ChatColor.DARK_AQUA + " have been returned to your inventory");
            }
        }

        shop.removeItem(item.getName());
        shop.updateSigns(item.getName());
        plugin.getShopManager().saveShop(shop);        

        return true;
    }
}