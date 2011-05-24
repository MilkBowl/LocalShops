package com.milkbukkit.localshops.commands;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.ShopLocation;

public class CommandShopCreate extends Command {

    public CommandShopCreate(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public CommandShopCreate(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
        super(plugin, commandLabel, sender, command, isGlobal);
    }

    public boolean process() {
        String creator = null;
        String world = null;
        int[] xyzA = new int[3];
        int[] xyzB = new int[3];
        
        Player player = null;

        // Get current shop
        if (sender instanceof Player) {
            player = (Player) sender;
            PlayerData pData = plugin.getPlayerData().get(player.getName());          

            creator = player.getName();
            world = player.getWorld().getName();

            //Check permissions
            if (!canCreateShop(creator)) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You already have the maximum number of shops or don't have permission to create them!");
                return false;
            }

            // If player is select, use their selection
            if (pData.isSelecting()) {
                if (!pData.checkSize()) {
                    String size = Config.getShopSizeMaxWidth() + "x" + Config.getShopSizeMaxHeight() + "x" + Config.getShopSizeMaxWidth();
                    player.sendMessage(ChatColor.DARK_AQUA + "Problem with selection. Max size is " + ChatColor.WHITE + size);
                    return false;
                }

                xyzA = pData.getPositionA();
                xyzB = pData.getPositionB();

                if (xyzA == null || xyzB == null) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Problem with selection. Only one point selected");
                    return false;
                }
            } else {
                // get current position
                Location loc = player.getLocation();
                int x = loc.getBlockX();
                int y = loc.getBlockY();
                int z = loc.getBlockZ();

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

            if(!plugin.getShopManager().shopPositionOk(xyzA, xyzB, world)) {
                sender.sendMessage("A shop already exists here!");
                return false;
            }
            if (isGlobal && Config.globalShopsContainsKey(world)) {
                sender.sendMessage(world + " already has a global shop. Remove it before creating a new one!");
                return false;
            }
            if (Config.getShopChargeCreate()) {
                if (!canUseCommand(CommandTypes.CREATE_FREE)) {
                    if (!plugin.getPlayerData().get(player.getName()).chargePlayer(player.getName(), Config.getShopChargeCreateCost())) {
                        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You need " + plugin.getEconManager().format(Config.getShopChargeCreateCost()) + " to create a shop.");
                        return false;
                    }
                }
            }

        } else {
            sender.sendMessage("Console is not implemented yet.");
            return false;
        }

        // Command matching     
        String name = null;

        Pattern pattern = Pattern.compile("(?i)create$");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            if (isGlobal) {
                name = player.getWorld().getName() + " Shop";
            } else {
                name = player.getName() + " Shop";
            }
        }

        if (name == null) {
            matcher.reset();
            Pattern.compile("(?i)create\\s+(.*)");
            matcher = pattern.matcher(command);
            if (matcher.find()) {
                name = matcher.group(1);
            }
        }

        Shop shop = new Shop(UUID.randomUUID());
        shop.setCreator(creator);
        shop.setOwner(creator);
        shop.setName(name);
        shop.setWorld(world);
        if ( !isGlobal) {
            shop.setLocations(new ShopLocation(xyzA), new ShopLocation(xyzB));

            log.info(String.format("[%s] Created: %s", plugin.pdfFile.getName(), shop.toString()));
        } else {
            shop.setUnlimitedMoney(true);
            shop.setUnlimitedStock(true);
            shop.setGlobal(true);
            Config.globalShopsAdd(world, shop.getUuid());
        }
        plugin.getShopManager().addShop(shop);
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            plugin.playerListener.checkPlayerPosition(p);
        }

        // Disable selecting for player (if player)
        if(sender instanceof Player) {
            plugin.getPlayerData().get(player.getName()).setSelecting(false);
        }

        // write the file

        if (plugin.getShopManager().saveShop(shop)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.WHITE + shop.getName() + ChatColor.DARK_AQUA + " was created successfully.");
            return true;
        } else {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "There was an error, could not create shop.");
            return false;
        }

    }
}