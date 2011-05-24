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
        Shop shop = null;


        if (sender instanceof Player) {
            // Check Permissions
            if (!canUseCommand(CommandTypes.ADMIN)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
                return true;
            }
            // Get player
            Player player = (Player) sender;

            Pattern pattern = Pattern.compile("(?i)link\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                String worldName = matcher.group(0);
                shop = getCurrentShop(player);
                if (shop == null) {
                    sender.sendMessage("No global shop on this world to link!");
                } else {
                    for ( World world : plugin.getServer().getWorlds() ) {
                        if (world.getName() == worldName) {
                            if (Config.globalShopsContainsKey(worldName)) {
                                if (Config.getGlobalShopUuid(worldName) == shop.getUuid()) {
                                    sender.sendMessage(worldName + " is already linked to that shop.");
                                } else {
                                    sender.sendMessage(worldName + " already has a global shop, delete or unlink the shop before attempting to link a new one.");
                                }
                                return true;
                            } else {
                                Config.globalShopsAdd(worldName, shop.getUuid());
                                sender.sendMessage("Added " + shop.getName() + " as a global shop for " + worldName);
                                return true;
                            }
                        }
                    }
                    sender.sendMessage("Could not find a world named " + worldName);
                }
            }
        } else {
            sender.sendMessage("Console is not implemented yet.");
            return true;
        }
        // Show link help
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link [worldname] " + ChatColor.DARK_AQUA + "- Link a global shop to this world.");
        return false;
    }

}
