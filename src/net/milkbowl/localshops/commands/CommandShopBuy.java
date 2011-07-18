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
import net.milkbowl.localshops.objects.Item;
import net.milkbowl.localshops.objects.ShopRecord;
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
		//Check for player immediately, we don't want to logic anything if this isn't a player..
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PLAYERS_ONLY));
			return false;
		}

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
				ItemInfo item = Search.itemByStack(itemStack);
				if (item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return true;
				}
				return shopBuy(shop, item, 1);
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
					count = shop.getItem(item).getStock();
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
					count = shop.getItem(item).getStock();
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
				Item item = Search.itemById(id, type);
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
					count = shop.getItem(item).getStock();
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
				Item item = Search.itemByName(itemName);
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
					count = shop.getItem(item).getStock();
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
			Item item = Search.itemById(id);
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
			Item item = Search.itemById(id);
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
			Item item = Search.itemById(id, type);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			return shopBuy(shop, item, 1);
		}

		// buy int:int int
		matcher.reset();
		pattern = Pattern.compile("(?i)buy\\s+(\\d+):(\\d+)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			Item item = Search.itemById(id, type);
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
			Item item = Search.itemByName(itemName);
			if (item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return true;
			}
			return shopBuy(shop, item, 1);
		}

		// Show sell help
		sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_USAGE, new String[] { "%COMMANDLABEL%" }, new Object[] { commandLabel }));
		return true;
	}
	private boolean shopBuy(Shop shop, Item item, int amount) {
		Player player = (Player) sender;
		ShopRecord invItem = shop.getItem(item);
		int startStock = invItem.getStock();
		//initial order request.

		// check if the shop is selling that item
		// check if the item has a price, or if this is a shop owner
		if (invItem == null || invItem.getBuyPrice() == 0) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		} else if (invItem.getStock() == 0 && !shop.isUnlimitedStock()) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_SOLD_OUT, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		} else if (invItem.getBuyPrice() == 0 && !isShopController(shop)) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_SHOP_NOT_SELLING, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
			return false;
		}

		amount = getBuyAmount(player, amount, item, shop);
		if (amount <= 0)
			return false;

		double totalCost = amount * invItem.getBuyPrice();

		/**
		 * Attempt the transaction - if it errors at this point then there is a serious issue.
		 * 
		 * No need for nested ifs.  If the shop is not unlimited money it shouldn't ever try to charge the player.
		 */
		if (shop.isUnlimitedMoney() && !shop.getOwner().equals(player.getName())) {
			if (!Econ.chargePlayer(player.getName(), totalCost)) {
				player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
				return true;
			}
		} else if (!shop.getOwner().equals(player.getName()) && !Econ.payPlayer(player.getName(), shop.getOwner(), totalCost)) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_UNEXPECTED_MONEY_ISSUE));
			return true;
		}
		
		if (!shop.isUnlimitedStock())
			shop.removeStock(item.getName(), amount);

		if (isShopController(shop)) {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_REMOVED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%" }, new Object[] { amount, item.getName() }));
		} else {
			player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_PURCHASED_QTY, new String[] { "%AMOUNT%", "%ITEMNAME%", "%COST%" }, new Object[] { amount, item.getName(), Vault.getEconomy().format(totalCost) }));
		}

		//Do our give stock stuff here

		plugin.getShopManager().logItems(player.getName(), shop.getName(), "buy-item", item.getName(), amount, startStock, invItem.getStock());
		shop.addTransaction(new Transaction(Transaction.Type.Sell, player.getName(), item.getName(), amount, totalCost));

		givePlayerItem(item, amount);
		plugin.getShopManager().saveShop(shop);

		//update any sign in this shop with that value.
		shop.updateSigns(shop.getSigns());

		return true;
	}

	private int getBuyAmount(Player player, int amount, Item item, Shop shop) {

		int originalAmount = amount;
		//Get the total amount of stock in the shop

		//Lower our amount if the shop doesn't have enough stock to sell what was requested.
		if (!shop.isUnlimitedStock() && amount > shop.getItem(item).getStock())
			amount = shop.getItem(item).getStock();;

			// check how many items the user has room for
			int freeSpots = 0;
			for (ItemStack thisSlot : player.getInventory().getContents()) {
				if (thisSlot == null || thisSlot.getType().equals(Material.AIR)) {
					//Adjust number of items slots by the number an air block can hold
					freeSpots += item.getMaxBundleSize();
					continue;
				}else if (thisSlot.getType().equals(item.getType()) && thisSlot.getDurability() == item.getSubTypeId()) {
					freeSpots += item.getMaxBundleSize() - thisSlot.getAmount();
				}
			}
			//If player doesn't have enough slots free reduce the amount they can buy.
			if (amount > freeSpots)
				amount = freeSpots;


			//Return with special amount if this is the shop owner, 
			//honestly the shop owner should be using /remove, but this is for compatibility.
			if (player.getName().equals(shop.getOwner()) && !shop.isUnlimitedStock())
				return amount;
			else if (player.getName().equals(shop.getOwner()) && shop.isUnlimitedStock())
				return 0;

			//Check player econ
			double totalPrice = shop.getItem(item).getBuyPrice() * amount;
			if (totalPrice > Econ.getBalance(player.getName())) {
				amount = (int) Math.floor(Econ.getBalance(player.getName()) / shop.getItem(item).getBuyPrice());
			}

			if (amount < originalAmount)
				player.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_BUY_ORDER_REDUCED, new String[] { "%BUNDLESIZE%", "%AMOUNT%" }, new Object[] { 1, amount }));


			return amount;
	}
}