package com.milkbukkit.localshops;

public class InventoryItem {

    private ItemInfo info;
    private int buySize = 0;
    private double buyPrice = 0;
    private int sellSize = 0;
    private double sellPrice = 0;
    private int stock;
    private int baseStock = 0;
    private double dynBuyPrice;
    private double dynSellPrice;
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
        dynBuyPrice = buyPrice;
        dynSellPrice = sellPrice;
        dynamic = false;
    }

    public InventoryItem(ItemInfo info) {
        this.info = info;
        buySize = 1;
        buyPrice = 0;
        sellSize = 1;
        sellPrice = 0;
        stock = 0;
        dynBuyPrice = buyPrice;
        dynSellPrice = sellPrice;
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
        this.dynBuyPrice = buyPrice;
        this.dynSellPrice = sellPrice;
    }

    public ItemInfo getInfo() {
        return info;
    }

    public void setSell(double sellPrice, int sellSize) {
        this.sellPrice = sellPrice;
        this.sellSize = sellSize;

    }

    public void setBuy(double buyPrice, int buySize) {
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
        //Make sure to reset the dynamic price on the item if the price is changed
        dynSellPrice = price;
    }

    public double getSellPrice() {
        //for dynamic items use the current dynamic price
        if (dynamic)
            return dynSellPrice;

        return sellPrice;
    }

    public void setSellSize(int size) {
        sellSize = size;
    }

    public int getSellSize() {
        return sellSize;
    }

    public void setBuySize(int size) {
        buySize = size;
    }

    public int getBuySize() {
        return buySize;
    }

    public void setBuyPrice(double price) {
        buyPrice = price;
        //Make sure to reset the dynamic price on the item if the price is changed
        dynBuyPrice = price;
    }

    public double getBuyPrice() {
        //for dynamic items use the current dynamic price
        if (dynamic)
            return dynBuyPrice;

        return buyPrice;
    }

    public double getDynBuyPrice() {
        return dynBuyPrice;
    }

    public void setDynBuyPrice(double dynBuyPrice) {
        this.dynBuyPrice = dynBuyPrice;
    }

    public double getDynSellPrice() {
        return dynSellPrice;
    }

    public void setDynSellPrice(double dynSellPrice) {
        this.dynSellPrice = dynSellPrice;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Sets the item as dynamic or not
     * Resets the dynamic prices when dynamic-ness is toggled
     * 
     * @param Boolean dynamic
     */
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
        //Reset dynamic prices to base prices when dynamic is enabled
        if (dynamic) {
            this.dynBuyPrice = this.buyPrice;
            this.dynSellPrice = this.sellPrice;
        }
    }

    /**
     * Checks if dynBuyPrice is non-zero and equal to buyPrice
     * 
     * @return True if dynBuyPrice non-zero and equal
     * 
     */
    public boolean isNormalized() {
        if (this.dynBuyPrice == this.buyPrice && this.dynBuyPrice != 0)
            return true;

        return false;
    }
    
    public int getBaseStock() {
        return this.baseStock;
    }

    public void setBaseStock(int baseStock) {
        this.baseStock = baseStock;
    }
    
}
