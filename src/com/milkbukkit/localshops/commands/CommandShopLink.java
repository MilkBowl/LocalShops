/**
 * 
 */
package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Shop;

/**
 * @author sleaker
 *
 */
public class CommandShopLink extends Command {

    public CommandShopLink(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopLink(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        String worldName = null;
        Shop shop = null;

        // Check Permissions
        if (!canUseCommand(CommandTypes.ADMIN)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }
        
        // check if "link shopId worldName
        Pattern pattern = Pattern.compile("(?i)\\w+\\s+([A-Za-z0-9\\-]+)\\s+(\\w+)$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String key = matcher.group(1);
            worldName = matcher.group(2);
            shop = plugin.getShopManager().getGlobalShopFromShortUuid(key);
            if (shop == null) {
                sender.sendMessage("Could not find a global shop that matches id: " + key);
                return true;
            }
        } else if(sender instanceof Player) {
            Player player = (Player) sender;
            matcher.reset();
            pattern = Pattern.compile("(?i)\\w+\\s+(.+)");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                worldName = matcher.group(1);
                shop = getCurrentShop(player);
                if (shop == null) {
                    sender.sendMessage("A global shop does not exist for your world, you must create one first before you can link!");
                    return true;
                }
            } else {
                // Send usage to player
                sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link [worldname] " + ChatColor.DARK_AQUA + "- Link a global shop from this world to worldname");
                sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link [shopid] [worldname] " + ChatColor.DARK_AQUA + "- Link a global shop with id to worldname");
                return true;
            }
        } else {
            // Send usage to Console
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link [shopid] [worldname] " + ChatColor.DARK_AQUA + "- Link a global shop with id to worldname");
            return true;
        }
        
        if (shop.isGlobal()) {
            Shop wShop = plugin.getShopManager().getGlobalShop(worldName);
            if (wShop != null) {
                sender.sendMessage(worldName + " already has a global shop with id: " + wShop.getShortUuidString());
                return true;
            }
            
            shop.getWorldsSet().add(worldName);
            sender.sendMessage("Added " + shop.getName() + " as a global shop for " + worldName);
            return true;
        } else {
            sender.sendMessage("Shop with id " + shop.getShortUuidString() + " is a local shop. Unable to link to a world");
            return true;
        }
    }

}
