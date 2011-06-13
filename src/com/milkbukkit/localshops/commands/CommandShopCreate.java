package com.milkbukkit.localshops.commands;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.milkbukkit.localshops.Config;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.PlayerData;
import com.milkbukkit.localshops.ResourceManager;
import com.milkbukkit.localshops.objects.GlobalShop;
import com.milkbukkit.localshops.objects.LocalShop;
import com.milkbukkit.localshops.objects.Shop;
import com.milkbukkit.localshops.objects.ShopLocation;
import com.milkbukkit.localshops.util.GenericFunctions;

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

        Player player = null;
        ShopLocation shopLoc = null;
        // Get current shop
        if (sender instanceof Player) {
            player = (Player) sender;
            PlayerData pData = plugin.getPlayerData().get(player.getName());          

            creator = player.getName();
            world = player.getWorld().getName();

            //Check permissions
            if (!canCreateShop(creator)) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_MAX_NUM_SHOPS));
                return false;
            }
           
            int x1 = 0, x2 = 0, z1 = 0, z2 = 0;
            // If player is select, use their selection
            if (pData.isSelecting()) {
                if (GenericFunctions.calculateCuboidSize(pData.getPositionA(), pData.getPositionB(), Config.getShopSizeMaxWidth(), Config.getShopSizeMaxHeight()) == null) {
                    String size = Config.getShopSizeMaxWidth() + "x" + Config.getShopSizeMaxHeight() + "x" + Config.getShopSizeMaxWidth();
                    player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SELECTION_PROB_SIZE, new String[] { "%SIZE%" }, new Object[] { size }));
                    return false;
                }

                Location loc1 = pData.getPositionA();
                Location loc2 = pData.getPositionB();

                if (loc1 == null || loc2 == null) {
                    player.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SELECTION_PROB_ONLY_ONE_POINT));
                    return false;
                }
            } else {
                // get current position
                Location loc = player.getLocation();
                int x = loc.getBlockX();
                int y = loc.getBlockY();
                int z = loc.getBlockZ();
                
                if (Config.getShopSizeDefWidth() % 2 == 0) {
                    x1 = x - (Config.getShopSizeDefWidth() / 2);
                    x2 = x + (Config.getShopSizeDefWidth() / 2);
                    z1 = z - (Config.getShopSizeDefWidth() / 2);
                    z2 = z + (Config.getShopSizeDefWidth() / 2);
                } else {
                    x1 = x - (Config.getShopSizeDefWidth() / 2) + 1;
                    x2 = x + (Config.getShopSizeDefWidth() / 2);
                    z1 = z - (Config.getShopSizeDefWidth() / 2) + 1;
                    z2 = z + (Config.getShopSizeDefWidth() / 2);
                }
                //generate the new shopLocation
                shopLoc = new ShopLocation(x1, y - 1, z1, x2, y + Config.getShopSizeDefHeight() - 1, z2, player.getWorld());
            }

            if(!plugin.getShopManager().shopPositionOk(shopLoc.getLocation1(), shopLoc.getLocation2(), world) && !isGlobal) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SHOP_EXISTS));
                return false;
            }
            if (isGlobal && plugin.getShopManager().getGlobalShopByWorld(world) != null) {
                sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_WORLD_HAS_GLOBAL));
                return false;
            }
            if (Config.getShopChargeCreate()) {
                if (!canUseCommand(CommandTypes.CREATE_FREE)) {
                    if (!plugin.getPlayerData().get(player.getName()).chargePlayer(player.getName(), Config.getShopChargeCreateCost())) {
                        sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_INSUFFICIENT_FUNDS));
                        return false;
                    }
                }
            }

        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.GEN_CONSOLE_NOT_IMPLEMENTED));
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


        matcher.reset();
        pattern = Pattern.compile("(?i)create\\s+(.*)");
        matcher = pattern.matcher(command);
        if (matcher.find()) {
            name = matcher.group(1);
        }
        
        Shop shop = null;
        
        if(isGlobal) {
            GlobalShop gShop = new GlobalShop(UUID.randomUUID());
            gShop.setCreator(creator);
            gShop.setOwner(creator);
            gShop.setName(name);
            gShop.setUnlimitedMoney(true);
            gShop.setUnlimitedStock(true);
            gShop.addWorld(world);
            plugin.getShopManager().addShop(gShop);
            shop = gShop;
            log.info(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_LOG, new String[] { "%TYPE%", "%SHOP%" }, new Object[] { "Global", gShop }));
        } else {
            LocalShop lShop = new LocalShop(UUID.randomUUID());
            lShop.setCreator(creator);
            lShop.setOwner(creator);
            lShop.setName(name);
            lShop.addWorld(world);
            lShop.getShopLocations().add(shopLoc);
            plugin.getShopManager().addShop(lShop);
            shop = lShop;
            log.info(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_LOG, new String[] { "%TYPE%", "%SHOP%" }, new Object[] { "Local", lShop }));
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                plugin.playerListener.checkPlayerPosition(p);
            }
        }

        // Disable selecting for player (if player)
        if(sender instanceof Player) {
            plugin.getPlayerData().get(player.getName()).setSelecting(false);
        }

        // write the file
        if (plugin.getShopManager().saveShop(shop)) {
            //Command.Shop.Create.Success=%CHAT_PREFIX%%WHITE%%SHOPNAME%%DARK_AQUA% was created successfully.  CMD_SHP_CREATE_SUCCESS
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_SUCCESS, new String[] { "%SHOPNAME%" }, new Object[] { shop.getName() }));
            return true;
        } else {
            sender.sendMessage(plugin.getResourceManager().getString(ResourceManager.CMD_SHP_CREATE_FAIL));
            return false;
        }

    }
}