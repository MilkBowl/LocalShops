package com.milkbukkit.localshops.commands;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
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
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.comparator.EntryValueComparator;
import com.milkbukkit.localshops.objects.GlobalShop;
import com.milkbukkit.localshops.objects.LocalShop;
import com.milkbukkit.localshops.objects.Shop;
import com.milkbukkit.localshops.objects.ShopLocation;
import com.milkbukkit.localshops.util.GenericFunctions;

public class CommandShopFind extends Command {

    public CommandShopFind(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public CommandShopFind(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        if(Config.getFindMaxDistance() == 0) {
            sender.sendMessage(String.format("[%s] Shop finding has been disabled on this server.", plugin.getDescription().getName()));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console is not implemented");
        }

        Player player = (Player) sender;

        // search
        Pattern pattern = Pattern.compile("(?i)find$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null) {
                return true;
            }
            ItemInfo found = null;
            if (LocalShops.getItemList().isDurable(itemStack)) {
                found = Search.itemById(itemStack.getTypeId());
            } else {
                found = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
            }
            if (found == null) {
                sender.sendMessage("Could not find an item.");
                return true;
            }
            return shopFind(player, found);
        }

        // search int
        matcher.reset();
        pattern = Pattern.compile("(?i)find\\s+(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            ItemInfo found = Search.itemById(id);
            if (found == null) {
                sender.sendMessage("Could not find an item.");
                return true;
            }
            return shopFind(player, found);
        }

        // search int:int
        matcher.reset();
        pattern = Pattern.compile("(?i)find\\s+(\\d+):(\\d+)$");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            short type = Short.parseShort(matcher.group(2));
            ItemInfo found = Search.itemById(id, type);
            if (found == null) {
                sender.sendMessage("Could not find an item.");
                return true;
            }
            return shopFind(player, found);
        }

        // search name
        matcher.reset();
        pattern = Pattern.compile("(?i)find\\s+(.*)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo found = Search.itemByName(name);
            if (found == null) {
                sender.sendMessage(String.format("No item was not found matching \"%s\"", name));
                return true;
            } else {
                return shopFind(player, found);
            }
        }

        // Show sell help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " find [itemname] " + ChatColor.DARK_AQUA + "- Find shops that buy or sell this item.");
        return true;
    }

    private boolean shopFind(Player player, ItemInfo found) {
        String playerWorld = player.getWorld().getName();
        ShopLocation playerLoc = new ShopLocation(player.getLocation());

        TreeMap<UUID, Double> foundShops = new TreeMap<UUID, Double>();
        List<Shop> shops = plugin.getShopManager().getAllShops();
        for (Shop shop : shops) {
            double distance = 0;
            
            // Check if global or local
            if(shop instanceof LocalShop) {
                LocalShop lShop = (LocalShop) shop;
                
                // Check that its the current world
                if (!playerWorld.equals(lShop.getWorld())) {
                    continue;
                }
                
                distance = GenericFunctions.calculateDistance(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), lShop.getLocationCenter().getX(), lShop.getLocationCenter().getY(), lShop.getLocationCenter().getZ());
            } else if(shop instanceof GlobalShop) {
                GlobalShop gShop = (GlobalShop) shop;
                
                // Check that its the current world
                if (!gShop.containsWorld(playerWorld)) {
                    continue;
                }
                
                distance = 0;
            }

            // Determine if distance is too far away && ignore
            if (Config.getFindMaxDistance() > 0 && distance > Config.getFindMaxDistance()) {
                continue;
            }

            // Check shop has item & is either buying or selling it
            if (!shop.containsItem(found)) {
                continue;
            }

            // This shop is valid, add to list
            foundShops.put(shop.getUuid(), distance);
        }

        @SuppressWarnings("unchecked")
        SortedSet<Entry<UUID, Double>> entries = new TreeSet<Entry<UUID, Double>>(new EntryValueComparator());
        entries.addAll(foundShops.entrySet());

        if (entries.size() > 0) {
            int count = 0;
            sender.sendMessage(ChatColor.DARK_AQUA + "Showing " + ChatColor.WHITE + foundShops.size() + ChatColor.DARK_AQUA + " shops having " + ChatColor.WHITE + found.name);
            for (Entry<UUID, Double> entry : entries) {
                UUID uuid = entry.getKey();
                double distance = entry.getValue();
                Shop shop = plugin.getShopManager().getLocalShop(uuid);
                InventoryItem item = shop.getItem(found.name);

                String sellPrice;
                if (item.getBuyPrice() <= 0 || item.getBuySize() <= 0 || (item.getStock() == 0 && !shop.isUnlimitedStock())) {
                    sellPrice = "--";
                } else {
                    sellPrice = String.format("%.2f", (item.getBuyPrice() / item.getBuySize()));
                }

                String buyPrice;
                if (item.getSellPrice() <= 0 || item.getSellSize() <= 0 || (item.getStock() > item.getMaxStock() && !shop.isUnlimitedStock() )) {
                    buyPrice = "--";
                } else {
                    buyPrice = String.format("%.2f", (item.getSellPrice() / item.getSellSize()));
                }

                if (buyPrice.equals("--") && sellPrice.equals("--")) {
                    continue;
                }

                sender.sendMessage(String.format(ChatColor.WHITE + "%s: " + ChatColor.GOLD + "selling for %s, " + ChatColor.GREEN + "buying for %s", shop.getName(), sellPrice, buyPrice));
                sender.sendMessage(String.format(ChatColor.WHITE + "  " + ChatColor.DARK_AQUA + "Currently " + ChatColor.WHITE + "%-2.0fm" + ChatColor.DARK_AQUA + " away with ID: " + ChatColor.WHITE + "%s", distance, shop.getShortUuidString()));

                count++;

                if (count == 4) {
                    break;
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "No shops were found having " + ChatColor.WHITE + found.name);
        }

        return true;
    }

}
