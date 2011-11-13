/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.Item;
import net.milkbowl.localshops.objects.ShopRecord;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.Transaction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandShopSell extends Command {

    public CommandShopSell(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopSell(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
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

            // Check Permissions then check access list
            if ((!canUseCommand(PermType.SELL) && !isGlobal) || (!canUseCommand(PermType.GLOBAL_SELL) && isGlobal)) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            } else if (!plugin.getShopManager().hasAccess(shop, player)) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            }

            // sell all (player only command)
            Pattern pattern = Pattern.compile("(?i)sell\\s+all$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    sender.sendMessage("You must be holding an item, or specify an item.");
                    return true;
                }
                Item item = null;
                int amount = countItemsInInventory(player.getInventory(), itemStack);
                item = Search.itemByStack(itemStack);

                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                    return true;
                } else if (item.isDurable()) {
                    //Durability checks
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.getName() + ChatColor.DARK_AQUA + " is too damaged to sell!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                }
                return shopSell(shop, item, amount);
            }

            // sell (player only command)
            matcher.reset();
            pattern = Pattern.compile("(?i)sell$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();

                Item item = Search.itemByStack(itemStack);
                ;
                int amount = itemStack.getAmount();
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                    return true;
                } else if (item.isDurable()) {
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.getName() + ChatColor.DARK_AQUA + " is too damaged to sell!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                }
                return shopSell(shop, item, amount);
            }
        } else {
            sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
            return true;
        }

        // Command matching

        // sell int
        Pattern pattern = Pattern.compile("(?i)sell\\s+(\\d+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            Item item = Search.itemById(id);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 1);
        }

        // sell int int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            Item item = Search.itemById(id);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // sell int all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+)\\s+all$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            Item item = Search.itemById(id);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // sell int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            Item item = Search.itemById(id, type);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 1);
        }

        // sell int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            Item item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // sell int:int all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)\\s+all$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            Item item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // shop sell name, ... int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            Item item = Search.itemByName(itemName);
            int count = Integer.parseInt(matcher.group(2));
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // shop sell name, ... all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)\\s+all$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            Player player = (Player) sender;
            String itemName = matcher.group(1);
            Item item = Search.itemByName(itemName);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            int count = countItemsInInventory(player.getInventory(), item.toStack());
            return shopSell(shop, item, count);
        }

        // shop sell name, ...
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            Item item = Search.itemByName(itemName);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 1);
        }

        // Show sell help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell [itemname] [number] " + ChatColor.DARK_AQUA + "- Sell this item.");
        return true;
    }

    private boolean shopSell(Shop shop, Item item, int amount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("/shop sell can only be used for players!");
            return false;
        }
        //Default to 1 if we try to sell less than 1
        if (amount < 0) {
            amount = 1;
        }

        Player player = (Player) sender;
        ShopRecord invItem = shop.getItem(item);
        // check if the shop is buying that item
        if (invItem == null || invItem.getSellPrice() == 0) {
            player.sendMessage(ChatColor.DARK_AQUA + "Sorry, " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is not buying " + ChatColor.WHITE + item.getName() + ChatColor.DARK_AQUA + " right now.");
            return false;
        }

        int startStock = invItem.getStock();

        //get the amount we can actually sell to the shop
        amount = getSellAmount(player, amount, item, shop);
        if (amount <= 0) {
            return false;
        }

        double totalCost = amount * invItem.getBuyPrice();

        /**
         * Attempt the transaction - if it errors at this point then there is a serious issue.
         *
         *
         * Also we should NEVER attempt loop transaction sales as they are incredibly inefficient.
         */
        if (shop.isUnlimitedMoney() && !shop.getOwner().equals(player.getName())) {
            if (!plugin.getEcon().depositPlayer(player.getName(), totalCost).transactionSuccess()) {
                player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
                return true;
            }
        } else {
            if (!shop.getOwner().equals(player.getName())) {
                if (plugin.getEcon().withdrawPlayer(shop.getOwner(), totalCost).transactionSuccess()) {
                    if (!plugin.getEcon().depositPlayer(player.getName(), totalCost).transactionSuccess()) {
                        // Refund owner, send message
                        plugin.getEcon().depositPlayer(shop.getOwner(), totalCost);
                        player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
                        return true;
                    }
                } else {
                    player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
                    return true;
                }
            }
        }
        if (!shop.isUnlimitedStock()) {
            shop.addStock(item, amount);
        }

        removeItemsFromInventory(player.getInventory(), item.toStack(), amount);

        plugin.getShopManager().logItems(player.getName(), shop.getName(), "sell-item", item.getName(), amount, startStock, invItem.getStock());
        shop.addTransaction(new Transaction(Transaction.Type.Buy, player.getName(), item.getName(), amount, totalCost));

        // Message the player
        if (isShopController(shop)) {
            player.sendMessage(ChatColor.DARK_AQUA + "You added " + ChatColor.WHITE + amount + " " + item.getName() + ChatColor.DARK_AQUA + " to the shop");
        } else {
            player.sendMessage(ChatColor.DARK_AQUA + "You sold " + ChatColor.WHITE + amount + " " + item.getName() + ChatColor.DARK_AQUA + " and gained " + ChatColor.WHITE + plugin.getEcon().format(totalCost));
        }

        // Save the changes to the Shop
        plugin.getShopManager().saveShop(shop);

        //update any sign in this shop with that value.
        plugin.getShopManager().updateSigns(shop, item);

        return true;
    }

    private int getSellAmount(Player player, int amount, Item item, Shop shop) {

        int originalAmount = amount;

        //Reduce amount if the player doesn't even have enough to sell
        int playerInventory = countItemsInInventory(player.getInventory(), item.toStack());
        if (amount > playerInventory) {
            amount = playerInventory;
        }

        //Return with special amount if this is the shop owner,
        //honestly the shop owner should be using /add, but this is for compatibility.
        if (player.getName().equals(shop.getOwner()) && !shop.isUnlimitedStock()) {
            return amount;
        } else if (player.getName().equals(shop.getOwner()) && shop.isUnlimitedStock()) {
            return 0;
        }


        //Check how many the shop can buy
        if (!(shop.getItem(item).getMaxStock() == 0) && amount + shop.getItem(item).getStock() > shop.getItem(item).getMaxStock()) {
            amount = shop.getItem(item).getMaxStock() - shop.getItem(item).getStock();
            if (amount <= 0) {
                player.sendMessage(ChatColor.DARK_AQUA + "Sorry, " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is not buying any more " + ChatColor.WHITE + item.getName() + ChatColor.DARK_AQUA + " right now.");
                return amount;
            }
        }

        double totalPrice = shop.getItem(item).getBuyPrice() * amount;

        //Reduce amount the player can sell if the owner doesn't have enough money, and shop is not unlimited stock.
        if (!shop.isUnlimitedMoney() && totalPrice > plugin.getEcon().getBalance(shop.getOwner())) {
            amount = (int) Math.floor(plugin.getEcon().getBalance(shop.getOwner()) / shop.getItem(item).getBuyPrice());
        }

        //let our user know if there was any change in the amount
        if (amount != originalAmount) {
            player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_ORDER_REDUCED, new String[]{"%AMOUNT%"}, new Object[]{1, amount}));
        }

        return amount;
    }
}
