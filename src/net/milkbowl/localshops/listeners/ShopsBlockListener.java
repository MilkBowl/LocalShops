/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package net.milkbowl.localshops.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.exceptions.TypeNotFoundException;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.LocalShop;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.ShopSign;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
    private static final Logger log = Logger.getLogger("Minecraft");

    public ShopsBlockListener(LocalShops plugin) {
        this.plugin = plugin;
    }

    @Override
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
                    int amount = 1;
                    if (!event.getLine(2).isEmpty()) {
                        try {
                            amount = Integer.parseInt(event.getLine(2));
                        } catch (Exception e) {
                            event.getPlayer().sendMessage("Error parsing amount on line 3 of sign, defaulting to 1");
                        }
                    }
                    if (event.getLine(1).equalsIgnoreCase("buy")) {
                        sign = new ShopSign(block, item.getName(), 1, amount);
                    } else if (event.getLine(1).equalsIgnoreCase("sell")) {
                        sign = new ShopSign(block, item.getName(), 2, amount);
                    } else {
                        // Add the sign to the Shop signlist and save the shop
                        sign = new ShopSign(block, item.getName(), 0, amount);
                    }

                    // Set, save, get lines
                    shop.getSigns().add(sign);
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

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        LocalShop shop = null;
        Player player = event.getPlayer();

        // Find the current shop.
        shop = plugin.getShopManager().getLocalShop(block.getLocation());

        // If we weren't in a shop then exit event
        if (shop == null) {
            return;
        }
        
        // Cancel any block place for non-managers/owners/admins while in a shop
        if (!player.getName().equals(shop.getOwner()) && !shop.getManagers().contains(player.getName()) && !(plugin.getPerm().has(player, PermType.ADMIN_LOCAL.get()))) {
            event.setCancelled(true);
            return;
        }

        // Cancel any block place that is targetted at a sign while in a shop
        if(event.getBlockAgainst().getType() == Material.SIGN || event.getBlockAgainst().getType() == Material.SIGN_POST) {
            event.setCancelled(true);
            return;
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        // If not a sign ignore event.
        if ((!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN))) {
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

        //Lets detect if this block is a sign, or if it has a sign attached to it.
        List<Block> blockList = new ArrayList<Block>();
        if (block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
            blockList.add(block);
        } else {
            blockList.addAll(findWallSigns(block));
        }

        if (!shop.getOwner().equals(player.getName()) && !(shop.getManagers().contains(player.getName())) && !(plugin.getPerm().has(player, PermType.ADMIN_LOCAL.get()))) {
            player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to remove signs in the shop");
            event.setCancelled(true);
            return;
        }

        //remove any Blocks in the blocklist
        if (!blockList.isEmpty()) {
            Iterator<ShopSign> iter = shop.getSigns().iterator();
            while (iter.hasNext()) {
                ShopSign sign = iter.next();
                for (Block b : blockList) {
                    if (sign.getLoc().equals(b.getLocation())) {
                        iter.remove();
                    }
                }
            }
        }
    }

    private List<Block> findWallSigns(Block block) {
        List<Block> foundSigns = new ArrayList<Block>(6);
        for (BlockFace face : BlockFace.values()) {
            if (block.getRelative(face).getType().equals(Material.WALL_SIGN)) {
                foundSigns.add(block.getRelative(face));
            } else if (block.getRelative(face).getType().equals(Material.SIGN_POST)) {
                foundSigns.add(block.getRelative(face));
            }
        }
        return foundSigns;
    }
}
