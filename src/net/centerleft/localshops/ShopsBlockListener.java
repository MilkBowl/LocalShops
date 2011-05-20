/**
 * 
 */
package net.centerleft.localshops;

import java.util.ArrayList;

import org.bukkit.ChatColor;
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
        Shop shop;
        Player player = event.getPlayer();
        Block block = event.getBlock();
        PlayerData pData = plugin.getPlayerData().get(player.getName());
        
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
        
        //TODO: change the sign format + display info about the item.
    }
    
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        
        //If not a sign ignore event.
        if ( !(block.getType() == Material.SIGN_POST) && !(block.getType() == Material.WALL_SIGN)) {
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
        if ( !(block.getType() == Material.SIGN_POST) && !(block.getType() == Material.WALL_SIGN)) {
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
        }
        
    }


}

