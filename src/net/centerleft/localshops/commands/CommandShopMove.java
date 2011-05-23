package net.centerleft.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.centerleft.localshops.Config;
import net.centerleft.localshops.LocalShops;
import net.centerleft.localshops.PlayerData;
import net.centerleft.localshops.Shop;
import net.centerleft.localshops.ShopLocation;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cuboidLocale.BookmarkedResult;
import cuboidLocale.PrimitiveCuboid;

public class CommandShopMove extends Command {

    public CommandShopMove(LocalShops plugin, String commandLabel, CommandSender sender, String command) {
        super(plugin, commandLabel, sender, command);
    }

    public CommandShopMove(LocalShops plugin, String commandLabel, CommandSender sender, String[] command) {
        super(plugin, commandLabel, sender, command);
    }

    public boolean process() {

        if(!canUseCommand(CommandTypes.MOVE)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You don't have permission to use this command");
            return false;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Console is not implemented yet.");
            return false;
        }

        Pattern pattern = Pattern.compile("(?i)move\\s+(.*)");
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            String id = matcher.group(1);

            Player player = (Player) sender;
            Location location = player.getLocation();
            Shop thisShop = null;

            double[] xyzAold = new double[3];
            double[] xyzBold = new double[3];

            // check to see if that shop exists
            thisShop = plugin.getShopManager().getShop(id);
            if(thisShop == null) {
                sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Could not find shop: " + ChatColor.WHITE + id);
                return false;
            }

            // check if player has access
            if (!thisShop.getOwner().equalsIgnoreCase(player.getName())) {
                player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You must be the shop owner to move this shop.");
                return false;
            }

            // store shop info
            String shopName = thisShop.getName();
            xyzAold = thisShop.getLocationA().toArray();
            xyzBold = thisShop.getLocationB().toArray();

            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();

            // setup the cuboid for the tree
            double[] xyzA = new double[3];
            double[] xyzB = new double[3];

            if (plugin.getPlayerData().containsKey(player.getName()) && plugin.getPlayerData().get(player.getName()).isSelecting()) {

                // Check if size is ok
                if (!plugin.getPlayerData().get(player.getName()).checkSize()) {
                    String size = Config.SHOP_SIZE_MAX_WIDTH + "x" + Config.SHOP_SIZE_MAX_HEIGHT + "x" + Config.SHOP_SIZE_MAX_WIDTH;
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
                if (Config.SHOP_SIZE_DEF_WIDTH % 2 == 0) {
                    xyzA[0] = x - (Config.SHOP_SIZE_DEF_WIDTH / 2);
                    xyzB[0] = x + (Config.SHOP_SIZE_DEF_WIDTH / 2);
                    xyzA[2] = z - (Config.SHOP_SIZE_DEF_WIDTH / 2);
                    xyzB[2] = z + (Config.SHOP_SIZE_DEF_WIDTH / 2);
                } else {
                    xyzA[0] = x - (Config.SHOP_SIZE_DEF_WIDTH / 2) + 1;
                    xyzB[0] = x + (Config.SHOP_SIZE_DEF_WIDTH / 2);
                    xyzA[2] = z - (Config.SHOP_SIZE_DEF_WIDTH / 2) + 1;
                    xyzB[2] = z + (Config.SHOP_SIZE_DEF_WIDTH / 2);
                }

                xyzA[1] = y - 1;
                xyzB[1] = y + Config.SHOP_SIZE_DEF_HEIGHT - 1;

            }

            // remove the old shop from the cuboid
            ShopLocation xyz = thisShop.getLocationCenter();
            BookmarkedResult res = new BookmarkedResult();
            res = LocalShops.getCuboidTree().relatedSearch(res.bookmark, xyz.getX(), xyz.getY(), xyz.getZ());

            // get the shop's tree node and delete it
            for (PrimitiveCuboid shopLocation : res.results) {

                // for each shop that you find, check to see if we're already in
                // it
                // this should only find one shop node
                if (shopLocation.uuid == null)
                    continue;
                if (!shopLocation.world.equalsIgnoreCase(thisShop.getWorld()))
                    continue;

                LocalShops.getCuboidTree().delete(shopLocation);
            }

            // need to check to see if the shop overlaps another shop
            if (shopPositionOk(xyzA, xyzB, player.getWorld().getName())) {

                PrimitiveCuboid tempShopCuboid = new PrimitiveCuboid(xyzA, xyzB);
                tempShopCuboid.uuid = thisShop.getUuid();
                tempShopCuboid.world = player.getWorld().getName();

                if (Config.SHOP_CHARGE_MOVE) {
                    if (!canUseCommand(CommandTypes.MOVE_FREE)) {
                        if (!plugin.getPlayerData().get(player.getName()).chargePlayer(player.getName(), Config.SHOP_CHARGE_MOVE_COST)) {
                            // insert the old cuboid back into the world
                            tempShopCuboid = new PrimitiveCuboid(xyzAold, xyzBold);
                            tempShopCuboid.uuid = thisShop.getUuid();
                            tempShopCuboid.world = thisShop.getWorld();
                            LocalShops.getCuboidTree().insert(tempShopCuboid);

                            player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "You need " + plugin.getEconManager().format(Config.SHOP_CHARGE_MOVE_COST) + " to move a shop.");
                            return false;
                        }
                    }
                }

                // insert the shop into the world
                thisShop.setWorld(player.getWorld().getName());
                thisShop.setLocations(new ShopLocation(xyzA), new ShopLocation(xyzB));
                log.info(thisShop.getUuid().toString());
                plugin.getShopManager().deleteShop(thisShop);
                plugin.getShopManager().addShop(thisShop);
                LocalShops.getCuboidTree().insert(tempShopCuboid);

                plugin.getPlayerData().put(player.getName(), new PlayerData(plugin, player.getName()));

                // write the file
                if (plugin.getShopManager().saveShop(thisShop)) {
                    player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.WHITE + shopName + ChatColor.DARK_AQUA + " was moved successfully.");
                    return true;
                } else {
                    player.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "There was an error, could not move shop.");
                    return false;
                }
            } else {
                // insert the old cuboid back into the world
                PrimitiveCuboid tempShopCuboid = new PrimitiveCuboid(xyzAold, xyzBold);
                tempShopCuboid.uuid = thisShop.getUuid();
                tempShopCuboid.world = thisShop.getWorld();
                LocalShops.getCuboidTree().insert(tempShopCuboid);
                return true;
            }            
        }

        // Show usage
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "The command format is " + ChatColor.WHITE + "/" + commandLabel + " move [id]");
        sender.sendMessage(LocalShops.CHAT_PREFIX + ChatColor.DARK_AQUA + "Use " + ChatColor.WHITE + "/" + commandLabel + " info" + ChatColor.DARK_AQUA + " to obtain the id.");
        return true;
    }

}