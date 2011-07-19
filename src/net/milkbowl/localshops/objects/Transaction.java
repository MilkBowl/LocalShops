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