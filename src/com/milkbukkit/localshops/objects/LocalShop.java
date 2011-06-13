package com.milkbukkit.localshops.objects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import com.milkbukkit.localshops.util.GenericFunctions;

public class LocalShop extends Shop {
    // Location Information
    //TODO: Store Location information in List to match World Information indices
    protected Set<ShopLocation> shopLocations = Collections.synchronizedSet(new HashSet<ShopLocation>(1));
    protected Set<Location> chests = null;

    public LocalShop(UUID uuid) {
        super(uuid);
    }


    /*
     * Initialized the chest set if it is empty, or return the set if it's non-null
     */
    public Set<Location> getChests() {
        if (chests == null) {
            chests = Collections.synchronizedSet(new HashSet<Location>());
            for (ShopLocation shopLoc : shopLocations) {
                chests.addAll(shopLoc.findBlocks(Material.CHEST));
            }
        }
        return chests;
    }

    public Set<ShopLocation> getShopLocations() {
        return shopLocations;
    }

    
     // TODO: Fix to-string method for new shop-location (embed in ShopLocation class?
    public String toString() {
        return null;
        /*
        String locA = "";
        String locB = "";
        if (locA != null) {
            locA = locationA.toString();
        }
        if (locB != null) {
            locB = locationB.toString();
        }
        return String.format("Shop \"%s\" at [%s], [%s] %d items - %s", this.name, locA, locB, inventory.size(), uuid.toString());
        */
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
        /* 
         * TODO: Redo Shop Location output
        log.info(String.format("   %-16s %s", "Location A:", locationA.toString()));
        log.info(String.format("   %-16s %s", "Location B:", locationB.toString()));
        */
        log.info(String.format("   %-16s %s", "World:", GenericFunctions.join(worlds, ", ")));

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

    //TODO: Support world# check vs location data check.
    public boolean containsPoint(String worldName, int x, int y, int z) {
        for (ShopLocation shopLoc : shopLocations) {
            if (shopLoc.getWorld().getName().equals(worldName)) {
                Location loc1 = shopLoc.getLocation1();
                Location loc2 = shopLoc.getLocation2();
                
                if (x >= loc1.getBlockX() && x <= loc2.getBlockX() && y >= loc1.getBlockY() && y <= loc2.getBlockY() && z >= loc1.getBlockZ() && z <= loc2.getBlockZ())
                    return true;
            }
        }
        return false;
    }
}
