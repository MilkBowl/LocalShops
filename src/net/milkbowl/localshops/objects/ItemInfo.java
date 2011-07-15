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

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemInfo {
    
    public String name = null;
    public String[][] search = null;
    public Material material;
    public short subTypeId = 0;
    
    public ItemInfo(String name, String[][] search, Material material, short subTypeId) {
        this.name = name;
        this.search = search;
        this.material = material;
        this.subTypeId = subTypeId;
    }
    
    public ItemInfo (String name, String[][] search, Material material) {
    	this(name, search, material, (short) 0);
    	
    }
    
    public String toString() {
        return String.format("%s, %s, %d:%d", name, Arrays.deepToString(search), material.getId(), subTypeId);
    }
    
    public ItemStack toStack() {
        return new ItemStack (material, 1, subTypeId);
    }
    
    public boolean equals(ItemInfo item){
        if (this.material == item.material && this.subTypeId == item.subTypeId)
            return true;
        
        return false;
    }
    
    public int getStackSize() {
    	return material.getMaxStackSize();
    }
    
    public int getId() {
    	return material.getId();
    }
}