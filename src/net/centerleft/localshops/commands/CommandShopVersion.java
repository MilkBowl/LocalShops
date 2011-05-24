package net.centerleft.localshops.commands;

import net.centerleft.localshops.LocalShops;

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
