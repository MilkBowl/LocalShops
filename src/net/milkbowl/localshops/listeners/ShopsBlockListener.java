/**
 * 
 */
package net.milkbowl.localshops.listeners;

import java.util.Iterator;
import java.util.logging.Logger;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.exceptions.TypeNotFoundException;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopSign;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;


/**
 * @author sleaker
 * 
 */
public class ShopsBlockListener extends BlockListener {
    private LocalShops plugin;
    
    // Logging
    private final Logger log = Logger.getLogger("Minecraft");

    public ShopsBlockListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    public void onSignChange(SignChangeEvent event) {
        Shop shop = null;
        Block block = event.getBlock();

        shop = plugin.getShopManager().getLocalShop(event.getBlock().getLocation());

        // Return if we aren't in a shop
        if (shop == null) {
            return;
        }
        ShopSign sign = null;
        String[] signLines = null;

        ItemInfo item = Search.itemByString(event.getLine(0));
        
        if (item != null) {
            if (shop.containsItem(item)) {
                // Create the sign object to work with
                try {
                    if (event.getLine(1).equalsIgnoreCase("buy")) {
                        sign = new ShopSign(block, item.name, 1);
                    } else if (event.getLine(1).equalsIgnoreCase("sell")) {
                        sign = new ShopSign(block, item.name, 2);
                    } else {
                        // Add the sign to the Shop signlist and save the shop
                        sign = new ShopSign(block, item.name, 0);
                    }
                    
                    // Set, save, get lines
                    shop.getSignSet().add(sign);
                    plugin.getShopManager().saveShop(shop);
                    signLines = shop.generateSignLines(sign);
                    
                    // Write back the lines for the sign
                    if (signLines != null) {
                        event.setLine(0, signLines[0]);
                        event.setLine(1, signLines[1]);
                        event.setLine(2, signLines[2]);
                        event.setLine(3, signLines[3]);
                    }
                    
                } catch (TypeNotFoundException e) {
                    log.warning(String.format("[%s] WARNING: TypeNotFoundException: %s", plugin.getDescription().getName(), e.getMessage()));
                }
            }
        }
    }

    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        // If not a sign ignore event.
        if ( (!(block.getType().equals(Material.SIGN_POST)) && !(block.getType().equals(Material.WALL_SIGN)) && !(block.getType().equals(Material.CHEST))) || event.isCancelled()) {
            return;
        }

        LocalShop shop = null;
        Player player = event.getPlayer();

        // Find the current shop.
        shop = plugin.getShopManager().getLocalShop(block.getLocation());

        // If we weren't in a shop then exit event
        if (shop == null) {
            return;
        }

        if (!shop.getOwner().equals(player.getName()) && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to place a sign or chest in the shop");
            event.setCancelled(true);
            return;
        } 
        else if (block.getType().equals(Material.CHEST) && shop.getChests().size() >= plugin.getPermManager().getInfoIntLow(shop.getWorld(), player.getName(), "maxchests") && plugin.getPermManager().getInfoIntLow(shop.getWorld(), player.getName(), "maxchests") > 0) {
            player.sendMessage(ChatColor.DARK_AQUA + "You already have the maximum allowed number of chests");
            event.setCancelled(true);
            return;
        }
        
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        // If not a sign ignore event.
        if ((!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN) && !block.getType().equals(Material.CHEST)) || event.isCancelled()) {
            return;
        }

        //events only affect localshops
        LocalShop shop = null;
        Player player = event.getPlayer();

        // Find the current shop.
        shop = plugin.getShopManager().getLocalShop(block.getLocation());

        // If we weren't in a shop then exit
        if (shop == null) {
            return;
        }

        if (!shop.getOwner().equals(player.getName()) && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to remove signs or chests in the shop");
            event.setCancelled(true);
            return;
        } else if (!block.getType().equals(Material.CHEST)) {
            Iterator<ShopSign> iter = shop.getSignSet().iterator();
            while (iter.hasNext()) {
                ShopSign sign = iter.next();
                if (sign.getLoc().equals(event.getBlock().getLocation())) {
                    iter.remove();
                }
            }
        } else {
            if (!(((ContainerBlock) block).getInventory().getContents() == null )) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_AQUA + "You must first remove all contents from the chest before removing it from the shop."); 
                return;
            }
            else {
                shop.getChests().remove(block.getLocation());
            }
        }

    }

}
