package com.milkbukkit.localshops;

public class InventoryItem {

    private ItemInfo info;
    private int buySize = 1;
    private double buyPrice = 0;
    private int sellSize = 1;
    private double sellPrice = 0;
    private int stock;
    private int baseStock = 0;
    private boolean dynamic;
    public int maxStock;

    public InventoryItem() {
        info = null;
        buySize = 1;
        buyPrice = 0;
        sellSize = 1;
        sellPrice = 0;
        stock = 0;
        maxStock = 0;
        dynamic = false;
    }

    public InventoryItem(ItemInfo info) {
        this.info = info;
        buySize = 1;
        buyPrice = 0;
        sellSize = 1;
        sellPrice = 0;
        stock = 0;
        dynamic = false;
    }
    
    public InventoryItem(ItemInfo info, int buySize, double buyPrice, int sellSize, double sellPrice, int stock, int maxStock) {
        this.info = info;
        this.buySize = buySize;
        this.buyPrice = buyPrice;
        this.sellSize = sellSize;
        this.sellPrice = sellPrice;
        this.stock = stock;
        this.maxStock = maxStock;

    }

    public ItemInfo getInfo() {
        return info;
    }

    public void setSell(double sellPrice, int sellSize) {
        if(sellSize < 1) {
            sellSize = 1;
        }
        
        this.sellPrice = sellPrice;
        this.sellSize = sellSize;

    }

    public void setBuy(double buyPrice, int buySize) {
        if(buySize < 1) {
            buySize = 1;
        }
        
        this.buyPrice = buyPrice;
        this.buySize = buySize;
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
        if (dynamic)
            return sellPrice * DynamicManager.getPriceAdjMap().get(info);

        return sellPrice;
    }

    public void setSellSize(int size) {
        if(size < 1) {
            size = 1;
        }
        
        sellSize = size;
    }

    public int getSellSize() {
        return sellSize;
    }

    public void setBuySize(int size) {
        if(size < 1) {
            size = 1;
        }
        
        buySize = size;
    }

    public int getBuySize() {
        return buySize;
    }

    public void setBuyPrice(double price) {
        buyPrice = price;
    }

    public double getBuyPrice() {
        //for dynamic items use the current dynamic price
        if (dynamic)
            return buyPrice * DynamicManager.getPriceAdjMap().get(info);

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
    
    public boolean equals(Object o) {
        if(o instanceof InventoryItem) {
            InventoryItem item = (InventoryItem) o;
            if(item.info == info) {
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    
}
