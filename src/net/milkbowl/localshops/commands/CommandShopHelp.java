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

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.PermType;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandShopHelp extends Command {

    public CommandShopHelp(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopHelp(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        sender.sendMessage(plugin.getResourceManager().getChatPrefix() + " " + ChatColor.DARK_AQUA + "Here are the available commands [required] <optional>");

        if (canUseCommand(PermType.ADD)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " add" + ChatColor.DARK_AQUA + " - Add the item that you are holding to the shop.");
        }
        if ((isGlobal && canUseCommand(PermType.GLOBAL_BROWSE)) || canUseCommand(PermType.BROWSE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " browse <buy|sell> " + ChatColor.DARK_AQUA + "- List the shop's inventory.");
        }
        if ((isGlobal && canUseCommand(PermType.BUY)) || canUseCommand(PermType.BUY)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " buy [itemname] [number] " + ChatColor.DARK_AQUA + "- Buy this item.");
        }
        if (canUseCommand(PermType.CREATE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " create [ShopName]" + ChatColor.DARK_AQUA + " - Create a shop at your location.");
        }
        if (canUseCommand(PermType.DESTROY)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " destroy" + ChatColor.DARK_AQUA + " - Destroy the shop you're in.");
        }
        if (Config.getFindMaxDistance() != 0 && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " find [itemname]" + ChatColor.DARK_AQUA + " - Find closest shops by item name.");
        }
        if (canUseCommand(PermType.ADMIN_GLOBAL) && isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link <shopid> [worldname]" + ChatColor.DARK_AQUA + " - Link a global shop to another world");
        }
        if (canUseCommand(PermType.MOVE) && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " move [ShopID]" + ChatColor.DARK_AQUA + " - Move a shop to your location.");
        }
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " search|searchall [itemname]" + ChatColor.DARK_AQUA + " - Search for an item by name.");
        if (canUseCommand(PermType.SELECT) && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " select" + ChatColor.DARK_AQUA + " - Select two corners for custom shop size.");
        }
        if ((isGlobal && canUseCommand(PermType.GLOBAL_SELL)) ||  canUseCommand(PermType.SELL)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell <#|all>" + ChatColor.DARK_AQUA + " - Sell the item in your hand.");
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell [itemname] [number]");
        }
        if (canUseCommand(PermType.SET)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " set" + ChatColor.DARK_AQUA + " - Display list of set commands");
        }
        if (canUseCommand(PermType.REMOVE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " remove [itemname]" + ChatColor.DARK_AQUA + " - Stop selling item in shop.");
        }
        return true;
    }
}
