package com.milkbukkit.localshops.commands;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.objects.LocalShop;
import com.milkbukkit.localshops.objects.Shop;
import com.milkbukkit.localshops.util.GenericFunctions;

public class CommandShopInfo extends Command {

    public CommandShopInfo(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }
    
    public CommandShopInfo(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        Shop shop = null;

        // Get current shop
        if (sender instanceof Player) {
            // Get player & data
            Player player = (Player) sender;

            // info (player only command)
            Pattern pattern = Pattern.compile("(?i)info$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                shop = getCurrentShop(player);
                if (shop == null) {
                    sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP));
                    return false;
                }
            }

            // info id
            matcher.reset();
            pattern = Pattern.compile("(?i)info\\s+(.*)$");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                String input = matcher.group(1);
                shop = plugin.getShopManager().getShop(input);
                if (shop == null) {
                    sender.sendMessage("Could not find shop with ID " + input);
                    return false;
                }
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
            return false;
        }

        int managerCount = shop.getManagers().size();

        sender.sendMessage(String.format(ChatColor.DARK_AQUA + "Shop Info about " + ChatColor.WHITE + "\"%s\"" + ChatColor.DARK_AQUA + " ID: " + ChatColor.WHITE + "%s", shop.getName(), shop.getShortUuidString()));
        if(shop.getCreator().equalsIgnoreCase(shop.getOwner())) {
            if(managerCount == 0) {
                sender.sendMessage(String.format("  Owned & Created by %s with no managers.", shop.getCreator()));
            } else {
                sender.sendMessage(String.format("  Owned & Created by %s with %d managers.", shop.getCreator(), managerCount));
            }
        } else {
            if(managerCount == 0) {
                sender.sendMessage(String.format("  Owned by %s, created by %s with no managers.", shop.getOwner(), shop.getCreator()));
            } else {
                sender.sendMessage(String.format("  Owned by %s created by %s with %d managers.", shop.getOwner(), shop.getCreator(), managerCount));
            }            
        }
        if(managerCount > 0) {
            sender.sendMessage(String.format("  Managed by %s", GenericFunctions.join(shop.getManagers(), " ")));
        }

        if(command.matches("info\\s+full")) {
            sender.sendMessage(String.format("  Full Id: %s", shop.getUuid().toString()));
        }

        if(shop instanceof LocalShop) {
            LocalShop lShop = (LocalShop) shop;
            //TODO: Fix Me - needs to dump all locations for the shop not just one.
            sender.sendMessage(String.format("  Located at %s x %s in \"%s\"", lShop.getLocationA().toString(), lShop.getLocationB().toString(), lShop.getWorlds().toString()));
        }

        // Calculate values
        int sellCount = 0;
        int buyCount = 0;
        int worth = 0;

        Iterator<InventoryItem> it = shop.getItems().iterator();
        while(it.hasNext()) {
            InventoryItem i = it.next();
            if(i.getBuyPrice() > 0) {
                sellCount++;
                worth += (i.getStock()/i.getBuySize()) * i.getBuyPrice();
            }

            if(i.getSellPrice() > 0) {
                buyCount++;
            }
        }

        // Selling %d items & buying %d items
        sender.sendMessage(String.format("  Selling %d items & buying %d items", sellCount, buyCount));

        // Shop stock is worth %d coins
        sender.sendMessage(String.format("  Inventory worth %s", plugin.getEconManager().format(worth)));

        if(shop.isUnlimitedMoney() || shop.isUnlimitedStock()) {
            sender.sendMessage(String.format("  Shop %s unlimited money and %s unlimited stock.", shop.isUnlimitedMoney() ? "has" : "doesn't have", shop.isUnlimitedStock() ? "has" : "doesn't have"));
        }

        return true;
    }
}
