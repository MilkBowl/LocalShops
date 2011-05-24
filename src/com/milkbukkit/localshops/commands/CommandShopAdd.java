package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.Shop;

public class CommandShopAdd extends Command {

    public CommandShopAdd(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopAdd(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
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
                sender.sendMessage("You are not in a shop!");
                return false;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.ADD)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
                return false;
            }            

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
                return true;
            }

            // add (player only command)
            Pattern pattern = Pattern.compile("(?i)add$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    return false;
                }
                ItemInfo item = null;
                int amount = itemStack.getAmount();
                if(LocalShops.getItemList().isDurable(itemStack)) {
                    item = Search.itemById(itemStack.getTypeId());
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " is too damaged to add to stock!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                return shopAdd(shop, item, amount);
            }

            // add all (player only command)
            matcher.reset();
            pattern = Pattern.compile("(?i)add\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    return false;
                }
                ItemInfo item = null;
                if(LocalShops.getItemList().isDurable(itemStack)) {
                    item = Search.itemById(itemStack.getTypeId());
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " is too damaged to add to stock!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                int amount = countItemsInInventory(player.getInventory(), itemStack);
                return shopAdd(shop, item, amount);
            }

            // add int all
            matcher.reset();
            pattern = Pattern.compile("(?i)add\\s+(\\d+)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                ItemInfo item = Search.itemById(id);
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                int count = countItemsInInventory(player.getInventory(), item.toStack());
                return shopAdd(shop, item, count);
            }

            // add int:int all
            matcher.reset();
            pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                short type = Short.parseShort(matcher.group(2));
                ItemInfo item = Search.itemById(id, type);
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                int count = countItemsInInventory(player.getInventory(), item.toStack());
                return shopAdd(shop, item, count);
            }

            // shop add name, ... all
            matcher.reset();
            pattern = Pattern.compile("(?i)add\\s+(.*)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                String itemName = matcher.group(1);
                ItemInfo item = Search.itemByName(itemName);
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                int count = countItemsInInventory(player.getInventory(), item.toStack());
                return shopAdd(shop, item, count);
            }

        } else {
            sender.sendMessage("Console is not implemented yet.");
            return false;
        }

        // Command matching     

        // add int
        Pattern pattern = Pattern.compile("(?i)add\\s+(\\d+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }            
            return shopAdd(shop, item, 0);
        }

        // add int int
        matcher.reset();
        pattern = Pattern.compile("(?i)add\\s+(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            ItemInfo item = Search.itemById(id);
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }            
            return shopAdd(shop, item, count);
        }

        // add int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }
            return shopAdd(shop, item, 0);
        }

        // add int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }
            return shopAdd(shop, item, count);
        }

        // shop add name, ... int
        matcher.reset();
        pattern = Pattern.compile("(?i)add\\s+(.*)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            int count = Integer.parseInt(matcher.group(2));
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }
            return shopAdd(shop, item, count);
        }

        // shop add name, ...
        matcher.reset();
        pattern = Pattern.compile("(?i)add\\s+(.*)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            if(item == null) {
                sender.sendMessage("Could not find an item.");
                return false;
            }
            return shopAdd(shop, item, 0);
        }

        // Show buy help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " buy [itemname] [number] " + ChatColor.DARK_AQUA + "- Buy this item.");
        return true;
    }
    
    private boolean shopAdd(Shop shop, ItemInfo item, int amount) {
        Player player = null;

        // Assign in sender is a Player
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        // Calculate number of items player has
        if (player != null) {
            int playerItemCount = countItemsInInventory(player.getInventory(), item.toStack());
            // Validate Count
            if (playerItemCount >= amount) {
                // Perform add
                log.info(String.format("Add %d of %s to %s", amount, item, shop));
            } else {
                // Nag player
                sender.sendMessage(ChatColor.DARK_AQUA + "You only have " + ChatColor.WHITE + playerItemCount + ChatColor.DARK_AQUA + " in your inventory that can be added.");
                // amount = playerItemCount;
                return false;
            }

            // If ALL (amount == -1), set amount to the count the player has
            if (amount == -1) {
                amount = playerItemCount;
            }
        }

        // If shop contains item
        if (shop.containsItem(item)) {
            // Check if stock is unlimited
            if (shop.isUnlimitedStock()) {
                // nicely message user
                sender.sendMessage(String.format("%s has unlimited stock and already carries %s!", shop.getName(), item.name));
                return true;
            }

            // Check if amount to be added is 0 (no point adding 0)
            if (amount == 0) {
                // nicely message user
                sender.sendMessage(String.format("%s already carries %s!", shop.getName(), item.name));
                return true;
            }
        }

        // Add item to shop if needed
        if (!shop.containsItem(item)) {
            shop.addItem(item.typeId, item.subTypeId, 0, 1, 0, 1, 0, 0);
        }

        // Check stock settings, add stock if necessary
        if (shop.isUnlimitedStock()) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Succesfully added " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " to the shop.");
        } else {
            shop.addStock(item.name, amount);
            sender.sendMessage(ChatColor.DARK_AQUA + "Succesfully added " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " to the shop. Stock is now " + ChatColor.WHITE + shop.getItem(item.name).getStock());
        }
        
        if(amount == 0) {
            sender.sendMessage(ChatColor.DARK_AQUA + item.name + " is almost ready to be purchased or sold!");
            sender.sendMessage(ChatColor.DARK_AQUA + "Use " + ChatColor.WHITE + "\"/shop set sell " + item.name + " price bundle\"" + ChatColor.DARK_AQUA + " to sell this item!");
            sender.sendMessage(ChatColor.DARK_AQUA + "Use " + ChatColor.WHITE + "\"/shop set  buy " + item.name + " price bundle\"" + ChatColor.DARK_AQUA + " to  buy this item!");
        }

        // log the transaction
        if (player != null) {
            int itemInv = shop.getItem(item.name).getStock();
            int startInv = itemInv - amount;
            if (startInv < 0) {
                startInv = 0;
            }
            plugin.getShopManager().logItems(player.getName(), shop.getName(), "add-item", item.name, amount, startInv, itemInv);

            // take items from player only if shop doesn't have unlim stock
            if(!shop.isUnlimitedStock()) {
                removeItemsFromInventory(player.getInventory(), item.toStack(), amount);
            }
        }
        plugin.getShopManager().saveShop(shop);
        return true;
    }
}