package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.Transaction;
import com.milkbukkit.localshops.objects.Shop;

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
                sender.sendMessage("plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP)");
                return true;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.SELL)) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
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
                ItemInfo item = null;
                int amount = countItemsInInventory(player.getInventory(), itemStack);
                if(LocalShops.getItemList().isDurable(itemStack)) {
                    item = Search.itemById(itemStack.getTypeId());
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " is too damaged to sell!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                return shopSell(shop, item, amount);
            }

            // sell (player only command)
            matcher.reset();
            pattern = Pattern.compile("(?i)sell$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    return true;
                }
                ItemInfo item = null;
                int amount = itemStack.getAmount();
                if(LocalShops.getItemList().isDurable(itemStack)) {
                    item = Search.itemById(itemStack.getTypeId());
                    if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "Your " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " is too damaged to sell!");
                        sender.sendMessage(ChatColor.DARK_AQUA + "Items must be damanged less than " + ChatColor.WHITE + Config.getItemMaxDamage() + "%");
                        return true;
                    }
                } else {
                    item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                }
                if(item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                return shopSell(shop, item, amount);
            }
        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
            return true;
        }

        // Command matching

        // sell int
        Pattern pattern = Pattern.compile("(?i)sell\\s+(\\d+)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 0);
        }

        // sell int int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            ItemInfo item = Search.itemById(id);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // sell int all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+)\\s+all");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            ItemInfo item = Search.itemById(id);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }        

        // sell int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 0);
        }

        // sell int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // sell int:int all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(\\d+):(\\d+)\\s+all");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }        

        // shop sell name, ... int
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)\\s+(\\d+)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            int count = Integer.parseInt(matcher.group(2));
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, count);
        }

        // shop sell name, ... all
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)\\s+all");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            Player player = (Player) sender;
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            int count = countItemsInInventory(player.getInventory(), item.toStack());
            return shopSell(shop, item, count);
        }        

        // shop sell name, ...
        matcher.reset();
        pattern = Pattern.compile("(?i)sell\\s+(.*)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            if(item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopSell(shop, item, 1);
        }

        // Show sell help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell [itemname] [number] " + ChatColor.DARK_AQUA + "- Sell this item.");
        return true;
    }
    
    private boolean shopSell(Shop shop, ItemInfo item, int amount) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("/shop sell can only be used for players!");
            return false;
        }

        Player player = (Player) sender;
        InventoryItem invItem = shop.getItem(item.name);
        PlayerData pData = plugin.getPlayerData().get(player.getName());

        // check if the shop is buying that item
        if (!shop.containsItem(item) || shop.getItem(item.name).getSellPrice() == 0) {
            player.sendMessage(ChatColor.DARK_AQUA + "Sorry, " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is not buying " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " right now.");
            return false;
        }

        // check how many items the player has
        int playerInventory = countItemsInInventory(player.getInventory(), item.toStack());
        if (amount < 0) {
            amount = 0;
        }

        // check if the amount to add is okay
        if (amount > playerInventory) {
            player.sendMessage(ChatColor.DARK_AQUA + "You only have " + ChatColor.WHITE + playerInventory + ChatColor.DARK_AQUA + " in your inventory that can be added.");
            amount = playerInventory;
        }

        // check if the shop has a max stock level set
        if (invItem.getMaxStock() != 0 && !shop.isUnlimitedStock()) {
            if (invItem.getStock() >= invItem.getMaxStock()) {
                player.sendMessage(ChatColor.DARK_AQUA + "Sorry, " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is not buying any more " + ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " right now.");
                return false;
            }

            if (amount > (invItem.getMaxStock() - invItem.getStock())) {
                amount = invItem.getMaxStock() - invItem.getStock();
            }
        }

        // calculate cost
        int bundles = amount / invItem.getSellSize();

        if (bundles == 0 && amount > 0) {
            player.sendMessage(ChatColor.DARK_AQUA + "The minimum number to sell is  " + ChatColor.WHITE + invItem.getSellSize());
            return false;
        }

        double itemPrice = invItem.getSellPrice();
        // recalculate # of items since may not fit cleanly into bundles
        // notify player if there is a change
        if (amount % invItem.getSellSize() != 0) {
            player.sendMessage(ChatColor.DARK_AQUA + "The bundle size is  " + ChatColor.WHITE + invItem.getSellSize() + ChatColor.DARK_AQUA + " order reduced to " + ChatColor.WHITE + bundles * invItem.getSellSize());
        }
        amount = bundles * invItem.getSellSize();
        double totalCost = bundles * itemPrice;

        // try to pay the player for order
        if (shop.isUnlimitedMoney()) {
            pData.payPlayer(player.getName(), totalCost);
        } else {
            if (!isShopController(shop)) {
                log.info(String.format("From: %s, To: %s, Cost: %f", shop.getOwner(), player.getName(), totalCost));
                if (!pData.payPlayer(shop.getOwner(), player.getName(), totalCost)) {
                    // lshop owner doesn't have enough money
                    // get shop owner's balance and calculate how many it can
                    // buy
                    double shopBalance = plugin.getPlayerData().get(player.getName()).getBalance(shop.getOwner());
                    // the current shop balance must be greater than the minimum
                    // balance to do the transaction.
                    if (shopBalance <= shop.getMinBalance() || shopBalance < invItem.getSellPrice()) {
                        player.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is broke!");
                        return true;
                    }
                    // Added Min Balance calculation for maximum items the shop can afford
                    int bundlesCanAford = (int) Math.floor(shopBalance - shop.getMinBalance() / itemPrice);
                    totalCost = bundlesCanAford * itemPrice;
                    amount = bundlesCanAford * invItem.getSellSize();
                    player.sendMessage(ChatColor.DARK_AQUA + shop.getName() + " could only afford " + ChatColor.WHITE + bundlesCanAford + ChatColor.DARK_AQUA + " bundles.");
                    if (!pData.payPlayer(shop.getOwner(), player.getName(), totalCost)) {
                        player.sendMessage(ChatColor.DARK_AQUA + "Unexpected money problem: could not complete sale.");
                        return true;
                    }
                }
            }
        }

        if (!shop.isUnlimitedStock()) {
            shop.addStock(item.name, amount);
        }

        if (isShopController(shop)) {
            player.sendMessage(ChatColor.DARK_AQUA + "You added " + ChatColor.WHITE + amount + " " + item.name + ChatColor.DARK_AQUA + " to the shop");
        } else {
            player.sendMessage(ChatColor.DARK_AQUA + "You sold " + ChatColor.WHITE + amount + " " + item.name + ChatColor.DARK_AQUA + " and gained " + ChatColor.WHITE + plugin.getEconManager().format(totalCost));
        }

        // log the transaction
        int itemInv = invItem.getStock();
        int startInv = itemInv - amount;
        if (startInv < 0) {
            startInv = 0;
        }
        plugin.getShopManager().logItems(player.getName(), shop.getName(), "sell-item", item.name, amount, startInv, itemInv);
        shop.addTransaction(new Transaction(Transaction.Type.Buy, player.getName(), item.name, amount, totalCost));

        removeItemsFromInventory(player.getInventory(), item.toStack(), amount);
        plugin.getShopManager().saveShop(shop);
        
        //update any sign in this shop with that value.
        shop.updateSigns(item.name);

        return true;
    }

}