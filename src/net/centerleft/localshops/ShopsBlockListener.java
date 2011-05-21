/**
 * 
 */
package net.centerleft.localshops;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import cuboidLocale.BookmarkedResult;
import cuboidLocale.PrimitiveCuboid;

/**
 * @author sleaker
 *
 */
public class ShopsBlockListener extends BlockListener {
    private LocalShops plugin;


    public ShopsBlockListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    
    public void onSignChange(SignChangeEvent event) {
        Shop shop = null;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        PlayerData pData = plugin.getPlayerData().get(player.getName());
        
        BookmarkedResult res = pData.bookmark;
        //Search for Shops near event block and add them to the array list.
        res = LocalShops.getCuboidTree().relatedSearch(res.bookmark, block.getX(), block.getY(), block.getZ());
        @SuppressWarnings("unchecked")
        ArrayList<PrimitiveCuboid> cuboids = (ArrayList<PrimitiveCuboid>) res.results.clone();
        
        for (PrimitiveCuboid cuboid : cuboids) {
            if (cuboid.uuid == null)
                continue;
            if(!cuboid.world.equalsIgnoreCase(player.getWorld().getName())) {
                continue;
            }
            shop = plugin.getShopData().getShop(cuboid.uuid);
        }
        
        //Return if we still don't have a shop.
        if (shop == null) {
            return;
        }
        
        //Regex the 1st line to check for an item.
        String line1 = event.getLine(0);
        String line2 = "Buy: ";
        String line3 = "Sell: ";
        String line4 = "";
        
        ItemInfo item = Search.itemByName(line1);
        if ( item != null ) {
            line1 = item.name;
            if (shop.containsItem(item)) {
                if (shop.getItem(line1).getBuyPrice() == 0) {
                    line2 += "-";
                } else {
                    line2 += shop.getItem(line1).getBuyPrice();
                }
                if (shop.getItem(line1).getSellPrice() == 0) {
                    line3 += "-";
                } else {
                    line3 += shop.getItem(line1).getSellPrice();
                }
                //Add the sign to the Shop signlist and save the shop
                shop.getSignMap().put(new Location(event.getBlock().getWorld(), block.getX(), block.getY(), block.getZ()), item.name);
                plugin.getShopData().saveShop(shop);
                
                //Write back the lines for the sign
                event.setLine(0, line1);
                event.setLine(1, line2);
                event.setLine(2, line3);
                event.setLine(3, line4);
            } else {
                return;
            }
            
        } else {
            return;
        }
            
        
    }
    
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        
        //If not a sign ignore event.
        if ( (!(block.getType() == Material.SIGN_POST) && !(block.getType() == Material.WALL_SIGN)) || event.isCancelled() ) {
            return;
        }
        
        Shop shop = null;
        Player player = event.getPlayer();
        PlayerData pData = plugin.getPlayerData().get(player.getName());
        
        //Find the current shop.
        BookmarkedResult res = pData.bookmark;
        res = LocalShops.getCuboidTree().relatedSearch(res.bookmark, block.getX(), block.getY(), block.getZ());
        @SuppressWarnings("unchecked")
        ArrayList<PrimitiveCuboid> cuboids = (ArrayList<PrimitiveCuboid>) res.results.clone();
        
        for (PrimitiveCuboid cuboid : cuboids) {
            if (cuboid.uuid == null)
                continue;
            if(!cuboid.world.equalsIgnoreCase(player.getWorld().getName())) {
                continue;
            }
            shop = plugin.getShopData().getShop(cuboid.uuid);
        }

        if ( shop.getCreator() != player.getName() && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to place signs in the shop");
            event.setCancelled(true);
            return;
        }
    }
    
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        
        //If not a sign ignore event.
        if ( (!(block.getType() == Material.SIGN_POST) && !(block.getType() == Material.WALL_SIGN)) || event.isCancelled()) {
            return;
        }
        
        Shop shop = null;
        Player player = event.getPlayer();
        PlayerData pData = plugin.getPlayerData().get(player.getName());
        
        //Find the current shop.
        BookmarkedResult res = pData.bookmark;
        res = LocalShops.getCuboidTree().relatedSearch(res.bookmark, block.getX(), block.getY(), block.getZ());
        @SuppressWarnings("unchecked")
        ArrayList<PrimitiveCuboid> cuboids = (ArrayList<PrimitiveCuboid>) res.results.clone();
        
        for (PrimitiveCuboid cuboid : cuboids) {
            if (cuboid.uuid == null)
                continue;
            if(!cuboid.world.equalsIgnoreCase(player.getWorld().getName())) {
                continue;
            }
            shop = plugin.getShopData().getShop(cuboid.uuid);
        }

        if ( shop.getCreator() != player.getName() && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin")) ) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to remove signs in the shop");
            event.setCancelled(true);
            return;
        } else {
            Location blockLoc = block.getLocation();
            Iterator<Location> iter = shop.getSignMap().keySet().iterator();
            while (iter.hasNext()) {
                Location signLoc = iter.next();
                if ( signLoc.getBlockX() == blockLoc.getBlockX() && signLoc.getBlockY() == blockLoc.getBlockY() && signLoc.getBlockZ() == blockLoc.getBlockZ()) {
                    iter.remove();
                    return;
                }
            }
        }
        
    }


}

