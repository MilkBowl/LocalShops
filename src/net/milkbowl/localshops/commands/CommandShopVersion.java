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


import net.milkbowl.localshops.LocalShops;

import org.bukkit.command.CommandSender;


public class CommandShopVersion extends Command {

    public CommandShopVersion(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }
    
    public CommandShopVersion(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        sender.sendMessage(String.format("LocalShops Version %s", plugin.getDescription().getVersion()));
        return true;
    }
}
