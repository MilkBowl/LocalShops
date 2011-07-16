package net.milkbowl.localshops.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item {
	
    protected final Material material;
    protected final short subTypeId;
    protected final String name;
    
    public Item (Material material, short subTypeId, String name) {
    	this.name = name;
    	this.material = material;
    	this.subTypeId = subTypeId;
    }
    
    public Material getType() {
    	return material;
    }
    
    public short getSubTypeId() {
    	return subTypeId;
    }
    
    public int getStackSize() {
    	return material.getMaxStackSize();
    }
    
    public int getId() {
    	return material.getId();
    }
    
    public String getName() {
		return name;
	}

	public int getMaxBundleSize() {
    	return material.getMaxStackSize();
    }
	
	public boolean equals (Item item) {
		return (this.material == item.material && this.subTypeId == item.subTypeId);
	}
	
    public ItemStack toStack() {
        return new ItemStack (this.material, 1, subTypeId);
    }
}
