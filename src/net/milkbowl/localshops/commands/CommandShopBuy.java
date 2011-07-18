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

package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.ShopItem;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.Transaction;
import net.milkbowl.localshops.util.Econ;
import net.milkbowl.vault.Vault;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CommandShopBuy extends Command {

	public CommandShopBuy(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
		super(plugin, commandLabel, sender, command, isGlobal);
	}

	public CommandShopBuy(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
		super(plugin, commandLabel, sender, command, isGlobal);
	}

	public boolean process() {
		Shop shop = null;

		// Get current shop
		if (sender instanceof Player) {
			// Get player & data
			Player player = (Player) sender;
			shop = getCurrentShop(player);
			if (shop == null || (isGlobal && !Config.getGlobalShopsEnabled())) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
				return true;
			}

			// Check Permissions
			if ((!canUseCommand(PermType.BUY) && !isGlobal) || (!canUseCommand(PermType.GLOBAL_BUY) && isGlobal)) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
				return true;
			}

			// buy (player only command)
			Pattern pattern = Pattern.compile("(?i)buy$");
			Matcher matcher = pattern.matcher(command);
			if (matcher.find()) {
				ItemStack itemStack = player.getItemInHand();
				if (itemStack == null) {
					return false;
				}
				ItemInfo item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				return shopBuy(shop, item, 0);
			}

			// buy all (player only command)
			matcher.reset();
			pattern = Pattern.compile("(?i)buy\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				ItemStack itemStack = player.getItemInHand();
				if (itemStack == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_NO_ITEM_IN_HAND));
					return true;
				}
				ItemInfo item = Search.itemById(itemStack.getTypeId(), itemStack.getDurability());
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				int count;
				if (shop.isUnlimitedStock()) {
					// get player avail space
					count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
				} else {
					// use shop stock
					count = shop.getItem(item.getName()).getStock();
				}

				return shopBuy(shop, item, count);
			}

			// buy int all
			matcher.reset();
			pattern = Pattern.compile("(?i)buy\\s+(\\d+)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				ItemInfo item = Search.itemById(id);
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				int count;
				if (shop.isUnlimitedStock()) {
					// get player avail space
					count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
				} else {
					// use shop stock
					count = shop.getItem(item.getName()).getStock();
				}
				if (count < 1) {
					//
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
					return true;
				}
				return shopBuy(shop, item, count);
			}

			// buy int:int all
			matcher.reset();
			pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				short type = Short.parseShort(matcher.group(2));
				ItemInfo item = Search.itemById(id, type);
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				int count;
				if (shop.isUnlimitedStock()) {
					// get player avail space
					count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
				} else {
					// use shop stock
					count = shop.getItem(item.getName()).getStock();
				}
				if (count < 1) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
					return true;
				}
				return shopBuy(shop, item, count);
			}

			// buy name, ... all
			matcher.reset();
			pattern = Pattern.compile("(?i)buy\\s+(.*)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				String itemName = matcher.group(1);
				ItemInfo item = Search.itemByName(itemName);
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				int count;
				if (shop.isUnlimitedStock()) {
					// get player avail space
					count = countAvailableSpaceForItemInInventory(player.getInventory(), item);
				} else {
					// use shop stock
					count = shop.getItem(item.getName()).getStock();
				}
				if (count < 1) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
					return true;
				}
				return shopBuy(shop, item, count);
			}

		} else {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
			return true;
		}

		// Command matching

		// buy int
		Pattern pattern = Pattern.compile("(?i)buy\\s+(\\d+)$");
		Matcher matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			ItemInfo item = Search.itemById(id);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			return shopBuy(shop, item, 0);
		}

		// buy int int
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(\\d+)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			int count = Integer.parseInt(matcher.group(2));
			ItemInfo item = Search.itemById(id);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			if (count < 1) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
				return true;
			}
			return shopBuy(shop, item, count);
		}

		// buy int:int
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			ItemInfo item = Search.itemById(id, type);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			return shopBuy(shop, item, 0);
		}

		// buy int:int int
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			ItemInfo item = Search.itemById(id, type);
			int count = Integer.parseInt(matcher.group(3));
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			if (count < 1) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
				return true;
			}
			return shopBuy(shop, item, count);
		}

		// buy name, ... int
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(.*)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			String itemName = matcher.group(1);
			ItemInfo item = Search.itemByName(itemName);
			int count = Integer.parseInt(matcher.group(2));
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			if (count < 1) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_MINIMUM_ONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() } ));
				return true;
			}
			return shopBuy(shop, item, count);
		}

		// buy name, ...
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(.*)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			String itemName = matcher.group(1);
			ItemInfo item = Search.itemByName(itemName);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			return shopBuy(shop, item, 0);
		}

		// Show sell help
		sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_USAGE, new String[] { "%COMMANDLABEL%" }, new Object[] { commandLabel }));
		return true;
	}

	private boolean shopBuy(Shop shop, ItemInfo item, int amount) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYERS_ONLY));
			return false;
		}

		Player player = (Player) sender;
		ShopItem invItem = shop.getItem(item.getName());

		// check if the shop is selling that item
		if (invItem == null || invItem.getBuyPrice() == 0) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		} else if (invItem.getStock() == 0 && !shop.isUnlimitedStock()) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_SOLD_OUT, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		}

		// check if the item has a price, or if this is a shop owner
		if (invItem.getBuyPrice() == 0 && !isShopController(shop)) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		}

		
		//Get what the player can actually buy
		amount = getBuyAmount(player, amount, invItem, shop.isUnlimitedStock());
			

		
		int bundles = amount / invItem.getBuySize();
		// recalculate # of items since may not fit cleanly into bundles
		amount = bundles * invItem.getBuySize();
		
		//cost Related stuff
		double itemPrice = invItem.getBuyPrice();
		double totalCost = bundles * itemPrice;
		boolean success = false;
		
		//TODO: Instead of attempting a sale on the original amount of items - we should check how much they can buy regardless of transaction and adjust the data down
		if (shop.isUnlimitedMoney()) {
			if (!Econ.chargePlayer(player.getName(), totalCost)) {
				// player doesn't have enough money
				// get player's balance and calculate how many it can buy
				double playerBalance = Econ.getBalance(player.getName());
				int bundlesCanAford = (int) Math.floor(playerBalance / itemPrice);
				totalCost = bundlesCanAford * itemPrice;
				amount = bundlesCanAford * invItem.getBuySize();
				if(bundlesCanAford == 0) {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYER_AFFORD_NONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
					return true;
				} else {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYER_AFFORD_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { bundlesCanAford, item.getName() }));
				}
				if (!Econ.chargePlayer(player.getName(), totalCost)) {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
					return true;
				} else {
					success = true;
				}
			} else {
				success = true;
			}
		} 
		// try to pay the shop owner
		else if (!isShopController(shop)) {
			if (!Econ.payPlayer(player.getName(), shop.getOwner(), totalCost)) {
				// player doesn't have enough money
				// get player's balance and calculate how many it can buy
				double playerBalance = Econ.getBalance(player.getName());
				int bundlesCanAford = (int) Math.floor(playerBalance / itemPrice);
				totalCost = bundlesCanAford * itemPrice;
				amount = bundlesCanAford * invItem.getBuySize();

				if(bundlesCanAford == 0) {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYER_AFFORD_NONE, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
					return true;
				} else {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYER_AFFORD_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { bundlesCanAford, item.getName() }));
				}
				if (!Econ.payPlayer(player.getName(), shop.getOwner(), totalCost)) {
					player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
					return true;
				} else {
					success = true;
				}
			} else {
				success = true;
			}
		}
		//If successful transaction lets do profit sharing!
		if (success && shop.getSharePercent() > 0 && shop.getManagers().size() > 0) {
			double shareAmount = totalCost * (shop.getSharePercent() / 100) / shop.getManagers().size();
			for (String manager : shop.getManagers()) {
				if (shop.isUnlimitedMoney())
					Vault.getEconomy().depositPlayer(manager, shareAmount);
				else
					Econ.payPlayer(shop.getOwner(), manager, shareAmount);
			}
		}

		if (!shop.isUnlimitedStock()) {
			shop.removeStock(item.getName(), amount);
		}
		if (isShopController(shop)) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_REMOVED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { amount, item.getName() }));
		} else {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PURCHASED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%", "%COST%" }, new Object[] { amount, item.getName(), Vault.getEconomy().format(totalCost) }));
		}

		// log the transaction
		int stock = invItem.getStock();
		int startStock = stock + amount;
		if (shop.isUnlimitedStock()) {
			startStock = 0;
		}
		plugin.getShopManager().logItems(player.getName(), shop.getName(), "buy-item", item.getName(), amount, startStock, stock);
		shop.addTransaction(new Transaction(Transaction.Type.Sell, player.getName(), item.getName(), amount, totalCost));

		givePlayerItem(item.toStack(), amount);
		plugin.getShopManager().saveShop(shop);

		//update any sign in this shop with that value.
		shop.updateSigns(shop.getSigns());

		return true;
	}
	
	private int getBuyAmount(Player player, int amount, ShopItem invItem, boolean unlimitedStock) {
		
		int originalAmount = amount;
		// if amount <= 0, assume single stack size
		if (amount <= 0) {
			amount = invItem.getBundleSize();
		}

		//Get the total amount of stock in the shop
		int totalStock = invItem.getStock();
		
		//If this is an unlimited shop set the total number of items to the amount being requested
		if (unlimitedStock) {
			totalStock = amount;
		}
		
		if (totalStock == 0 && !unlimitedStock) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_HAS_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { totalStock, invItem.getType().name() }));
			return totalStock;
		}

		// check how many items the user has room for
		int freeSpots = 0;
		for (ItemStack thisSlot : player.getInventory().getContents()) {
			if (thisSlot == null || thisSlot.getType().equals(Material.AIR)) {
				//Adjust number of items slots by the number an air block can hold
				freeSpots += invItem.getMaxStock();
				continue;
			}else if (thisSlot.getType().equals(invItem.getType()) && thisSlot.getDurability() == invItem.getSubTypeId()) {
				freeSpots += invItem.getMaxStock() - thisSlot.getAmount();
			}
		}
		// Calculate the amount the player can store
		if (amount > freeSpots) {
			amount = freeSpots - (freeSpots % invItem.getBundleSize());
		}
		if (amount > totalStock && !unlimitedStock) {
			//normalize the amount to the buy bundle size if it's greater than the number in the shop
			amount = totalStock - (totalStock % invItem.getBundleSize());
		} 
		if (amount % invItem.getBundleSize() != 0) {
			//Make sure we conform to shop bundle size
			amount = amount - (amount % invItem.getBuySize());
		}
		if (amount < originalAmount)
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_ORDER_REDUCED, new String[] { "%BUNDLESIZE%", "%AMOUNT%" }, new Object[] { invItem.getBundleSize(), amount }));
			
		
		return amount;
	}
}