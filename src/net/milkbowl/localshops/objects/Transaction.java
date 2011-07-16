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

/*
 * represents a transaction object
 */
public class Transaction implements Cloneable {

    // Transactions are in perspective of the shop
    public static enum Type {
        Buy(1),
        Sell(2);
        
        int id;
        
        Type(int id) {
            this.id = id;
        }
    }
    
    public final Type type;
    public final String playerName;
    public final String itemName;
    public final int quantity;
    public final double cost;
    
    public Transaction(Type type, String playerName, String itemName, int quantity, double cost) {
        this.type = type;
        this.playerName = playerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
    }
    
    public String toString() {
        String rVal = null;
        switch(type) {
        case Buy:
            rVal = String.format("%s sold %d %s for %.2f", playerName, quantity, itemName, cost);
            break;
        case Sell:
            rVal = String.format("%s bought %d %s for %.2f", playerName, quantity, itemName, cost);
            break;
        }
        return rVal;
    }
}