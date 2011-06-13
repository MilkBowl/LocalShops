package net.milkbowl.localshops;

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
}