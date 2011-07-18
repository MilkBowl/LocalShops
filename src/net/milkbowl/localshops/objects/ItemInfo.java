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


public class ItemInfo extends Item {
    

    public final String[][] search;

    
    public ItemInfo(String name, String[][] search, Material material, short subTypeId) {
    	super(material, subTypeId, name);
        this.search = search;
    }
    
    public ItemInfo (String name, String[][] search, Material material) {
    	super(material, name);
    	this.search = search;
    }
    
    public String toString() {
        return String.format("%s, %s, %d:%d", name, Arrays.deepToString(search), material.getId(), subTypeId);
    }
    
    public boolean equals(Object obj){
    	return super.equals(obj);
    }
    
}