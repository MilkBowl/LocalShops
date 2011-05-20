package net.centerleft.localshops;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import org.bukkit.Location;

import cuboidLocale.PrimitiveCuboid;

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
    private HashMap<String, InventoryItem> inventory = new HashMap<String, InventoryItem>();
    private PrimitiveCuboid cuboid = null;
    private double minBalance = 0;
    private ArrayBlockingQueue<Transaction> transactions;
    private boolean notification = true;
    private HashMap<Location, String> signList = null;
    
    // Logging
    private static final Logger log = Logger.getLogger("Minecraft");    

    public Shop(UUID uuid) {
        this.uuid = uuid;
        transactions = new ArrayBlockingQueue<Transaction>(Config.SHOP_TRANSACTION_MAX_SIZE);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setWorld(String name) {
        world = name;
    }

    public String getWorld() {
        return world;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocations(ShopLocation locationA, ShopLocation locationB) {
        this.locationA = locationA;
        this.locationB = locationB;
    }

    public void setLocationA(ShopLocation locationA) {
        this.locationA = locationA;
    }

    public void setLocationA(long x, long y, long z) {
        locationA = new ShopLocation(x, y, z);
    }

    public void setLocationB(ShopLocation locationB) {
        this.locationB = locationB;
    }

    public void setLocationB(long x, long y, long z) {
        locationB = new ShopLocation(x, y, z);
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
        double[] xyz = new double[3];
        double[] xyzA = locationA.toArray();
        double[] xyzB = locationA.toArray();

        for (int i = 0; i < 3; i++) {
            if (xyzA[i] < xyzB[i]) {
                xyz[i] = xyzA[i] + (Math.abs(xyzA[i] - xyzB[i])) / 2;
            } else {
                xyz[i] = xyzA[i] - (Math.abs(xyzA[i] - xyzB[i])) / 2;
            }
        }

        return new ShopLocation(xyz);
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

    public InventoryItem getItem(String item) {
        return inventory.get(item);
    }
    
    public boolean containsItem(ItemInfo item) {
        Iterator<InventoryItem> it = inventory.values().iterator();
        while(it.hasNext()) {
            InventoryItem invItem = it.next();
            ItemInfo invItemInfo = invItem.getInfo();
            if(invItemInfo.typeId == item.typeId && invItemInfo.subTypeId == item.subTypeId) {
                return true;
            }
        }
        return false;
    }
    
    public String getShortUuidString() {
        String sUuid = uuid.toString();
        return sUuid.substring(sUuid.length() - Config.UUID_MIN_LENGTH);
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

    public boolean addItem(int itemNumber, short itemData, double buyPrice, int buyStackSize, double sellPrice, int sellStackSize, int stock, int maxStock) {
        // TODO add maxStock to item object
        ItemInfo item = Search.itemById(itemNumber, itemData);
        if(item == null) {
            return false;
        }
        
        InventoryItem thisItem = new InventoryItem(item);

        thisItem.setBuy(buyPrice, buyStackSize);
        thisItem.setSell(sellPrice, sellStackSize);

        thisItem.setStock(stock);

        thisItem.maxStock = maxStock;

        if (inventory.containsKey(item.name)) {
            inventory.remove(item.name);
        }

        inventory.put(item.name, thisItem);
        
        return true;
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

    public PrimitiveCuboid getCuboid() {
        // If no cuboid, create it
        if (cuboid == null) {
            // Check if either locaiton is null and return appropriately
            if (locationA == null || locationB == null) {
                return null;
            }
            cuboid = new PrimitiveCuboid(getLocationA().toArray(), getLocationB().toArray());
            cuboid.uuid = uuid;
            cuboid.world = world;
        }

        return cuboid;
    }

    public String toString() {
        return String.format("Shop \"%s\" at [%s], [%s] %d items - %s", this.name, locationA.toString(), locationB.toString(), inventory.size(), uuid.toString());
    }
    
    public void log() {
        // Details
        log.info("Shop Information");
        log.info(String.format("   %-16s %s", "UUID:", uuid.toString()));
        log.info(String.format("   %-16s %s", "Name:", name));
        log.info(String.format("   %-16s %s", "Creator:", creator));
        log.info(String.format("   %-16s %s", "Owner:", owner));
        log.info(String.format("   %-16s %s", "Managers:", Search.join(managers, ",")));
        log.info(String.format("   %-16s %.2f", "Minimum Balance:", minBalance));
        log.info(String.format("   %-16s %s", "Unlimited Money:", unlimitedMoney ? "Yes" : "No"));
        log.info(String.format("   %-16s %s", "Unlimited Stock:", unlimitedStock ? "Yes" : "No"));        
        log.info(String.format("   %-16s %s", "Location A:", locationA.toString()));
        log.info(String.format("   %-16s %s", "Location B:", locationB.toString()));
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
    }
    
    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getUuid().compareTo(o2.uuid);
    }

    public void setSignList(HashMap<Location, String> signList) {
        this.signList = signList;
    }

    public HashMap<Location, String> getSignList() {
        return signList;
    }

}