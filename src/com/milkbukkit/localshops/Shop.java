package com.milkbukkit.localshops;

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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.milkbukkit.localshops.util.GenericFunctions;

public class Shop implements Comparator<Shop> {
    // Attributes
    private UUID uuid = null;
    private String world = null;
    private String name = null;
    private ShopLocation locationA = null;
    private ShopLocation locationB = null;
    private String owner = null;
    private String creator = null;
    private ArrayList<String> managers = new ArrayList<String>();
    private boolean unlimitedMoney = false;
    private boolean unlimitedStock = false;
    private boolean dynamicPrices = false;
    private HashMap<String, InventoryItem> inventory = new HashMap<String, InventoryItem>();
    private double minBalance = 0;
    private ArrayBlockingQueue<Transaction> transactions;
    private boolean notification = true;
    private int locationLowX, locationHighX, locationLowY, locationHighY, locationLowZ, locationHighZ;
    private Set<ShopSign> signSet = Collections.synchronizedSet(new HashSet<ShopSign>());
    private boolean global = false;

    // Logging
    private static final Logger log = Logger.getLogger("Minecraft");    

    public Shop(UUID uuid) {
        this.uuid = uuid;
        transactions = new ArrayBlockingQueue<Transaction>(Config.getShopTransactionMaxSize());
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the World the shop is located in
     * 
     * @param String world of the shop
     */
    public void setWorld(String name) {
        world = name;
    }

    /**
     * Returns the name of the world the shop is located in
     * 
     * @return String world of the shop
     */
    public String getWorld() {
        return world;
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

    public void setLocations(ShopLocation locationA, ShopLocation locationB) {
        this.locationA = locationA;
        this.locationB = locationB;

        organizeLocation();
    }

    public void setLocationA(ShopLocation locationA) {
        this.locationA = locationA;
        organizeLocation();
    }

    public void setLocationA(int x, int y, int z) {
        locationA = new ShopLocation(x, y, z);
        organizeLocation();
    }

    public void setLocationB(ShopLocation locationB) {
        this.locationB = locationB;
        organizeLocation();
    }

    public void setLocationB(int x, int y, int z) {
        locationB = new ShopLocation(x, y, z);
        organizeLocation();
    }

    public ShopLocation[] getLocations() {
        return new ShopLocation[] { locationA, locationB };
    }

    public ShopLocation getLocationA() {
        return locationA;
    }

    public ShopLocation getLocationB() {
        return locationB;
    }

    public ShopLocation getLocationCenter() {
        int[] xyz = new int[3];
        int[] xyzA = locationA.toArray();
        int[] xyzB = locationA.toArray();

        for (int i = 0; i < 3; i++) {
            if (xyzA[i] < xyzB[i]) {
                xyz[i] = xyzA[i] + (Math.abs(xyzA[i] - xyzB[i])) / 2;
            } else {
                xyz[i] = xyzA[i] - (Math.abs(xyzA[i] - xyzB[i])) / 2;
            }
        }

        return new ShopLocation(xyz);
    }

    public int getLocationLowX() {
        return locationLowX;
    }

    public int getLocationLowY() {
        return locationLowY;
    }

    public int getLocationLowZ() {
        return locationLowZ;
    }

    public int getLocationHighX() {
        return locationHighX;
    }

    public int getLocationHighY() {
        return locationHighY;
    }

    public int getLocationHighZ() {
        return locationHighZ;
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

    public List<String> getManagers() {
        return managers;
    }

    public List<InventoryItem> getItems() {
        return new ArrayList<InventoryItem>(inventory.values());
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

    public String toString() {
        if(global) {
            return String.format("Shop \"%s\" in world \"%s\" %d items - %s", this.name, world, inventory.size(), uuid.toString());
        } else {
            String locA = "";
            String locB = "";
            if(locA != null) {
                locA = locationA.toString();
            }
            if(locB != null) {
                locB = locationB.toString();
            }
            return String.format("Shop \"%s\" at [%s], [%s] %d items - %s", this.name, locA, locB, inventory.size(), uuid.toString());
        }
    }

    public void log() {
        // Details
        log.info("Shop Information");
        log.info(String.format("   %-16s %s", "UUID:", uuid.toString()));
        log.info(String.format("   %-16s %s", "Name:", name));
        log.info(String.format("   %-16s %s", "Creator:", creator));
        log.info(String.format("   %-16s %s", "Owner:", owner));
        log.info(String.format("   %-16s %s", "Managers:", GenericFunctions.join(managers, ",")));
        log.info(String.format("   %-16s %.2f", "Minimum Balance:", minBalance));
        log.info(String.format("   %-16s %s", "Unlimited Money:", unlimitedMoney ? "Yes" : "No"));
        log.info(String.format("   %-16s %s", "Unlimited Stock:", unlimitedStock ? "Yes" : "No"));
        if(!global) {
            log.info(String.format("   %-16s %s", "Location A:", locationA.toString()));
            log.info(String.format("   %-16s %s", "Location B:", locationB.toString()));
        }
        log.info(String.format("   %-16s %s", "World:", world));

        // Items
        log.info("Shop Inventory");
        log.info("   BP=Buy Price, BS=Buy Size, SP=Sell Price, SS=Sell Size, ST=Stock, MX=Max Stock");
        log.info(String.format("   %-9s %-6s %-3s %-6s %-3s %-3s %-3s", "Id", "BP", "BS", "SP", "SS", "ST", "MX"));        
        Iterator<InventoryItem> it = inventory.values().iterator();
        while(it.hasNext()) {
            InventoryItem item = it.next();
            ItemInfo info = item.getInfo();
            log.info(String.format("   %6d:%-2d %-6.2f %-3d %-6.2f %-3d %-3d %-3d", info.typeId, info.subTypeId, item.getBuyPrice(), item.getBuySize(), item.getSellPrice(), item.getSellSize(), item.getStock(), item.getMaxStock()));
        }

        // Signs
        log.info("Shop Signs");
        for(ShopSign sign : signSet) {
            log.info(String.format("   %s", sign.toString()));
        }
    }

    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getUuid().compareTo(o2.uuid);
    }


    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setSignSet(Set<ShopSign> signSet) {
        this.signSet = signSet;
    }

    public Set<ShopSign> getSignSet() {
        return signSet;
    }

    public boolean containsPoint(String worldName, int x, int y, int z) {
        if(!worldName.equals(world)) {
            return false;
        }

        if (x >= locationLowX &&
                x <= locationHighX &&
                y >= locationLowY &&
                y <= locationHighY &&
                z >= locationLowZ &&
                z <= locationHighZ) {
            return true;
        } else {
            return false;
        }
    }

    //private int locationLowX, locationHighX, locationLowY, locationHighY, locationLowZ, locationHighZ;
    private void organizeLocation() {
        if(locationA == null || locationB == null) {
            return;
        }

        // X
        if(locationA.getX() > locationB.getX()) {
            locationHighX = locationA.getX();
            locationLowX = locationB.getX();
        } else {
            locationHighX = locationB.getX();
            locationLowX = locationA.getX();            
        }

        // Y
        if(locationA.getY() > locationB.getY()) {
            locationHighY = locationA.getY();
            locationLowY = locationB.getY();
        } else {
            locationHighY = locationB.getY();
            locationLowY = locationA.getY();            
        }

        // Z
        if(locationA.getZ() > locationB.getZ()) {
            locationHighZ = locationA.getZ();
            locationLowZ = locationB.getZ();
        } else {
            locationHighZ = locationB.getZ();
            locationLowZ = locationA.getZ();            
        }
    }

    public void updateSign(ShopSign sign) {

        //TODO: Make method for dealing with sign line logic that returns an array of strings.
        String line1 = sign.getItemName();
        String line2 = "Buy: ";
        String line3 = "Sell: ";
        String line4 = "Stock: " ;

        //If shop no longer carries this item - otherwise update it
        if(this.getItem(sign.getItemName()) == null) {
            line1 = "";
            line2 = "";
            line3 = "";
            line4 = "";
            this.signSet.remove(sign);
        } else {
            if (this.getItem(sign.getItemName()).getBuyPrice() == 0 || (this.getItem(sign.getItemName()).getStock() == 0 && !this.unlimitedStock)) 
                line2 += "-";
            else 
                line2 += this.getItem(sign.getItemName()).getBuyPrice();

            if (this.getItem(sign.getItemName()).getSellPrice() == 0 ) 
                line3 += "-";
            else if (this.getItem(sign.getItemName()).getStock() >= this.getItem(sign.getItemName()).maxStock && !this.unlimitedStock)
                line3 += "Overflow";
            else 
                line3 += this.getItem(sign.getItemName()).getSellPrice();

            if (!this.unlimitedStock)
                line4 += this.getItem(sign.getItemName()).getStock();
            else
                line4 += "-";
        }
        //Set the lines
        ((Sign) sign.getLoc().getBlock().getState()).setLine(0, line1);
        ((Sign) sign.getLoc().getBlock().getState()).setLine(1, line2);
        ((Sign) sign.getLoc().getBlock().getState()).setLine(2, line3);
        ((Sign) sign.getLoc().getBlock().getState()).setLine(3, line4);

        sign.getLoc().getBlock().getState().update();
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
        for (ShopSign sign : this.signSet) {
            if (sign.getItemName().equalsIgnoreCase(itemName))
                updateSign(sign);
        }
    }

    public void updateSigns(Set<ShopSign> signSet) {
        for (ShopSign sign : signSet) 
            updateSign(sign);
    }



}