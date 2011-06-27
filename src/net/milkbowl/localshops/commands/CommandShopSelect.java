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
import net.milkbowl.localshops.ResourceManager;
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
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
            return true;
        }
    }
}
