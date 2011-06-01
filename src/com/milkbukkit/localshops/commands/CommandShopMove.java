package com.milkbukkit.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.objects.LocalShop;
import com.milkbukkit.localshops.objects.ShopLocation;
import com.milkbukkit.localshops.util.GenericFunctions;

public class CommandShopMove extends Command {

    public CommandShopMove(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public CommandShopMove(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {
        
        if(isGlobal) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You cannot move a global shop!");
            return true;
        }

        if(!canUseCommand(CommandTypes.MOVE)) {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_USER_ACCESS_DENIED));
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Console is not implemented yet.");
            return true;
        }

        Pattern pattern = Pattern.compile("(?i)move\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String id = matcher.group(1);

            Player player = (Player) sender;
            Location location = player.getLocation();

            // check to see if that shop exists
            LocalShop thisShop = plugin.getShopManager().getLocalShop(id);
            if(thisShop == null) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Could not find shop: " + ChatColor.WHITE + id);
                return false;
            }

            // check if player has access
            if (!thisShop.getOwner().equalsIgnoreCase(player.getName())) {
                player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to move this shop.");
                return false;
            }

            // Get current player location
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();

            // setup the cuboid for the tree
            int[] xyzA = new int[3];
            int[] xyzB = new int[3];

            if (plugin.getPlayerData().containsKey(player.getName()) && plugin.getPlayerData().get(player.getName()).isSelecting()) {

                // Check if size is ok
                PlayerData pData = plugin.getPlayerData().get(player.getName());
                if (GenericFunctions.calculateCuboidSize(pData.getPositionA(), pData.getPositionB(), Config.getShopSizeMaxWidth(), Config.getShopSizeMaxHeight()) == null) {
                    String size = Config.getShopSizeMaxWidth() + "x" + Config.getShopSizeDefHeight() + "x" + Config.getShopSizeMaxWidth();
                    player.sendMessage(ChatColor.DARK_AQUA + "Problem with selection. Max size is " + ChatColor.WHITE + size);
                    return false;
                }

                // if a custom size had been set, use that
                PlayerData data = plugin.getPlayerData().get(player.getName());
                xyzA = data.getPositionA().clone();
                xyzB = data.getPositionB().clone();

                if (xyzA == null || xyzB == null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Problem with selection.");
                    return false;
                }
            } else {
                // otherwise calculate the shop from the player's location
                if (Config.getShopSizeDefWidth() % 2 == 0) {
                    xyzA[0] = x - (Config.getShopSizeDefWidth() / 2);
                    xyzB[0] = x + (Config.getShopSizeDefWidth() / 2);
                    xyzA[2] = z - (Config.getShopSizeDefWidth() / 2);
                    xyzB[2] = z + (Config.getShopSizeDefWidth() / 2);
                } else {
                    xyzA[0] = x - (Config.getShopSizeDefWidth() / 2) + 1;
                    xyzB[0] = x + (Config.getShopSizeDefWidth() / 2);
                    xyzA[2] = z - (Config.getShopSizeDefWidth() / 2) + 1;
                    xyzB[2] = z + (Config.getShopSizeDefWidth() / 2);
                }

                xyzA[1] = y - 1;
                xyzB[1] = y + Config.getShopSizeDefHeight() - 1;

            }

            // need to check to see if the shop overlaps another shop
            if (plugin.getShopManager().shopPositionOk(xyzA, xyzB, player.getWorld().getName())) {

                if (Config.getShopChargeMove()) {
                    if (!canUseCommand(CommandTypes.MOVE_FREE)) {
                        if (!plugin.getPlayerData().get(player.getName()).chargePlayer(player.getName(), Config.getShopChargeMoveCost())) {
                            // return, this player did not have enough money

                            player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You need " + plugin.getEconManager().format(Config.getShopChargeMoveCost()) + " to move a shop.");
                            return false;
                        }
                    }
                }

                // update the shop
                thisShop.setWorld(player.getWorld().getName());
                thisShop.setLocations(new ShopLocation(xyzA), new ShopLocation(xyzB));
                log.info(thisShop.getUuid().toString());

                plugin.getPlayerData().put(player.getName(), new PlayerData(plugin, player.getName()));

                // write the file
                if (plugin.getShopManager().saveShop(thisShop)) {
                    player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.WHITE + thisShop.getName() + ChatColor.DARK_AQUA + " was moved successfully.");
                    return true;
                } else {
                    player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "There was an error, could not move shop.");
                    return false;
                }
            }            
        }

        // Show usage
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "The command format is " + ChatColor.WHITE + "/" + commandLabel + " move [id]");
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Use " + ChatColor.WHITE + "/" + commandLabel + " info" + ChatColor.DARK_AQUA + " to obtain the id.");
        return true;
    }

}