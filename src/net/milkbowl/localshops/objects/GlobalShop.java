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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.milkbowl.localshops.util.GenericFunctions;


public class GlobalShop extends Shop {
    private static final long serialVersionUID = 30000L;
    
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
        log.info(String.format("   %-16s %s", "Groups:", GenericFunctions.join(groups, ", ")));
        log.info(String.format("   %-16s %s", "Users:", GenericFunctions.join(users, ", ")));
        log.info(String.format("   %-16s %s", "Worlds:", GenericFunctions.join(worlds, ", ")));

        // Items
        log.info("Shop Inventory");
        log.info("   BP=Buy Price, BS=Buy Size, SP=Sell Price, SS=Sell Size, ST=Stock, MX=Max Stock");
        log.info(String.format("   %-9s %-6s %-3s %-6s %-3s %-3s %-3s", "Id", "BP", "BS", "SP", "SS", "ST", "MX"));
        Iterator<Item> it = inventory.keySet().iterator();
        while(it.hasNext()) {
            Item item = it.next();
            ShopRecord record = inventory.get(item);
            log.info(String.format("   %6d:%-2d %-6.2f %-6.2f %-3d %-3d", item.getId(), item.getSubTypeId(), record.getBuyPrice(), record.getSellPrice(), record.getStock(), record.getMaxStock()));
        }
    }

}
