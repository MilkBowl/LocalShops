package com.milkbukkit.localshops.commands;


import org.bukkit.command.CommandSender;

import com.milkbukkit.localshops.LocalShops;

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
