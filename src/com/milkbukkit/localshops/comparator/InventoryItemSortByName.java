package com.milkbukkit.localshops.comparator;

import java.util.Comparator;

import com.milkbukkit.localshops.objects.InventoryItem;


public class InventoryItemSortByName implements Comparator<InventoryItem> {

    @Override
    public int compare(InventoryItem o1, InventoryItem o2) {
        return o1.getInfo().name.compareTo(o2.getInfo().name);
    }
    
}