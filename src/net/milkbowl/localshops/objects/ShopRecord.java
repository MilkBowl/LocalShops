/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
