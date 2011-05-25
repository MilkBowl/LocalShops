/**
 * 
 */
package com.milkbukkit.localshops.commands;

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
        Shop shop = null;


        if (sender instanceof Player) {
            // Check Permissions
            if (!canUseCommand(CommandTypes.ADMIN)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
                return true;
            }
            Pattern pattern = Pattern.compile("(?i)\\w+\\s+(.*)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                String worldName = matcher.group(0);
                shop = plugin.getShopManager().getShop(Config.getGlobalShopUuid(worldName));
                if (shop == null) {
                    sender.sendMessage("No global shop on " + worldName + " to unlink!");
                    return true;
                } else {
                    Config.globalShopsRemove(worldName);
                    sender.sendMessage(ChatColor.DARK_AQUA + "Unlinked " + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " from " + ChatColor.WHITE + worldName);
                }
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }
        // Show link help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " unlink [worldname] " + ChatColor.DARK_AQUA + "- Link a global shop to this world.");

        return false;
    }
}
