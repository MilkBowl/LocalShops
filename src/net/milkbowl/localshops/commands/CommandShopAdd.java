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

package net.milkbowl.localshops.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.objects.ItemInfo;
import net.milkbowl.localshops.objects.MsgType;
import net.milkbowl.localshops.objects.PermType;
import net.milkbowl.localshops.objects.Shop;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CommandShopAdd extends Command {

	public CommandShopAdd(LocalShops plugin, String commandLabel, CommandSender sender, String command, boolean isGlobal) {
		super(plugin, commandLabel, sender, command, isGlobal);
	}

	public CommandShopAdd(LocalShops plugin, String commandLabel, CommandSender sender, String[] command, boolean isGlobal) {
		super(plugin, commandLabel, sender, command, isGlobal);
	}

	public boolean process() {
		Shop shop = null;

		// Get current shop
		if (sender instanceof Player) {
			// Get player & data
			Player player = (Player) sender;

			shop = getCurrentShop(player);
			if (shop == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_NOT_IN_SHOP));
				return false;
			}

			// Check Permissions
			if (!canUseCommand(PermType.ADD)) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_USER_ACCESS_DENIED));
				return false;
			}            

			// Check if Player can Modify
			if (!isShopController(shop)) {
				player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_MUST_BE_SHOP_OWNER));
				player.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CURR_OWNER_IS, new String[] { "%OWNER%" }, new String[] { shop.getOwner() }));
				return true;
			}

			// add (player only command)
			Pattern pattern = Pattern.compile("(?i)add$");
			Matcher matcher = pattern.matcher(command);
			if (matcher.find()) {
				ItemStack itemStack = player.getItemInHand();
				if (itemStack == null) {
					return false;
				}
				ItemInfo item = Search.itemByStack(itemStack);
				int amount = itemStack.getAmount();
				if(item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return false;
				} else if (item.isDurable()) {
					if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
						sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_TOO_DAM, new String[] { "%ITEMNAME%" }, new String[] { item.getName() }));
						sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_DMG_LESS_THAN, new String[] { "%DAMAGEVALUE%" }, new String[] { String.valueOf(Config.getItemMaxDamage()) }));
						return true;
					}
				} else {
					item = Search.itemByStack(itemStack);
				}

				return shopAdd(shop, item, amount);
			}

			// add all (player only command)
			matcher.reset();
			pattern = Pattern.compile("(?i)add\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				ItemStack itemStack = player.getItemInHand();
				if (itemStack == null) {
					return false;
				}
				ItemInfo item = null;
				item = Search.itemByStack(itemStack);
				if(item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return false;
				}

				if (item.isDurable()) {
					if (calcDurabilityPercentage(itemStack) > Config.getItemMaxDamage() && Config.getItemMaxDamage() != 0) {
						sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_TOO_DAM, new String[] { "%ITEMNAME%" }, new String[] { item.getName() }));
						sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_DMG_LESS_THAN, new String[] { "%DAMAGEVALUE%" }, new String[] { String.valueOf(Config.getItemMaxDamage()) }));
						return true;
					}
				} 

				int amount = countItemsInInventory(player.getInventory(), itemStack);
				return shopAdd(shop, item, amount);
			}

			// add int all
			matcher.reset();
			pattern = Pattern.compile("(?i)add\\s+(\\d+)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				ItemInfo item = Search.itemById(id);
				if(item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return false;
				}
				int count = countItemsInInventory(player.getInventory(), item.toStack());
				return shopAdd(shop, item, count);
			}

			// add int:int all
			matcher.reset();
			pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				short type = Short.parseShort(matcher.group(2));
				ItemInfo item = Search.itemById(id, type);
				if(item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return false;
				}
				int count = countItemsInInventory(player.getInventory(), item.toStack());
				return shopAdd(shop, item, count);
			}

			// shop add name, ... all
			matcher.reset();
			pattern = Pattern.compile("(?i)add\\s+(.*)\\s+all$");
			matcher = pattern.matcher(command);
			if (matcher.find()) {
				String itemName = matcher.group(1);
				ItemInfo item = Search.itemByName(itemName);
				if(item == null) {
					sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
					return false;
				}
				int count = countItemsInInventory(player.getInventory(), item.toStack());
				return shopAdd(shop, item, count);
			}

		} else {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_CONSOLE_NOT_IMPLEMENTED));
			return false;
		}

		// Command matching     

		// add int
		Pattern pattern = Pattern.compile("(?i)add\\s+(\\d+)$");
		Matcher matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			ItemInfo item = Search.itemById(id);
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}            
			return shopAdd(shop, item, 0);
		}

		// add int int
		matcher.reset();
		pattern = Pattern.compile("(?i)add\\s+(\\d+)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			int count = Integer.parseInt(matcher.group(2));
			ItemInfo item = Search.itemById(id);
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}            
			return shopAdd(shop, item, count);
		}

		// add int:int
		matcher.reset();
		pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			ItemInfo item = Search.itemById(id, type);
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}
			return shopAdd(shop, item, 0);
		}

		// add int:int int
		matcher.reset();
		pattern = Pattern.compile("(?i)add\\s+(\\d+):(\\d+)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group(1));
			short type = Short.parseShort(matcher.group(2));
			ItemInfo item = Search.itemById(id, type);
			int count = Integer.parseInt(matcher.group(3));
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}
			return shopAdd(shop, item, count);
		}

		// shop add name, ... int
		matcher.reset();
		pattern = Pattern.compile("(?i)add\\s+(.*)\\s+(\\d+)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			String itemName = matcher.group(1);
			ItemInfo item = Search.itemByName(itemName);
			int count = Integer.parseInt(matcher.group(2));
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}
			return shopAdd(shop, item, count);
		}

		// shop add name, ...
		matcher.reset();
		pattern = Pattern.compile("(?i)add\\s+(.*)$");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			String itemName = matcher.group(1);
			ItemInfo item = Search.itemByName(itemName);
			if(item == null) {
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.GEN_ITEM_NOT_FOUND));
				return false;
			}
			return shopAdd(shop, item, 0);
		}

		// Show add help
		sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_USAGE, new String[] { "%CMDLABEL%" }, new String[] { commandLabel }));
		return true;
	}

	private boolean shopAdd(Shop shop, ItemInfo item, int amount) {
		Player player = null;

		// Assign in sender is a Player
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		// Calculate number of items player has
		if (player != null) {
			int playerItemCount = countItemsInInventory(player.getInventory(), item.toStack());
			// Validate Count
			if (playerItemCount < amount) {
				// Nag player
				log.info(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_INSUFFICIENT_INV, new String[] { "%ITEMCOUNT%" }, new Object[] { playerItemCount }));
				return false;
			}

			// If ALL (amount == -1), set amount to the count the player has
			if (amount == -1) {
				amount = playerItemCount;
			}
		}

		// If shop contains item
		if (shop.containsItem(item)) {
			// Check if stock is unlimited
			if (shop.isUnlimitedStock()) {
				// nicely message user
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_UNLIM_STOCK, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
				return true;
			}

			// Check if amount to be added is 0 (no point adding 0)
			if (amount == 0) {
				// nicely message user
				sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_ALREADY_HAS, new String[] { "%SHOPNAME%", "%ITEMNAME%" }, new Object[] { shop.getName(), item.getName() }));
				return true;
			}
		}

		// Add item to shop if needed
		if (!shop.containsItem(item)) {
			//Autoset item as dynamic if adding to a dynamic shop
			shop.addItem(item.getId(), item.getSubTypeId(), 0, 0, 0, 0, shop.isDynamicPrices());     
		}

		// Check stock settings, add stock if necessary
		if (shop.isUnlimitedStock()) {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_SUCCESS, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
		} else {
			shop.addStock(item, amount);
			shop.updateSigns(item);
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_STOCK_SUCCESS, new String[] { "%ITEMNAME%", "%STOCK%" }, new Object[] { item.getName(), shop.getItem(item).getStock() }));
		}

		if(amount == 0) {
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_READY0, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_READY1, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
			sender.sendMessage(plugin.getResourceManager().getString(MsgType.CMD_SHP_ADD_READY2, new String[] { "%ITEMNAME%" }, new Object[] { item.getName() }));
		}

		// log the transaction
		if (player != null) {
			int itemInv = shop.getItem(item).getStock();
			int startInv = itemInv - amount;
			if (startInv < 0) {
				startInv = 0;
			}
			plugin.getShopManager().logItems(player.getName(), shop.getName(), "add-item", item.getName(), amount, startInv, itemInv);

			// take items from player only if shop doesn't have unlim stock
			if(!shop.isUnlimitedStock()) {
				removeItemsFromInventory(player.getInventory(), item.toStack(), amount);
			}
		}
		plugin.getShopManager().saveShop(shop);
		return true;
	}
}