/**
 * 
 */
package com.milkbukkit.localshops.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.LocalShops;
import com.milkbukkit.localshops.Search;
import com.milkbukkit.localshops.Shop;
import com.milkbukkit.localshops.ShopSign;

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
        Block block = event.getBlock();

        shop = plugin.getShopManager().getShop(event.getBlock().getLocation());

        // Return if we aren't in a shop
        if (shop == null) {
            return;
        }

        ItemInfo item = Search.itemByString(event.getLine(0));

        if (item != null) {

            String line1 = item.name;
            String line2 = "Buy: ";
            String line3 = "Sell: ";
            String line4 = "Stock: ";

            if (shop.containsItem(item)) {

                if (shop.getItem(item).getBuyPrice() == 0)
                    line2 += "-";
                else
                    line2 += shop.getItem(item).getBuyPrice();

                if (shop.getItem(item).getSellPrice() == 0)
                    line3 += "-";
                else
                    line3 += shop.getItem(item).getSellPrice();

                if (!shop.isUnlimitedStock())
                    line4 += shop.getItem(item).getStock();
                else
                    line4 += "-";

                // Add the sign to the Shop signlist and save the shop
                ShopSign sign = new ShopSign(block, item.name);
                shop.getSignMap().put(sign.hashString(), sign);
                plugin.getShopManager().saveShop(shop);

                // Write back the lines for the sign
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

        // If not a sign ignore event.
        if ((!(block.getType() == Material.SIGN_POST) && !(block.getType() == Material.WALL_SIGN)) || event.isCancelled()) {
            return;
        }

        Shop shop = null;
        Player player = event.getPlayer();

        // Find the current shop.
        shop = plugin.getShopManager().getShop(block.getLocation());

        // If we weren't in a shop then exit event
        if (shop == null) {
            return;
        }

        if (shop.getCreator() != player.getName() && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to place signs in the shop");
            event.setCancelled(true);
            return;
        }
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        // If not a sign ignore event.
        if ((block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) || event.isCancelled()) {
            return;
        }

        Shop shop = null;
        Player player = event.getPlayer();

        // Find the current shop.
        shop = plugin.getShopManager().getShop(block.getLocation());

        // If we weren't in a shop then exit
        if (shop == null) {
            return;
        }

        if (shop.getCreator() != player.getName() && !(shop.getManagers().contains(player.getName())) && !(plugin.getPermManager().hasPermission(player, "localshops.admin"))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to remove signs in the shop");
            event.setCancelled(true);
            return;
        } else {
            String xyz = block.getWorld().getName() + block.getX() + block.getY() + block.getZ();
            if (shop.getSignMap().containsKey(xyz)) {
                shop.getSignMap().remove(xyz);
            }
        }

    }

}
