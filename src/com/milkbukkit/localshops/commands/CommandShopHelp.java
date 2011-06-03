package com.milkbukkit.localshops.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;

public class CommandShopHelp extends Command {

    public CommandShopHelp(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }
    
    public CommandShopHelp(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        sender.sendMessage(plugin.getResourceManager().getChatPrefix() + " " + ChatColor.DARK_AQUA + "Here are the available commands [required] <optional>");

        if (canUseCommand(CommandTypes.ADD)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " add" + ChatColor.DARK_AQUA + " - Add the item that you are holding to the shop.");
        }
        if (canUseCommand(CommandTypes.BROWSE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " browse <buy|sell> " + ChatColor.DARK_AQUA + "- List the shop's inventory.");
        }
        if (canUseCommand(CommandTypes.BUY)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " buy [itemname] [number] " + ChatColor.DARK_AQUA + "- Buy this item.");
        }
        if (canUseCommand(CommandTypes.CREATE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " create [ShopName]" + ChatColor.DARK_AQUA + " - Create a shop at your location.");
        }
        if (canUseCommand(CommandTypes.DESTROY)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " destroy" + ChatColor.DARK_AQUA + " - Destroy the shop you're in.");
        }
        if (Config.getFindMaxDistance() != 0 && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " find [itemname]" + ChatColor.DARK_AQUA + " - Find closest shops by item name.");
        }
        if (canUseCommand(CommandTypes.ADMIN) && isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " link <shopid> [worldname]" + ChatColor.DARK_AQUA + " - Link a global shop to another world");
        }
        if (canUseCommand(CommandTypes.MOVE) && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " move [ShopID]" + ChatColor.DARK_AQUA + " - Move a shop to your location.");
        }
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " search [itemname]" + ChatColor.DARK_AQUA + " - Search for an item by name.");
        if (canUseCommand(CommandTypes.SELECT) && !isGlobal) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " select" + ChatColor.DARK_AQUA + " - Select two corners for custom shop size.");
        }
        if (canUseCommand(CommandTypes.SELL)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell <#|all>" + ChatColor.DARK_AQUA + " - Sell the item in your hand.");
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " sell [itemname] [number]");
        }
        if (canUseCommand(CommandTypes.SET)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " set" + ChatColor.DARK_AQUA + " - Display list of set commands");
        }
        if (canUseCommand(CommandTypes.REMOVE)) {
            sender.sendMessage(ChatColor.WHITE + "   /" + commandLabel + " remove [itemname]" + ChatColor.DARK_AQUA + " - Stop selling item in shop.");
        }
        return true;
    }
}
