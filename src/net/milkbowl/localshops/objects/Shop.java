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

package net.milkbowl.localshops.objects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.Search;
import net.milkbowl.localshops.ShopManager;
import net.milkbowl.localshops.util.GenericFunctions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;


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
	protected HashMap<String, InventoryItem> inventory = new HashMap<String, InventoryItem>();
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

	public InventoryItem getItem(String item) {
		return inventory.get(item);
	}

	public InventoryItem getItem(ItemInfo item) {
		return inventory.get(item.name);
	}

	public boolean containsItem(ItemInfo item) {
		Iterator<InventoryItem> it = inventory.values().iterator();
		while(it.hasNext()) {
			InventoryItem invItem = it.next();
			if(invItem.getInfo().equals(item)) {
				return true;
			}
		}
		return false;
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
	 public boolean addItem(int itemNumber, short itemData, double buyPrice, int buyStackSize, double sellPrice, int sellStackSize,  int stock, int maxStock, boolean dynamicItem) {

		 ItemInfo item = Search.itemById(itemNumber, itemData);
		 if(item == null || sellStackSize < 1 || buyStackSize < 1) {
			 return false;
		 }

		 InventoryItem thisItem = new InventoryItem(item);

		 thisItem.setBuy(buyPrice, buyStackSize);
		 thisItem.setSell(sellPrice, sellStackSize);

		 thisItem.setStock(stock);
		 thisItem.setDynamic(dynamicItem);
		 thisItem.maxStock = maxStock;

		 if (inventory.containsKey(item.name)) {
			 inventory.remove(item.name);
		 }

		 inventory.put(item.name, thisItem);

		 return true;
	 }

	 public boolean addItem(int itemNumber, short itemData, double buyPrice, int buyStackSize, double sellPrice, int sellStackSize, int stock, int maxStock) {
		 return addItem(itemNumber, itemData, buyPrice, buyStackSize, sellPrice, sellStackSize, stock, maxStock, false);
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

	 public List<InventoryItem> getItems() {
		 return new ArrayList<InventoryItem>(inventory.values());
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

	public boolean addStock(String itemName, int amount) {
		 if (!inventory.containsKey(itemName)) {
			 return false;
		 }
		 inventory.get(itemName).addStock(amount);
		 return true;
	 }

	 public boolean removeStock(String itemName, int amount) {
		 if (!inventory.containsKey(itemName))
			 return false;
		 inventory.get(itemName).removeStock(amount);
		 return true;
	 }

	 public void setItemBuyPrice(String itemName, double price) {
		 inventory.get(itemName).setBuyPrice(price);
	 }

	 public void setItemBuyAmount(String itemName, int buySize) {
		 inventory.get(itemName).setBuySize(buySize);
	 }

	 public void setItemSellPrice(String itemName, double price) {
		 inventory.get(itemName).setSellPrice(price);
	 }

	 public void setItemSellAmount(String itemName, int sellSize) {
		 inventory.get(itemName).setSellSize(sellSize);
	 }    

	 /**
	  * Sets an item as dynamically adjustable
	  * 
	  * @param String itemName to set
	  */
	 public void setItemDynamic(String itemName) {
		 inventory.get(itemName).setDynamic(!inventory.get(itemName).isDynamic());
	 }

	 /**
	  * Checks if an item is set to dynamic pricing or not
	  * 
	  * @param String itemName to check
	  * @return Boolean dynamic
	  */
	 public boolean isItemDynamic(String itemName) {
		 return inventory.get(itemName).isDynamic();
	 }


	 /**
	  * Checks the number of dynamic items the shop contains.
	  * 
	  * @return int num of dynamic items
	  */
	 public int numDynamicItems() {
		 int num = 0;
		 for (InventoryItem item : this.getItems() ) {
			 if (item.isDynamic())
				 num++;
		 }
		 return num;   
	 }

	 public void removeItem(String itemName) {
		 inventory.remove(itemName);
	 }

	 public int itemMaxStock(String itemName) {
		 return inventory.get(itemName).maxStock;
	 }

	 public void setItemMaxStock(String itemName, int maxStock) {
		 inventory.get(itemName).maxStock = maxStock;
	 }

	 public Queue<Transaction> getTransactions() {
		 return transactions;
	 }

	 public void removeTransaction(Transaction trans) {
		 transactions.remove(trans);
	 }

	 public void addTransaction(Transaction trans) {
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

	 public Set<ShopSign> getSignSet() {
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

	 public void updateSigns(String itemName) {  
		 int index = 0;
		 for (ShopSign sign : this.signSet) {
			 if (sign.getItemName().equalsIgnoreCase(itemName)) {
				 updateSign(sign, index);
				 index++;
			 }
		 }
	 }

	 public void updateSigns(Set<ShopSign> signSet) {
		 int index = 0;
		 for (ShopSign sign : signSet) {
			 updateSign(sign, index);
			 index++;
		 }
	 }   

	 public String[] generateSignLines(ShopSign sign) {
		 String[] signLines = {"", "", "", ""};
		 //If this item no longer exists lets just return with blank lines
		 if (this.getItem(sign.getItemName()) == null) {
			 return signLines;
		 } else 
			 signLines[0] = sign.getItemName();

		 //create our string array and set the 1st element to our item name

		 //Store the variables we'll be using multiple times
		 int stock = this.getItem(sign.getItemName()).getStock();
		 double buyPrice = this.getItem(sign.getItemName()).getBuyPrice();
		 double sellPrice = this.getItem(sign.getItemName()).getSellPrice();
		 int maxStock = this.getItem(sign.getItemName()).getMaxStock();
		 String buyBundle = "";	
		 String sellBundle = "";

		 String bCol = GenericFunctions.parseColors(Config.getSignBuyColor());
		 String sCol = GenericFunctions.parseColors(Config.getSignSellColor());
		 String bunCol = GenericFunctions.parseColors(Config.getSignBundleColor());
		 String dCol = GenericFunctions.parseColors(Config.getSignDefaultColor());
		 String stoCol = GenericFunctions.parseColors(Config.getSignStockColor());

		 //Only set Bundle strings if they are greater than 1
		 if (this.getItem(sign.getItemName()).getBuySize() > 1) {
			 buyBundle = bunCol + "  [" + this.getItem(sign.getItemName()).getBuySize() + "]";
		 }
		 if (this.getItem(sign.getItemName()).getSellSize() > 1) {
			 sellBundle = bunCol + "  [" + this.getItem(sign.getItemName()).getSellSize() + "]";
		 }

		 //Colorize the title and strip it of vowels if it's too long
		 if (signLines[0].length() >= 12) {
			 signLines[0] = GenericFunctions.stripVowels(signLines[0]);
		 }
		 signLines[0] = GenericFunctions.parseColors(Config.getSignNameColor()) + signLines[0];
		 //If shop no longer carries this item - otherwise update it
		 if(this.getItem(sign.getItemName()) == null) {
			 signLines[0] = "";
			 signLines[1] = "";
			 signLines[2] = "";
			 signLines[3] = "";
			 this.signSet.remove(sign);
		 } else if (sign.getType() == ShopSign.SignType.INFO ){
			 if (buyPrice == 0 ) 
				 signLines[1] = bCol + "-";
			 else if (stock == 0 && !this.unlimitedStock)
				 signLines[1] = dCol + "Understock";
			 else 
				 signLines[1] =  bCol + numFormat.format(buyPrice) + buyBundle;
			 if (sellPrice == 0 ) 
				 signLines[2] = stoCol + "-";
			 else if (maxStock > 0 && stock >= maxStock && !this.unlimitedStock)
				 signLines[2] = dCol + "Overstock";
			 else 
				 signLines[2] = sCol + numFormat.format(sellPrice) + sellBundle;

			 if (!this.unlimitedStock)
				 signLines[3] = dCol + "Stock: " + stoCol + stock;
			 else
				 signLines[3] = stoCol + "Unlimited";
		 } else if (sign.getType() == ShopSign.SignType.BUY ) {
			 if (buyPrice == 0 ) 
				 signLines[1] = dCol + "Not Selling";
			 else if (stock == 0 && !this.unlimitedStock) 
				 signLines[1] = dCol + "Understock";
			 else  {
				 signLines[1] = sCol + numFormat.format(buyPrice) + buyBundle;
				 signLines[3] = dCol + "R-Clk to Buy";
			 }
			 if (!this.unlimitedStock)
				 signLines[2] = dCol + "Stock: " + stoCol + stock;
			 else
				 signLines[2] = stoCol + "Unlimited";         
		 } else if (sign.getType() == ShopSign.SignType.SELL ) {
			 if (sellPrice == 0 ) 
				 signLines[1] = dCol + "Not Buying";
			 else if (maxStock > 0 && stock >= maxStock && !this.unlimitedStock)
				 signLines[1] = dCol + "Overstock";
			 else {
				 signLines[1] = sCol + numFormat.format(sellPrice) + sellBundle;
				 signLines[3] = dCol + "R-Clk to Sell";
			 }
			 if (!this.unlimitedStock)
				 signLines[2] = dCol + "Stk: " + stoCol + stock;
			 else
				 signLines[2] = stoCol + "Unlimited";   
		 }

		 return signLines;
	 }
}