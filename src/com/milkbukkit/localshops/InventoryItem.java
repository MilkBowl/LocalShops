package com.milkbukkit.localshops;

public class InventoryItem {

    private ItemInfo info;
    private int buySize = 0;
    private double buyPrice = 0;
    private int sellSize = 0;
    private double sellPrice = 0;
    private int stock;
    public int maxStock;

    public InventoryItem() {
        info = null;
        buySize = 1;
        buyPrice = 0;
        sellSize = 1;
        sellPrice = 0;
        stock = 0;
        maxStock = 0;
    }

    public InventoryItem(ItemInfo info) {
        this.info = info;
        buySize = 1;
        buyPrice = 0;
        sellSize = 1;
        sellPrice = 0;
        stock = 0;
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
    }

    public double getSellPrice() {
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
    }

    public double getBuyPrice() {
        return buyPrice;
    }
}
