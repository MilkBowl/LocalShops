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

    private Item item = null;
    private double buyPrice = 0;
    private double sellPrice = 0;
    private int stock = 0;
    private int baseStock = 0;
    private boolean dynamic = false;
    private int maxStock = 0;

    public ShopRecord(Item item, double buyPrice, double sellPrice, int stock, int maxStock, boolean dynamic) {
        this.item = item;
        setBuyPrice(buyPrice);
        setSellPrice(sellPrice);
        setStock(stock);
        setMaxStock(maxStock);
        setDynamic(dynamic);
    }

    public void setSell(double sellPrice) {
        if(Double.isNaN(sellPrice)) {
            this.sellPrice = 0;
        } else {
            this.sellPrice = sellPrice;
        }
    }

    public void setBuy(double buyPrice) {
        if(Double.isNaN(buyPrice)) {
            this.buyPrice = 0;
        } else {
            this.buyPrice = buyPrice;
        }
    }

    public int getMaxStock() {
        return maxStock;
    }

    public final void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public final void setStock(int stock) {
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

    public final void setSellPrice(double price) {
        if(Double.isNaN(price)) {
            this.sellPrice = 0;
        } else {
            this.sellPrice = price;
        }
    }

    public double getSellPrice() {
        //for dynamic items use the currently adjusted price
        if (dynamic && DynamicManager.getPriceAdjMap().get(item) != null) {
            return sellPrice * DynamicManager.getPriceAdjMap().get(item);
        }

        return sellPrice;
    }

    public final void setBuyPrice(double price) {
        if(Double.isNaN(price)) {
            this.buyPrice = 0;
        } else {
            this.buyPrice = price;
        }
    }

    public double getBuyPrice() {
        //for dynamic items use the current dynamic price
        if (dynamic && DynamicManager.getPriceAdjMap().get(item) != null) {
            return buyPrice * DynamicManager.getPriceAdjMap().get(item);
        }

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
    public final void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public int getBaseStock() {
        return this.baseStock;
    }

    public void setBaseStock(int baseStock) {
        this.baseStock = baseStock;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ShopRecord) {
            return this == obj;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (int) (Double.doubleToLongBits(this.buyPrice) ^ (Double.doubleToLongBits(this.buyPrice) >>> 32));
        hash = 61 * hash + (int) (Double.doubleToLongBits(this.sellPrice) ^ (Double.doubleToLongBits(this.sellPrice) >>> 32));
        hash = 61 * hash + this.stock;
        hash = 61 * hash + (this.dynamic ? 1 : 0);
        hash = 61 * hash + this.maxStock;
        return hash;
    }
}
