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

import net.milkbowl.localshops.DynamicManager;

public class ShopRecord {

    private double buyPrice = 0;
    private double sellPrice = 0;
    private int stock = 0;
    private int baseStock = 0;
    private boolean dynamic = false;
    private int maxStock = 0;
    
    public ShopRecord(double buyPrice, double sellPrice, int stock, int maxStock, boolean dynamic) {
    	this.buyPrice = buyPrice;
    	this.sellPrice = sellPrice;
    	this.stock = stock;
    	this.maxStock = maxStock;
    	this.dynamic = dynamic;
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

    public void setMaxStock(int maxStock) {
		this.maxStock = maxStock;
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
    
    public boolean equals (Object obj) {
    	if (obj instanceof ShopRecord) {
    		return this == obj;
    	} else {
    	    return false;
    	}
    }
    
}
