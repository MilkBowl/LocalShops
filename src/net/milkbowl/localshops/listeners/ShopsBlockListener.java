/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License. To view a copy of
 * this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send
 * a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View,
 * California, 94041, USA.
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
import net.milkbowl.vault.Vault;

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
						sign = new ShopSign(block, item.getName(), 1);
					} else if (event.getLine(1).equalsIgnoreCase("sell")) {
						sign = new ShopSign(block, item.getName(), 2);
					} else {
						// Add the sign to the Shop signlist and save the shop
						sign = new ShopSign(block, item.getName(), 0);
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

	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Block block = event.getBlock();

		// If not a sign ignore event.
		if ( (!(block.getType().equals(Material.SIGN_POST)) && !(block.getType().equals(Material.WALL_SIGN)) ) || event.isCancelled()) {
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

		if (!shop.getOwner().equals(player.getName()) && !(shop.getManagers().contains(player.getName())) && !(Vault.getPermission().has(player, PermType.ADMIN_LOCAL.get()))) {
			player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to place a sign or chest in the shop");
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
		if ((!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN) )) {
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
		if (block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN) )
			blockList.add(block);
		else {
			blockList.addAll(findWallSigns(block));
		}
		
		if (!shop.getOwner().equals(player.getName()) && !(shop.getManagers().contains(player.getName())) && !(Vault.getPermission().has(player, PermType.ADMIN_LOCAL.get()))) {
			player.sendMessage(ChatColor.DARK_AQUA + "You must be the shop owner or a manager to remove signs in the shop");
			event.setCancelled(true);
			return;
		} 
		
		//remove any Blocks in the blocklist
		if (!blockList.isEmpty()) {
			Iterator<ShopSign> iter = shop.getSigns().iterator();
			while (iter.hasNext()) {
				ShopSign sign = iter.next();
				for (Block b : blockList) 
					if (sign.getLoc().equals(b.getLocation())) 
						iter.remove();
			}
		} 
	}
	
	private List<Block> findWallSigns(Block block) {
		List<Block> foundSigns = new ArrayList<Block>(6);
		for (BlockFace face : BlockFace.values()) {
			if (block.getRelative(face).getType().equals(Material.WALL_SIGN))
				foundSigns.add(block.getRelative(face));
			else if (block.getRelative(face).getType().equals(Material.SIGN_POST))
				foundSigns.add(block.getRelative(face));
		}
		return foundSigns;
	}

}
