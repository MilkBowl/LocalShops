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
import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandShopMultiSearch extends Command {

    public CommandShopMultiSearch(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public CommandShopMultiSearch(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        Pattern pattern = Pattern.compile("(?i)search\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String name = matcher.group(1);
            ItemInfo[] found = Items.itemsByName(name, true);
            if (found == null || found.length == 0) {
                sender.sendMessage(String.format("No items found matching \"%s\"", name));
            } else {
                int show = found.length;
                if (show > 5) {
                    show = 5;
                }

                sender.sendMessage("Showing " + show + " of " + found.length + " results.");
                for (int i = 0; i < show - 1; i++) {
                    sender.sendMessage(found[i].toString());
                }

            }
            return true;
        }

        // Show search stuff
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " multisearch [item name]" + ChatColor.DARK_AQUA + " - Searches for and displays information about an item.");
        return true;
    }
}
