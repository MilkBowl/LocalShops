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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.milkbowl.localshops.util.GenericFunctions;


public class GlobalShop extends Shop {
    protected List<String> worlds = Collections.synchronizedList(new ArrayList<String>(1));
    
    public GlobalShop(UUID uuid) {
        super(uuid);
    }
    
    public void addWorld(String worldName) {
        if (!worlds.contains(worldName))
            worlds.add(worldName);
    }
    
    public void removeWorld(String worldName) {
        worlds.remove(worldName);
    }
    
    public boolean containsWorld(String worldName) {
        return worlds.contains(worldName);
    }
    
    public void clearWorlds() {
        worlds.clear();
    }
    
    public List<String> getWorlds() {
        List<String> list = new ArrayList<String>();
        for(String s : worlds) {
            list.add(s);
        }
        
        return Collections.unmodifiableList(list);
    }
    
    @Override
    public String toString() {
        return String.format("Shop \"%s\" in worlds \"%s\" %d items - %s", this.name, GenericFunctions.join(worlds, ", "), inventory.size(), uuid.toString());
    }

    @Override
    public void log() {
        // Details
        log.info("Shop Information");
        log.info(String.format("   %-16s %s", "UUID:", uuid.toString()));
        log.info(String.format("   %-16s %s", "Type:", "Global"));
        log.info(String.format("   %-16s %s", "Name:", name));
        log.info(String.format("   %-16s %s", "Creator:", creator));
        log.info(String.format("   %-16s %s", "Owner:", owner));
        log.info(String.format("   %-16s %s", "Managers:", GenericFunctions.join(managers, ",")));
        log.info(String.format("   %-16s %.2f", "Minimum Balance:", minBalance));
        log.info(String.format("   %-16s %s", "Unlimited Money:", unlimitedMoney ? "Yes" : "No"));
        log.info(String.format("   %-16s %s", "Unlimited Stock:", unlimitedStock ? "Yes" : "No"));
        log.info(String.format("   %-16s %s", "Worlds:", GenericFunctions.join(worlds, ", ")));

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

}
