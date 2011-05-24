package net.centerleft.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.centerleft.localshops.ItemInfo;
import net.centerleft.localshops.LocalShops;
import net.centerleft.localshops.Search;
import net.centerleft.localshops.Shop;

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
                sender.sendMessage("You are not in a shop!");
                return false;
            }

            // Check Permissions
            if (!canUseCommand(CommandTypes.REMOVE)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
                return false;
            }            

            // Check if Player can Modify
            if (!isShopController(shop)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to set this.");
                player.sendMessage(ChatColor.DARK_AQUA + "The current shop owner is " + ChatColor.WHITE + shop.getOwner());
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
                ItemInfo item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
                if(item == null) {
                    sender.sendMessage("Could not find an item.");
                    return false;
                }
                return shopRemove(shop, item);
            }

        } else {
            sender.sendMessage("Console is not implemented yet.");
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
            sender.sendMessage(ChatColor.DARK_AQUA + "The shop is not selling " + ChatColor.WHITE + item.name);
            return true;
        }

        sender.sendMessage(ChatColor.WHITE + item.name + ChatColor.DARK_AQUA + " removed from the shop. ");
        if (!shop.isUnlimitedStock()) {
            int amount = shop.getItem(item.name).getStock();

            if (sender instanceof Player) {
                Player player = (Player) sender;
                // log the transaction
                plugin.getShopManager().logItems(player.getName(), shop.getName(), "remove-item", item.name, amount, amount, 0);

                givePlayerItem(item.toStack(), amount);
                player.sendMessage("" + ChatColor.WHITE + amount + ChatColor.DARK_AQUA + " have been returned to your inventory");
            }
        }

        shop.removeItem(item.name);
        plugin.getShopManager().saveShop(shop);        

        return true;
    }
}