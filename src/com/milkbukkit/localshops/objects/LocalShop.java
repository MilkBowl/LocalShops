package com.milkbukkit.localshops.objects;

import java.util.Iterator;
import java.util.UUID;

import com.milkbukkit.localshops.InventoryItem;
import com.milkbukkit.localshops.ItemInfo;
import com.milkbukkit.localshops.ShopSign;
import com.milkbukkit.localshops.util.GenericFunctions;

public class LocalShop extends Shop {
    // Location Information
    private String world = null;
    private ShopLocation locationA = null;
    private ShopLocation locationB = null;
    private int locationLowX, locationHighX, locationLowY, locationHighY, locationLowZ, locationHighZ;

    public LocalShop(UUID uuid) {
        super(uuid);
    }

    /**
     * Sets the World the shop is located in
     * Updates the Set list for worlds
     * 
     * @param String world of the shop
     */
    public void setWorld(String name) {
        this.worlds.remove(world);
        world = name;
        this.worlds.add(name);
    }

    /**
     * Returns the name of the world the shop is located in
     * 
     * @return String world of the shop
     */
    public String getWorld() {
        return world;
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
    
    public String toString() {
        String locA = "";
        String locB = "";
        if (locA != null) {
            locA = locationA.toString();
        }
        if (locB != null) {
            locB = locationB.toString();
        }
        return String.format("Shop \"%s\" at [%s], [%s] %d items - %s", this.name, locA, locB, inventory.size(), uuid.toString());
    }
    
    public void log() {
        // Details
        log.info("Shop Information");
        log.info(String.format("   %-16s %s", "UUID:", uuid.toString()));
        log.info(String.format("   %-16s %s", "Type:", "Local"));
        log.info(String.format("   %-16s %s", "Name:", name));
        log.info(String.format("   %-16s %s", "Creator:", creator));
        log.info(String.format("   %-16s %s", "Owner:", owner));
        log.info(String.format("   %-16s %s", "Managers:", GenericFunctions.join(managers, ",")));
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

        // Signs
        log.info("Shop Signs");
        for (ShopSign sign : signSet) {
            log.info(String.format("   %s", sign.toString()));
        }
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
}
