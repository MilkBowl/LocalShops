package net.centerleft.localshops.commands;

import net.centerleft.localshops.LocalShops;
import net.centerleft.localshops.PlayerData;

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

        if (canUseCommand(CommandTypes.SELECT)) {

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
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return true;
        }
    }
}
