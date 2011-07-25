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

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.GlobalShop;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.PlayerData;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShopDebug extends Command {

    public CommandShopDebug(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);

    }

    public CommandShopDebug(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        if (isGlobal) {
            GlobalShop shop = null;
            String worldName = null;

            Pattern pattern = Pattern.compile("(?i)debug\\s+(.*)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                worldName = matcher.group(1);
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                worldName = player.getWorld().getName();
            } else {
                worldName = plugin.getServer().getWorlds().get(0).getName();
            }

            if (worldName == null) {
                sender.sendMessage("Could not find a world!");
            }

            shop = plugin.getShopManager().getGlobalShopByWorld(worldName);
            if (shop == null) {
                sender.sendMessage("Could not find the global shop for world \"" + worldName + "\"!");
                return true;
            }

            shop.log();

            return true;
        } else {
            Shop shop = null;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PlayerData pData = plugin.getPlayerData().get(player.getName());

                // info (player only command)
                Pattern pattern = Pattern.compile("(?i)debug$");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find()) {
                    // Get Current Shop
                    shop = plugin.getShopManager().getLocalShop(player.getLocation());
                    if (shop == null) {
                        sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
                        return true;
                    }
                }
            } else {
                // ignore?
            }

            // info id
            Pattern pattern = Pattern.compile("(?i)debug\\s+(.*)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                String input = matcher.group(1);
                shop = plugin.getShopManager().getShop(input);
                if (shop == null) {
                    sender.sendMessage("Could not find shop with ID " + input);
                    return false;
                }
            }

            if (shop != null) {
                shop.log();
                if (sender instanceof Player) {
                    sender.sendMessage("Shop has been logged to console!");
                }
                return true;
            } else {
                sender.sendMessage("Could not find shop!");
                return false;
            }
        }
    }
}
