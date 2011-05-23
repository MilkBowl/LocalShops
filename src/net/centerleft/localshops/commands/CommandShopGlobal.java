package net.centerleft.localshops.commands;

import net.centerleft.localshops.Config;
import net.centerleft.localshops.LocalShops;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author sleaker
 *
 */
public class CommandShopGlobal extends Command {
    public CommandShopGlobal(LocalShops plugin, String commandLabel, CommandSender sender, String command) {
        super(plugin, commandLabel, sender, command);
    }
    
    public CommandShopGlobal(LocalShops plugin, String commandLabel, CommandSender sender, String[] command) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        // Check Permissions
        if (!canUseCommand(CommandTypes.ADMIN)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }
        
        log.info(String.format("[%s] Command issued: %s", plugin.pdfFile.getName(), command));
        Config.GLOBAL_SHOP = !Config.GLOBAL_SHOP;
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Global shop was set to " + ChatColor.WHITE + Config.GLOBAL_SHOP);
        if (Config.GLOBAL_SHOP_UUID == null) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Currently no shop is set to global, make sure to set one!");
        } else {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "The current global shop is: " + plugin.getShopManager().getShop(Config.GLOBAL_SHOP_UUID));
        }
        
        return true;
    }
}
