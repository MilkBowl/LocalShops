/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
 * 
 */

package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.ItemInfo;

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
            ItemInfo[] found = Search.itemsByName(name, true);
            if (found == null || found.length == 0) {
                sender.sendMessage(String.format("No items found matching \"%s\"", name));
            } else {
            	int show = found.length;
            	if (show > 5)
            		show = 5;
            	
            	sender.sendMessage("Showing " + show + " of " + found.length + " results.");
            	for (int i = 0; i < show - 1; i++) 
            		sender.sendMessage(found[i].toString());
            	
            }
            return true;            
        }

        // Show search stuff
        sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " multisearch [item name]" + ChatColor.DARK_AQUA + " - Searches for and displays information about an item.");
        return true;
    }
}
