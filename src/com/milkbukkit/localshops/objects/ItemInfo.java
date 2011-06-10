package com.milkbukkit.localshops.objects;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

public class ItemInfo {
    
    public static enum StackMode {
        VANILLA(0),
        ENHANCED(1),
        MAXIMUM(2);
        
        int id;
        
        StackMode(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
    }
    
    public String name = null;
    public String[][] search = null;
    public int typeId = -1;
    public short subTypeId = 0;
    public int[] maxStackSize = { 64, 64, 64 };
    
    public ItemInfo(String name, String[][] search, int typeId, short subTypeId) {
        this.name = name;
        this.search = search;
        this.typeId = typeId;
        this.subTypeId = subTypeId;
    }
    
    public ItemInfo(String name, String[][] search, int typeId, short subTypeId, int[] maxStackSize) {
        this(name, search, typeId, subTypeId);
        this.maxStackSize = maxStackSize;
    }
    
    public String toString() {
        return String.format("%s, %s, %d:%d", name, Arrays.deepToString(search), typeId, subTypeId);
    }
    
    public ItemStack toStack() {
        return new ItemStack (typeId, 1, subTypeId);
    }
    
    public boolean equals(ItemInfo item){
        if (this.typeId == item.typeId && this.subTypeId == item.subTypeId)
            return true;
        
        return false;
    }
}