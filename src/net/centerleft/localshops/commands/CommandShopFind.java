package net.centerleft.localshops.commands;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.centerleft.localshops.Config;
import net.centerleft.localshops.InventoryItem;
import net.centerleft.localshops.ItemInfo;
import net.centerleft.localshops.LocalShops;
import net.centerleft.localshops.Search;
import net.centerleft.localshops.Shop;
import net.centerleft.localshops.ShopLocation;
import net.centerleft.localshops.comparator.EntryValueComparator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandShopFind extends Command {

    public CommandShopFind(LocalShops plugin, String commandLabel, CommandSender sender, String command) {
        super(plugin, commandLabel, sender, command);
    }
    
    public CommandShopFind(LocalShops plugin, String commandLabel, CommandSender sender, String[] command) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
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
        List<Shop> shops = plugin.getShopData().getAllShops();
        for (Shop shop : shops) {
            // Check that its the current world
            if (!playerWorld.equals(shop.getWorld())) {
                continue;
            }

            // Determine distance, if too far away ignore
            double distance = calculateDistance(playerLoc, shop.getLocationCenter());
            if (Config.FIND_MAX_DISTANCE > 0 && distance > Config.FIND_MAX_DISTANCE) {
                continue;
            }

            // Check shop has item & is either buying or selling it
            if (!shop.containsItem(found)) {
                continue;
            }

            // This shop is valid, add to list
            foundShops.put(shop.getUuid(), distance);
        }
        if (Config.GLOBAL_SHOP && Config.GLOBAL_SHOP_UUID != null) {
            foundShops.put(Config.GLOBAL_SHOP_UUID, 0D);
        }
        
        @SuppressWarnings("unchecked")
        SortedSet<Entry<UUID, Double>> entries = new TreeSet<Entry<UUID, Double>>(new EntryValueComparator());
        entries.addAll(foundShops.entrySet());

        if (entries.size() > 0) {
            int count = 0;
            sender.sendMessage(ChatColor.DARK_AQUA + "Showing " + ChatColor.WHITE + "4" + ChatColor.DARK_AQUA + " of " + ChatColor.WHITE + foundShops.size() + ChatColor.DARK_AQUA + " shop(s) having " + ChatColor.WHITE + found.name);
            for (Entry<UUID, Double> entry : entries) {
                UUID uuid = entry.getKey();
                double distance = entry.getValue();
                Shop shop = plugin.getShopData().getShop(uuid);
                InventoryItem item = shop.getItem(found.name);

                String sellPrice;
                if (item.getBuyPrice() <= 0 || item.getBuySize() <= 0 || item.getStock() == 0) {
                    sellPrice = "--";
                } else {
                    sellPrice = String.format("%.2f", (item.getBuyPrice() / item.getBuySize()));
                }

                String buyPrice;
                if (item.getSellPrice() <= 0 || item.getSellSize() <= 0 || item.getStock() > item.getMaxStock()) {
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
