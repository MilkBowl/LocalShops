/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package net.milkbowl.localshops.commands;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.ShopLocation;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if (isGlobal) {
            // Check Permissions
            if (!canUseCommand(PermType.ADMIN_GLOBAL)) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            }
            String worldName = null;
            Pattern pattern = Pattern.compile("(?i)(unlink|removeloc)\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                worldName = matcher.group(2);
            } else if (sender instanceof Player) {
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
        } else {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
                return true;
            }
            LocalShop lShop = (LocalShop) getCurrentShop(player);
            if (lShop == null) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
                return true;
            }
            if (!canUseCommand(PermType.ADMIN_LOCAL) && !lShop.getManagers().contains(player.getName()) && !lShop.getOwner().equals(player.getName())) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            } else if (!(lShop.getShopLocations().size() > 1)) {
                sender.sendMessage(ChatColor.DARK_AQUA + "This shop only has 1 location.  You must first add a new location before removing this one.");
                sender.sendMessage(ChatColor.DARK_AQUA + "If you want to permanently remove this shop, please use /lshop destroy");
            }
            Pattern pattern = Pattern.compile("(?i)(unlink|removeloc)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                Iterator<ShopLocation> iter = lShop.getShopLocations().iterator();
                while (iter.hasNext()) {
                    ShopLocation shopLoc = iter.next();
                    if (shopLoc.contains(player.getLocation())) {
                        //TODO: change formatting?
                        sender.sendMessage(ChatColor.DARK_AQUA + "Unlinked " + ChatColor.WHITE + lShop.getName() + ChatColor.DARK_AQUA + " from locations: " + shopLoc.toString());
                        iter.remove();
                        break;
                    }
                }

            }
            // Show unlink help
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " unlink " + ChatColor.DARK_AQUA + "- Unlink location from the shop you're in");
            return true;
        }
    }
}
