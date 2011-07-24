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

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.PlayerData;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShopSelect extends Command {

    public CommandShopSelect(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public CommandShopSelect(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only players can interactively select coordinates.");
            return false;
        }

        if (canUseCommand(PermType.SELECT)) {

            Player player = (Player) sender;

            String playerName = player.getName();
            if (!plugin.getPlayerData().containsKey(playerName)) {
                plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
            }
            plugin.getPlayerData().get(playerName).setSelecting(!plugin.getPlayerData().get(playerName).isSelecting());

            if (plugin.getPlayerData().get(playerName).isSelecting()) {
                sender.sendMessage(ChatColor.WHITE + "Shop selection enabled." + ChatColor.DARK_AQUA + " Use " + ChatColor.WHITE + "bare hands " + ChatColor.DARK_AQUA + "to select!");
                sender.sendMessage(ChatColor.DARK_AQUA + "Left click to select the bottom corner for a shop");
                sender.sendMessage(ChatColor.DARK_AQUA + "Right click to select the far upper corner for the shop");
            } else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Selection disabled");
                plugin.getPlayerData().put(playerName, new PlayerData(plugin, playerName));
            }
            return true;
        } else {
            sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
            return true;
        }
    }
}
