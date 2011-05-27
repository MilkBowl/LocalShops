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
import com.milkbukkit.localshops.objects.GlobalShop;
import com.milkbukkit.localshops.objects.Shop;

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
            
        GlobalShop shop = plugin.getShopManager().getGlobalShopByWorld(worldName);
        // Check if null
        if (shop == null) {
            sender.sendMessage("No global shop on " + worldName + " to unlink!");
            return true;
        }
        
        // Validate Worlds size on Shop
        shop.removeWorld(worldName);
        plugin.getShopManager().removeGlobalShopByWorld(worldName);
        sender.sendMessage(ChatColor.DARK_AQUA + "Unlinked " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " from " + ChatColor.WHITE + worldName);
        return true;
    }
}
