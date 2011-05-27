/**
 * 
 */
package com.milkbukkit.localshops.commands;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;

/**
 * @author sleaker
 *
 */
public class CommandShopUnlink extends Command {

    public CommandShopUnlink(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopUnlink(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        String worldName = null;

        // Check Permissions
        if (!canUseCommand(CommandTypes.ADMIN)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }
        
        Pattern pattern = Pattern.compile("(?i)(unlink|removeloc)\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            worldName = matcher.group(2);
        } else if(sender instanceof Player) {
            Player player = (Player) sender;
            worldName = player.getWorld().getName();
        } else {
            // Show link help (will only show to console)
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " unlink [worldname] " + ChatColor.DARK_AQUA + "- Unlink a global shop from a specific world.");
        }
            
        Shop shop = plugin.getShopManager().getGlobalShop(worldName);
        // Check if null
        if (shop == null) {
            sender.sendMessage("No global shop on " + worldName + " to unlink!");
            return true;
        }
        
        // Check if shop is not global
        if (!shop.isGlobal()) {
            sender.sendMessage(shop.getName() + " was not a global shop!");
            return true;
        }
        
        // Validate Worlds size on Shop
        Set<String> worldsSet = shop.getWorldsSet();
        if (worldsSet.size() > 1) {
            worldsSet.remove(worldName);
            sender.sendMessage(ChatColor.DARK_AQUA + "Unlinked " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " from " + ChatColor.WHITE + worldName);
            return true;
        } else {
            // only one world, needs to be destroyed!
            sender.sendMessage(ChatColor.DARK_AQUA + "Failed to unlink " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " from " + ChatColor.WHITE + worldName);
            sender.sendMessage(ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " is only in " + ChatColor.WHITE + worldName + ChatColor.DARK_AQUA + " and must be destroyed");
            return true;
        }
    }
}
