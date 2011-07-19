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

package net.milkbowl.localshops.objects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.ShopManager;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;


public abstract class Shop implements Comparator<Shop> {
	// Attributes
	protected UUID uuid = null;
	protected String name = null;
	protected String owner = null;
	protected String creator = null;
	protected ArrayList<String> managers = new ArrayList<String>();
	protected boolean unlimitedMoney = false;
	protected boolean unlimitedStock = false;
	protected boolean dynamicPrices = false;
	protected HashMap<Item, ShopRecord> inventory = new HashMap<Item, ShopRecord>();
	protected double minBalance = 0;
	protected double sharePercent = 0;
	protected ArrayBlockingQueue<Transaction> transactions;
	protected boolean notification = true;
	protected Set<ShopSign> signSet = Collections.synchronizedSet(new HashSet<ShopSign>());
	protected Set<String> groups = Collections.synchronizedSet(new HashSet<String>());
	protected Set<String> users = Collections.synchronizedSet(new HashSet<String>());

	private static final NumberFormat numFormat = new DecimalFormat("0.##");

	// Logging
	protected static final Logger log = Logger.getLogger("Minecraft");

	public Shop(UUID uuid) {
		this.uuid = uuid;
		transactions = new ArrayBlockingQueue<Transaction>(Config.getShopTransactionMaxSize());
	}

	public UUID getUuid() {
		return uuid;
	}

	/**
	 * Sets the name of the shop
	 * 
	 * @param String name of the shop
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the Shop
	 * 
	 * @return String name of the shop
	 */
	public String getName() {
		return name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOwner() {
		return owner;
	}

	public String getCreator() {
		return creator;
	}

	public void setUnlimitedStock(boolean b) {
		unlimitedStock = b;
	}

	public void setUnlimitedMoney(boolean b) {
		unlimitedMoney = b;
	}

	public void setDynamicPrices(boolean dynamicPrices) {
		this.dynamicPrices = dynamicPrices;
	}

	public ShopRecord getItem(Item item) {
		return inventory.get(item);
	}

	public boolean containsItem(Item item) {
		return (inventory.containsKey(item));
	}

	public String getShortUuidString() {
		String sUuid = uuid.toString();
		return sUuid.substring(sUuid.length() - Config.getUuidMinLength());
	}

	/**
	 * Gets the minimum account balance this shop allows.
	 * 
	 * @return int minBalance
	 */
	public double getMinBalance() {
		return this.minBalance;
	}

	/**
	 * Sets the minBalance this shop allows.
	 * 
	 * @param int newBalance
	 */
	public void setMinBalance(double newBalance) {
		this.minBalance = newBalance;
	}

	public void setNotification(boolean setting) {
		this.notification = setting;
	}

	public boolean getNotification() {
		return notification;
	}
	/**
	 * @param id
	 * @param type
	 * @param buyPrice
	 * @param buyStackSize
	 * @param sellPrice
	 * @param sellStackSize
	 * @param currStock
	 * @param maxStock
	 * @param dynamicItem
	 * @return
	 */

	public boolean addItem(int itemNumber, short itemData, double buyPrice, double sellPrice, int stock, int maxStock, boolean dynamicItem) {

		ItemInfo item = Search.itemById(itemNumber, itemData);
		ShopRecord thisItem = new ShopRecord(buyPrice, sellPrice, stock, maxStock, dynamicItem);

		inventory.put(item, thisItem);

		return true;
	}

	public boolean addItem(int itemNumber, short itemData, double buyPrice, double sellPrice, int stock, int maxStock) {
		return addItem(itemNumber, itemData, buyPrice, sellPrice, stock, maxStock, false);
	}

	public void setManagers(String[] managers) {
		this.managers = new ArrayList<String>();

		for (String manager : managers) {
			if (!manager.equals("")) {
				this.managers.add(manager);
			}
		}
	}

	public void addManager(String manager) {
		managers.add(manager);
	}

	public void removeManager(String manager) {
		managers.remove(manager);
	}

	public void addUser(String user) {
		users.add(user);
	}

	public void removeUser(String user) {
		users.remove(user);
	}

	public void addGroup(String group) {
		groups.add(group);
	}

	public void removeGroup(String group) {
		groups.remove(group);
	}

	public List<String> getManagers() {
		return managers;
	}

	public List<Item> getItems() {
		return new ArrayList<Item>(inventory.keySet());
	}

	public Set<String> getGroupSet() {
		return groups;
	}

	public Set<String> getUserSet() {
		return users;
	}

	public boolean isUnlimitedStock() {
		return unlimitedStock;
	}

	public boolean isUnlimitedMoney() {
		return unlimitedMoney;
	}

	/**
	 * True if the shop is set to dynamic
	 * 
	 * @return Boolean dynamicPrices 
	 */
	public boolean isDynamicPrices() {
		return dynamicPrices;
	}

	public double getSharePercent() {
		return sharePercent;
	}

	public void setSharePercent(double sharePercent) {
		this.sharePercent = sharePercent;
	}

	public boolean addStock(Item item, int amount) {
		if (!inventory.containsKey(item)) {
			return false;
		}
		inventory.get(item).addStock(amount);
		return true;
	}

	public boolean removeStock(Item item, int amount) {
		if (!inventory.containsKey(item))
			return false;
		inventory.get(item).removeStock(amount);
		return true;
	}

	public void setItemBuyPrice(Item item, double price) {
		inventory.get(item).setBuyPrice(price);
	}

	public void setItemSellPrice(Item item, double price) {
		inventory.get(item).setSellPrice(price);
	}

	/**
	 * Sets an item as dynamically adjustable
	 * 
	 * @param String itemName to set
	 */
	public void setItemDynamic(Item item) {
		inventory.get(item).setDynamic(!inventory.get(item).isDynamic());
	}

	/**
	 * Checks if an item is set to dynamic pricing or not
	 * 
	 * @param String itemName to check
	 * @return Boolean dynamic
	 */
	public boolean isItemDynamic(Item item) {
		return inventory.get(item).isDynamic();
	}


	/**
	 * Checks the number of dynamic items the shop contains.
	 * 
	 * @return int num of dynamic items
	 */
	public int numDynamicItems() {
		int num = 0;
		for (ShopRecord item : inventory.values() ) {
			if (item.isDynamic())
				num++;
		}
		return num;   
	}

	public void removeItem(Item item) {
		inventory.remove(item);
	}

	public int itemMaxStock(Item item) {
		return inventory.get(item).getMaxStock();
	}

	public void setItemMaxStock(Item item, int maxStock) {
		inventory.get(item).setMaxStock(maxStock);
	}

	public Queue<Transaction> getTransactions() {
		return transactions;
	}

	public void removeTransaction(Transaction trans) {
		transactions.remove(trans);
	}

	public void addTransaction(Transaction trans) {
		switch(trans.type) {
		case Buy:
			log.info(String.format("%s to %s (%s)", trans.toString(), name, getShortUuidString()));
			break;
		case Sell:
			log.info(String.format("%s form %s (%s)", trans.toString(), name, getShortUuidString()));
			break;
		}
		if(transactions.remainingCapacity() >= 1) {
			transactions.add(trans);
		} else {
			transactions.remove();
			transactions.add(trans);
		}
	}

	public void clearTransactions() {
		transactions.clear();
	}

	public abstract String toString();
	public abstract void log();

	@Override
	public int compare(Shop o1, Shop o2) {
		return o1.getUuid().compareTo(o2.uuid);
	}

	public void setSignSet(Set<ShopSign> signSet) {
		this.signSet = signSet;
	}

	public Set<ShopSign> getSigns() {
		return signSet;
	}

	public void updateSign(ShopSign sign, int delay) {
		String[] signLines = generateSignLines(sign);

		BlockState signState = sign.getLoc().getBlock().getState();
		//Set the lines
		((Sign) signState).setLine(0, signLines[0]);
		((Sign) signState).setLine(1, signLines[1]);
		((Sign) signState).setLine(2, signLines[2]);
		((Sign) signState).setLine(3, signLines[3]);

		ShopManager.plugin.scheduleUpdate(sign, 2*delay);

	}

	public void updateSign(ShopSign sign) {
		updateSign(sign, 0);
	}

	public void updateSign(Location loc) {
		for (ShopSign sign : this.signSet) {
			if (sign.getLoc().equals(loc)) {
				updateSign(sign);
				break;
			}   
		}
	}

	public void updateSign(Block block) {
		updateSign(block.getLocation());
	}

	public void updateSigns(Item item) {  
		int index = 0;
		for (ShopSign sign : this.signSet) {
			if (sign.getItemName().equalsIgnoreCase(item.getName())) {
				updateSign(sign, index);
				index++;
			}
		}
	}

	/**
	 * Only checks if a player is on the shop access list, or an owner, manager, or admin.
	 * This IS NOT used for checking permission to use a command, ONLY for checking access list.
	 * 
	 * @param player
	 * @return
	 */
	public boolean hasAccess(Player player) {
		if (owner.equals(player.getName()))
			return true;
		else if (managers.contains(player.getName()))
			return true;
		else if (users.isEmpty() && groups.isEmpty())
			return true;
		else if (users.contains(player.getName()))
			return true;
		else if (groups.contains(LocalShops.getPerm().getPlayerGroups(player.getWorld().getName(), player.getName())))
			return true;
		else
			return false;
	}

	public void updateSigns(Set<ShopSign> signSet) {
		int index = 0;
		for (ShopSign sign : signSet) {
			updateSign(sign, index);
			index++;
		}
	}   

	//TODO: needs massive update.
	public String[] generateSignLines(ShopSign sign) {
		String[] signLines = {"", "", "", ""};
		//If this item no longer exists lets just return with blank lines
		ItemInfo item = Search.itemByName(sign.getItemName());
		ShopRecord shopRecord = null;
		if (item == null) {
			this.signSet.remove(sign);
			return signLines;
		} else if (this.getItem(item) == null) {
			this.signSet.remove(sign);
			return signLines;
		} else {
			shopRecord = this.getItem(item);
			signLines[0] = sign.getItemName();
		}

		//create our string array and set the 1st element to our item name

		//Store the variables we'll be using multiple times
		int stock = shopRecord.getStock();
		double buyPrice = shopRecord.getBuyPrice() * sign.getAmount();
		double sellPrice = shopRecord.getSellPrice() * sign.getAmount();
		int maxStock = shopRecord.getMaxStock();
		int available = stock / sign.getAmount();

		String bCol = GenericFunctions.parseColors(Config.getSignBuyColor());
		String sCol = GenericFunctions.parseColors(Config.getSignSellColor());
		String aCol = GenericFunctions.parseColors(Config.getSignBundleColor());
		String dCol = GenericFunctions.parseColors(Config.getSignDefaultColor());
		String stoCol = GenericFunctions.parseColors(Config.getSignStockColor());


		//Colorize the title and strip it of vowels if it's too long
		if (signLines[0].length() >= 12) {
			signLines[0] = GenericFunctions.stripVowels(signLines[0]);
		}

		signLines[0] = GenericFunctions.parseColors(Config.getSignNameColor()) + signLines[0];

		if (sign.getType() == ShopSign.SignType.INFO ){
			if (buyPrice == 0 ) 
				signLines[1] = bCol + "-";
			else if (stock == 0 && !this.unlimitedStock)
				signLines[1] = dCol + "Understock";
			else 
				signLines[1] =  bCol + numFormat.format(buyPrice);
			if (sellPrice == 0 ) 
				signLines[2] = stoCol + "-";
			else if (maxStock > 0 && stock >= maxStock && !this.unlimitedStock)
				signLines[2] = dCol + "Overstock";
			else 
				signLines[2] = sCol + numFormat.format(sellPrice);

			if (!this.unlimitedStock)
				signLines[3] = dCol + "Stk: " + stoCol + stock;
			else
				signLines[3] = stoCol + "Unlimited";
		} else if (sign.getType() == ShopSign.SignType.BUY ) {
			if (buyPrice == 0 ) 
				signLines[1] = dCol + "Not Selling";
			else if (stock == 0 && !this.unlimitedStock) 
				signLines[1] = dCol + "Understock";
			else  {
				signLines[1] = sCol + numFormat.format(buyPrice);
				signLines[2] = dCol + "BuyAmt: " + aCol + sign.getAmount();
			}
			if (!this.unlimitedStock)
				signLines[3] = dCol + "Stk: " + stoCol + available;
			else
				signLines[3] = stoCol + "Unlimited";         
		} else if (sign.getType() == ShopSign.SignType.SELL ) {
			if (sellPrice == 0 ) 
				signLines[1] = dCol + "Not Buying";
			else if (maxStock > 0 && stock >= maxStock && !this.unlimitedStock)
				signLines[1] = dCol + "Overstock";
			else {
				signLines[1] = sCol + numFormat.format(sellPrice);
				signLines[2] = dCol + "SellAmt: " + aCol + sign.getAmount();
			}
			if (!this.unlimitedStock)
				signLines[3] = dCol + "Stk: " + stoCol + available;
			else
				signLines[3] = stoCol + "Unlimited";   
		}

		return signLines;
	}
}