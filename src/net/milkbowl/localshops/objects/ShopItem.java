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

import org.bukkit.inventory.ItemStack;

import net.milkbowl.localshops.DynamicManager;

public class ShopItem extends Item {

    private double buyPrice = 0;
    private double sellPrice = 0;
    private int stock;
    private int baseStock = 0;
    private boolean dynamic;
    public int maxStock;


    public ShopItem(ItemInfo info) {
    	super (info.getType(), info.getSubTypeId(), info.getName());
        buyPrice = 0;
        sellPrice = 0;
        stock = 0;
        dynamic = false;
    }
    
    public ShopItem(ItemInfo info, double buyPrice, double sellPrice, int stock, int maxStock) {
    	super(info.getType(), info.getSubTypeId(), info.getName());
    	this.buyPrice = buyPrice;
    	this.sellPrice = sellPrice;
    	this.stock = stock;
    	this.maxStock = maxStock;
    }

    public void setSell(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setBuy(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void addStock(int stock) {
        this.stock += stock;
    }

    public void removeStock(int stock) {
        this.stock -= stock;
        if (this.stock < 0) {
            this.stock = 0;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setSellPrice(double price) {
        sellPrice = price;
    }

    public double getSellPrice() {
        //for dynamic items use the currently adjusted price
        if (dynamic && DynamicManager.getPriceAdjMap().get(this) != null)
            return sellPrice * DynamicManager.getPriceAdjMap().get(this);

        return sellPrice;
    }

    public void setBuyPrice(double price) {
        buyPrice = price;
    }

    public double getBuyPrice() {
        //for dynamic items use the current dynamic price
        if (dynamic && DynamicManager.getPriceAdjMap().get(this) != null)
            return buyPrice * DynamicManager.getPriceAdjMap().get(this);

        return buyPrice;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Toggles the item as dynamic or not
     * 
     * @param Boolean dynamic
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public int getBaseStock() {
        return this.baseStock;
    }

    public void setBaseStock(int baseStock) {
        this.baseStock = baseStock;
    }
    
    /**
     * TODO: this should return ItemStack[] which conforms to MC stack size.
     * And should be Overrided here for simplicity.
     * @return
     */
    @Override
    public ItemStack toStack() {
        return new ItemStack (this.material, 1, subTypeId);
    }
    
    public boolean equals (Object obj) {
    	if (obj instanceof ShopItem) {
    		return this == obj;
    	} else if (obj instanceof Item || obj instanceof ItemInfo) {
    		return super.equals(obj);
    	}
    	else
    		return false;
    }
    
}
