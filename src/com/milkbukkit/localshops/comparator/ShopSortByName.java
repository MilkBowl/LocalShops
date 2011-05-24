package com.milkbukkit.localshops.comparator;

import java.util.Comparator;

import com.milkbukkit.localshops.Shop;


public class ShopSortByName implements Comparator<Shop> {

    @Override
    public int compare(Shop o1, Shop o2) {
        return o1.getName().compareTo(o2.getName());
    }

}