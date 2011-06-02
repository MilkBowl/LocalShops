package com.milkbukkit.localshops.commands;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.objects.GlobalShop;
import com.milkbukkit.localshops.objects.Shop;

public class CommandShopDebug extends Command {

    public CommandShopDebug(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
        
    }

    public CommandShopDebug(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        if (isGlobal) {
            GlobalShop shop = null;
            String worldName = null;
            
            Pattern pattern = Pattern.compile("(?i)debug\\s+(.*)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                worldName = matcher.group(1);
            } else if(sender instanceof Player) {
                Player player = (Player) sender;
                worldName = player.getWorld().getName();
            } else {
                worldName = plugin.getServer().getWorlds().get(0).getName();
            }
            
            if(worldName == null) {
                sender.sendMessage("Could not find a world!");
            }
            
            shop = plugin.getShopManager().getGlobalShopByWorld(worldName);
            if(shop == null) {
                sender.sendMessage("Could not find the global shop for world \""+worldName+"\"!");
                return true;
            }
            
            shop.log();
            
            return true;
        } else {
            Shop shop = null;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PlayerData pData = plugin.getPlayerData().get(player.getName());

                // info (player only command)
                Pattern pattern = Pattern.compile("(?i)debug$");
                Matcher matcher = pattern.matcher(command);
                if (matcher.find()) {
                    // Get Current Shop
                    UUID shopUuid = pData.getCurrentShop();
                    if (shopUuid != null) {
                        shop = plugin.getShopManager().getLocalShop(shopUuid);
                    }
                    if (shop == null) {
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_NOT_IN_SHOP));
                        return false;
                    }
                }
            } else {
                // ignore?
            }

            // info id
            Pattern pattern = Pattern.compile("(?i)debug\\s+(.*)$");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                String input = matcher.group(1);
                shop = plugin.getShopManager().getShop(input);
                if (shop == null) {
                    sender.sendMessage("Could not find shop with ID " + input);
                    return false;
                }
            }

            if (shop != null) {
                shop.log();
                if (sender instanceof Player) {
                    sender.sendMessage("Shop has been logged to console!");
                }
                return true;
            } else {
                sender.sendMessage("Could not find shop!");
                return false;
            }
        }
    }

}
