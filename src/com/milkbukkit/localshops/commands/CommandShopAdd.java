package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.objects.ItemInfo;
import com.milkbukkit.localshops.objects.Shop;

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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP));
                return false;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.ADD)) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
                return false;
            }            

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_MUST_BE_SHOP_OWNER));
                player.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CURR_OWNER_IS, new String[] { "%OWNER%" }, new String[] { shop.getOwner() }));
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
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_TOO_DAM, new String[] { "%ITEMNAME%" }, new String[] { item.name }));
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_DMG_LESS_THAN, new String[] { "%DAMAGEVALUE%" }, new String[] { String.valueOf(Config.getItemMaxDamage()) }));
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_TOO_DAM, new String[] { "%ITEMNAME%" }, new String[] { item.name }));
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_DMG_LESS_THAN, new String[] { "%DAMAGEVALUE%" }, new String[] { String.valueOf(Config.getItemMaxDamage()) }));
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return false;
                }
                int count = countItemsInInventory(player.getInventory(), item.toStack());
                return shopAdd(shop, item, count);
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return false;
            }
            return shopAdd(shop, item, 0);
        }

        // Show add help
        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_USAGE, new String[] { "%CMDLABEL%" }, new String[] { commandLabel }));
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
                log.info(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_USAGE, new String[] { "%AMOUNT%", "%ITEMNAME%", "%SHOPNAME%" }, new Object[] { amount, item, shop }));
            } else {
                // Nag player
                log.info(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_INSUFFICIENT_INV, new String[] { "%ITEMCOUNT%" }, new Object[] { playerItemCount }));
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_UNLIM_STOCK, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.name }));
                return true;
            }

            // Check if amount to be added is 0 (no point adding 0)
            if (amount == 0) {
                // nicely message user
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_ALREADY_HAS, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.name }));
                return true;
            }
        }

        // Add item to shop if needed
        if (!shop.containsItem(item)) {
          //Autoset item as dynamic if adding to a dynamic shop
          shop.addItem(item.typeId, item.subTypeId, 0, 1, 0, 1, 0, 0, shop.isDynamicPrices());     
        }

        // Check stock settings, add stock if necessary
        if (shop.isUnlimitedStock()) {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_SUCCESS, new String[] { "%ITEMNAME%" }, new Object[] { item.name }));
        } else {
            shop.addStock(item.name, amount);
            shop.updateSigns(item.name);
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_STOCK_SUCCESS, new String[] { "%ITEMNAME%", "%STOCK%" }, new Object[] { item.name, shop.getItem(item.name).getStock() }));
        }
        
        if(amount == 0) {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_READY0, new String[] { "%ITEMNAME%" }, new Object[] { item.name }));
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_READY1, new String[] { "%ITEMNAME%" }, new Object[] { item.name }));
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_ADD_READY2, new String[] { "%ITEMNAME%" }, new Object[] { item.name }));
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