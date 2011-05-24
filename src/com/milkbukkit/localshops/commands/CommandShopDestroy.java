package com.milkbukkit.localshops.commands;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.Shop;

public class CommandShopDestroy extends Command {

    public CommandShopDestroy(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopDestroy(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        if (!(sender instanceof Player) || !canUseCommand(CommandTypes.DESTROY)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return false;
        }

        /*
         * Available formats: /lshop destroy
         */

        Player player = (Player) sender;
        String playerName = player.getName();
        
        // get the shop the player is currently in
        if (plugin.getPlayerData().get(playerName).shopList.size() == 1 && !isGlobal) {
            UUID shopUuid = plugin.getPlayerData().get(playerName).shopList.get(0);
            Shop shop = plugin.getShopManager().getShop(shopUuid);

            if (!shop.getOwner().equalsIgnoreCase(player.getName()) && !canUseCommand(CommandTypes.ADMIN)) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner to destroy it.");
                return false;
            }

            Iterator<PlayerData> it = plugin.getPlayerData().values().iterator();
            while (it.hasNext()) {
                PlayerData p = it.next();
                if (p.shopList.contains(shop.getUuid())) {
                    Player thisPlayer = plugin.getServer().getPlayer(p.playerName);
                    p.removePlayerFromShop(thisPlayer, shop.getUuid());
                    thisPlayer.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " has been destroyed");
                }
            }

            Collection<InventoryItem> shopItems = shop.getItems();

            if (plugin.getShopManager().deleteShop(shop)) {
                // return items to player (if a player)
                if (sender instanceof Player) {
                    for (InventoryItem item : shopItems) {
                        givePlayerItem(item.getInfo().toStack(), item.getStock());
                    }
                }
            } else {
                // error message :(
                sender.sendMessage("Could not return shop inventory!");
            }

        } else if (isGlobal && Config.globalShopsContainsKey(player.getWorld().getName())) {
            Shop shop = plugin.getShopManager().getShop(Config.getGlobalShopUuid(player.getWorld().getName()));

            if (!shop.getOwner().equalsIgnoreCase(player.getName()) && !canUseCommand(CommandTypes.ADMIN) && !shop.getManagers().contains(player.getName())) {
                player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or manager to destroy a global shop");
                return false;
            } else {
                if (plugin.getShopManager().deleteShop(shop)) {
                    Config.globalShopsRemove(player.getWorld().getName());
                    player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " has been destroyed");
                } else {
                    player.sendMessage(LocalShops.CHAT_PREFIX + " Error while attempting to destroy shop: " +  ChatColor.WHITE + shop.getName());
                }
            }
            
        } else {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be inside a shop to use /" + commandLabel + " destroy");
        }

        return true;
    }

}