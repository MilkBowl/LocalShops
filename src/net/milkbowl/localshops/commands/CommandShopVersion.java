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
import org.bukkit.ChatColor;

import org.bukkit.command.CommandSender;


public class CommandShopVersion extends Command {

    public CommandShopVersion(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }
    
    public CommandShopVersion(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

   @Override
    public boolean process() {
        sender.sendMessage(String.format("LocalShops Version %s", plugin.getDescription().getVersion()));
        sender.sendMessage(ChatColor.DARK_AQUA + "Permission: " + ChatColor.WHITE + LocalShops.getPerm().getName());
        sender.sendMessage(ChatColor.DARK_AQUA + "sEconomy: " + ChatColor.WHITE + LocalShops.getEcon().getName());
        return true;
    }
}
