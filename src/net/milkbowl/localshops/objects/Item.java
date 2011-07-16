package net.milkbowl.localshops.objects;

import org.bukkit.Material;

public class Item {
	
    protected final Material material;
    protected final short subTypeId;
    
    public Item (Material material, short subTypeId) {
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
}
