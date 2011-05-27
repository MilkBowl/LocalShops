package com.milkbukkit.localshops.comparator;

import java.util.Comparator;

import com.milkbukkit.localshops.objects.Shop;


public class ShopSortByOwner implements Comparator<Shop> {

    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getOwner().compareTo(o2.getOwner());
    }

}