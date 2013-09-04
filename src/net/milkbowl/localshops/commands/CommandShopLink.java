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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopLocation;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    @Override
    public boolean process() {
        if (!isGlobal) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (!canUseCommand(PermType.MULTI_LOCATION)) {
                    sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                    return true;
                }

                ShopLocation shopLoc = getNewShopLoc(player);

                //make sure we are getting a proper shop Location
                if (shopLoc == null) {
                    return false;
                }

                LocalShop shop = null;
                Pattern pattern = Pattern.compile("(?i)\\w+\\s+([A-Za-z0-9\\-]+)$");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find()) {
                    shop = plugin.getShopManager().getLocalShop(matcher.group(1));
                    if (shop == null) {
                        sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_SHOP_NOT_FOUND));
                        return true;
                    } else if (!shop.getWorld().equals(player.getWorld().getName())) {
                        sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_NOT_ON_WORLD, new String[]{"%SHOPNAME%"}, new String[]{shop.getName()}));
                        return false;
                    }
                    if (!plugin.getShopManager().shopPositionOk(shopLoc.getLocation1(), shopLoc.getLocation2(), shop.getWorld())) {
                        sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_CREATE_SHOP_EXISTS));
                        return false;
                    }
                    if (!plugin.getShopManager().canBuildInPosition(player, shopLoc.getLocation1(), shopLoc.getLocation2(), player.getWorld())) {
                        sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_CREATE_ZONE_EXISTS));
                        return false;
                    }
                    shop.getShopLocations().add(shopLoc);
                    return true;
                } else {
                    // Send usage to player
                    sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link [shopid] " + ChatColor.DARK_AQUA + "- Link a local shop with id to your current location or selection");
                    return true;
                }
            } else {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
            }

            return true;
        } else {
            String worldName = null;
            GlobalShop shop = null;
            // Check Permissions
            if (!canUseCommand(PermType.ADMIN_GLOBAL)) {
                sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
                return true;
            }
            // check if "link shopId worldName
            Pattern pattern = Pattern.compile("(?i)\\w+\\s+([A-Za-z0-9\\-]+)\\s+(\\w+)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                String key = matcher.group(1);
                worldName = matcher.group(2);
                shop = plugin.getShopManager().getGlobalShop(key);
                if (shop == null) {
                    sender.sendMessage("Could not find a global shop that matches id: " + key);
                    return true;
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                matcher.reset();
                pattern = Pattern.compile("(?i)\\w+\\s+(.+)");
                matcher = pattern.matcher(command);
                if (matcher.find()) {
                    worldName = matcher.group(1);
                    Shop s = getCurrentShop(player);
                    if (s == null) {
                        sender.sendMessage("A global shop does not exist for your world, you must create one first before you can link!");
                        return true;
                    }

                    if (s instanceof GlobalShop) {
                        shop = (GlobalShop) s;
                    } else {
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

            Shop wShop = plugin.getShopManager().getGlobalShopByWorld(worldName);
            if (wShop != null) {
                sender.sendMessage(worldName + " already has a global shop with id: " + wShop.getShortUuidString());
                return true;
            }

            shop.addWorld(worldName);
            plugin.getShopManager().mapWorldShop(worldName, shop);
            sender.sendMessage("Added " + shop.getName() + " as a global shop for " + worldName);
            return true;
        }
    }
}
