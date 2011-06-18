package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.ResourceManager;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.InventoryItem;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.PlayerData;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.Transaction;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CommandShopBuy extends Command {

    public CommandShopBuy(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopBuy(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
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
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP));
                return true;
            }

            // Check Permissions
            if ((!canUseCommand(CommandTypes.BUY) && !isGlobal) || (!canUseCommand(CommandTypes.GLOBAL_BUY) && isGlobal)) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
                return true;
            }

            // buy (player only command)
            Pattern pattern = Pattern.compile("(?i)buy$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    return false;
                }
                ItemInfo item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                return shopBuy(shop, item, 0);
            }

            // buy all (player only command)
            matcher.reset();
            pattern = Pattern.compile("(?i)buy\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                ItemStack itemStack = player.getItemInHand();
                if (itemStack == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_NO_ITEM_IN_HAND));
                    return true;
                }
                ItemInfo item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                int count;
                if (shop.isUnlimitedStock()) {
                    // get player avail space
                    count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
                } else {
                    // use shop stock
                    count = shop.getItem(item.name).getStock();
                }

                return shopBuy(shop, item, count);
            }

            // buy int all
            matcher.reset();
            pattern = Pattern.compile("(?i)buy\\s+(\\d+)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                ItemInfo item = Search.itemById(id);
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                int count;
                if (shop.isUnlimitedStock()) {
                    // get player avail space
                    count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
                } else {
                    // use shop stock
                    count = shop.getItem(item.name).getStock();
                }
                if (count < 1) {
                    //
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                    return true;
                }
                return shopBuy(shop, item, count);
            }

            // buy int:int all
            matcher.reset();
            pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                int id = Integer.parseInt(matcher.group(1));
                short type = Short.parseShort(matcher.group(2));
                ItemInfo item = Search.itemById(id, type);
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                int count;
                if (shop.isUnlimitedStock()) {
                    // get player avail space
                    count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
                } else {
                    // use shop stock
                    count = shop.getItem(item.name).getStock();
                }
                if (count < 1) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                    return true;
                }
                return shopBuy(shop, item, count);
            }

            // buy name, ... all
            matcher.reset();
            pattern = Pattern.compile("(?i)buy\\s+(.*)\\s+all$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                String itemName = matcher.group(1);
                ItemInfo item = Search.itemByName(itemName);
                if (item == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                    return true;
                }
                int count;
                if (shop.isUnlimitedStock()) {
                    // get player avail space
                    count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
                } else {
                    // use shop stock
                    count = shop.getItem(item.name).getStock();
                }
                if (count < 1) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                    return true;
                }
                return shopBuy(shop, item, count);
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
            return true;
        }

        // Command matching

        // buy int
        Pattern pattern = Pattern.compile("(?i)buy\\s+(\\d+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo item = Search.itemById(id);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopBuy(shop, item, 0);
        }

        // buy int int
        matcher.reset();
        pattern = Pattern.compile("(?i)buy\\s+(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            int count = Integer.parseInt(matcher.group(2));
            ItemInfo item = Search.itemById(id);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            if (count < 1) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                return true;
            }
            return shopBuy(shop, item, count);
        }

        // buy int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopBuy(shop, item, 0);
        }

        // buy int:int int
        matcher.reset();
        pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo item = Search.itemById(id, type);
            int count = Integer.parseInt(matcher.group(3));
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            if (count < 1) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                return true;
            }
            return shopBuy(shop, item, count);
        }

        // buy name, ... int
        matcher.reset();
        pattern = Pattern.compile("(?i)buy\\s+(.*)\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            int count = Integer.parseInt(matcher.group(2));
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            if (count < 1) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name } ));
                return true;
            }
            return shopBuy(shop, item, count);
        }

        // buy name, ...
        matcher.reset();
        pattern = Pattern.compile("(?i)buy\\s+(.*)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String itemName = matcher.group(1);
            ItemInfo item = Search.itemByName(itemName);
            if (item == null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_ITEM_NOT_FOUND));
                return true;
            }
            return shopBuy(shop, item, 0);
        }

        // Show sell help
        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_USAGE, new String[] { "%COMMANDLABEL%" }, new Object[] { commandLabel }));
        return true;
    }

    private boolean shopBuy(Shop shop, ItemInfo item, int amount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_PLAYERS_ONLY));
            return false;
        }

        Player player = (Player) sender;
        InventoryItem invItem = shop.getItem(item.name);
        PlayerData pData = plugin.getPlayerData().get(player.getName());

        // check if the shop is buying that item
        if (invItem == null || invItem.getBuyPrice() == 0) {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.name }));
            return false;
        } else if (invItem.getStock() == 0 && !shop.isUnlimitedStock()) {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_SOLD_OUT, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.name }));
            return false;
        }

        // check if the item has a price, or if this is a shop owner
        if (invItem.getBuyPrice() == 0 && !isShopController(shop)) {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.name }));
            return false;
        }

        // if amount = 0, assume single stack size
        if (amount == 0) {
            amount = invItem.getBuySize();
        }

        int totalAmount;
        totalAmount = invItem.getStock();

        if (totalAmount == 0 && !shop.isUnlimitedStock()) {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_HAS_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { totalAmount, item.name }));
            return true;
        }

        if (amount < 0) {
            amount = 0;
        }

        if (shop.isUnlimitedStock()) {
            totalAmount = amount;
        }
        if (amount > totalAmount) {
            amount = totalAmount - (totalAmount % invItem.getBuySize());
            if (!shop.isUnlimitedStock()) {
                player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_HAS_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { totalAmount, item.name }));
            }
        } else if (amount % invItem.getBuySize() != 0) {
            amount = amount - (amount % invItem.getBuySize());
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_HAS_QTY, new String[] { "%BUNDLESIZE%", "%AMOUNT%" }, new Object[] { invItem.getBuySize(), amount }));
        }

        // check how many items the user has room for
        int freeSpots = 0;
        for (ItemStack thisSlot : player.getInventory().getContents()) {
            if (thisSlot == null || thisSlot.getType() == Material.AIR) {
                freeSpots += 64;
                continue;
            }
            if (thisSlot.getTypeId() == item.typeId && thisSlot.getDurability() == item.subTypeId) {
                freeSpots += 64 - thisSlot.getAmount();
            }
        }

        // Calculate the amount the player can store
        if (amount > freeSpots) {
            amount = freeSpots - (freeSpots % invItem.getBuySize());
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_SHOP_HAS_QTY, new String[] { "%AMOUNT%" }, new Object[] { amount }));
        }

        // calculate cost
        int bundles = amount / invItem.getBuySize();
        double itemPrice = invItem.getBuyPrice();
        // recalculate # of items since may not fit cleanly into bundles
        amount = bundles * invItem.getBuySize();
        double totalCost = bundles * itemPrice;
        
        if (shop.isUnlimitedMoney()) {
            if (!pData.chargePlayer(player.getName(), totalCost)) {
                player.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_UNEXPECTED_MONEY_ISSUE));
                return true;
            }
        } 
        // try to pay the shop owner
        else if (!isShopController(shop)) {
            if (!pData.payPlayer(player.getName(), shop.getOwner(), totalCost)) {
                // player doesn't have enough money
                // get player's balance and calculate how many it can buy
                double playerBalance = pData.getBalance(player.getName());
                int bundlesCanAford = (int) Math.floor(playerBalance / itemPrice);
                totalCost = bundlesCanAford * itemPrice;
                amount = bundlesCanAford * invItem.getSellSize();
                
                if(bundlesCanAford == 0) {
                    player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_PLAYER_AFFORD_NONE, new String[] { "%ITEMNAME%" }, new Object[] { item.name }));
                    return true;
                } else {
                    player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_PLAYER_AFFORD_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { bundlesCanAford, item.name }));
                }
                if (!pData.payPlayer(player.getName(), shop.getOwner(), totalCost)) {
                    player.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_UNEXPECTED_MONEY_ISSUE));
                    return true;
                }
            }
        }

        if (!shop.isUnlimitedStock()) {
            shop.removeStock(item.name, amount);
        }
        if (isShopController(shop)) {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_REMOVED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { amount, item.name }));
        } else {
            player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_BUY_PURCHASED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%", "%COST%" }, new Object[] { amount, item.name, plugin.getEconManager().format(totalCost) }));
        }

        // log the transaction
        int stock = invItem.getStock();
        int startStock = stock + amount;
        if (shop.isUnlimitedStock()) {
            startStock = 0;
        }
        plugin.getShopManager().logItems(player.getName(), shop.getName(), "buy-item", item.name, amount, startStock, stock);
        shop.addTransaction(new Transaction(Transaction.Type.Sell, player.getName(), item.name, amount, totalCost));

        givePlayerItem(item.toStack(), amount);
        plugin.getShopManager().saveShop(shop);

        //update any sign in this shop with that value.
        shop.updateSigns(shop.getSignSet());
        
        return true;
    }
}