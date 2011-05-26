package com.milkbukkit.localshops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


import org.bukkit.entity.Player;

import com.milkbukkit.localshops.modules.economy.EconomyResponse;

public class PlayerData {
    // Objects
    private LocalShops plugin = null;

    // Attributes
    public List<UUID> shopList = Collections.synchronizedList(new ArrayList<UUID>());
    public String playerName = null;
    private boolean isSelecting = false;
    private int xyzA[] = null;
    private int xyzB[] = null;
    
    // Logging
    private static final Logger log = Logger.getLogger("Minecraft");    

    // Constructor
    public PlayerData(LocalShops plugin, String playerName) {
        this.plugin = plugin;
        this.playerName = playerName;
    }

    public int[] getPositionA() {
        return xyzA;
    }

    public int[] getPositionB() {
        return xyzB;
    }

    public void setPositionA(int[] xyz) {
        xyzA = xyz.clone();
    }

    public void setPositionB(int[] xyz) {
        xyzB = xyz.clone();
    }

    public boolean addPlayerToShop(Shop shop) {
        String playerWorld = plugin.getServer().getPlayer(playerName).getWorld().getName();

        if (!playerIsInShop(shop) && shop.getWorldName().equalsIgnoreCase(playerWorld)) {
            shopList.add(shop.getUuid());
            return true;
        } else {
            return false;
        }
    }

    public boolean playerIsInShop(Shop shop) {
        String playerWorld = plugin.getServer().getPlayer(playerName).getWorld().getName();

        if (shopList.contains(shop.getUuid())) {
            if (shop.getWorldName().equalsIgnoreCase(playerWorld)) {
                return true;
            }
        }
        return false;
    }

    public void removePlayerFromShop(Player player, UUID uuid) {
        shopList.remove(uuid);
    }

    public List<UUID> playerShopsList(String playerName) {
        return shopList;
    }

    public boolean payPlayer(String playerName, double cost) {
        EconomyResponse depositResp = plugin.getEconManager().depositPlayer(playerName, cost);
        if(depositResp.transactionSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean payPlayer(String playerFrom, String playerTo, double cost) {       
        EconomyResponse balanceFromResp = plugin.getEconManager().getBalance(playerFrom);
        EconomyResponse balanceToResp = plugin.getEconManager().getBalance(playerTo);
        
        log.info("PlayerFrom: " + playerFrom + " balanceFrom: " + balanceFromResp.amount + " PlayerTo: " + playerTo + " balanceTo: " + balanceToResp.amount + " Cost: " + cost);
        
        EconomyResponse withdrawResp = plugin.getEconManager().withdrawPlayer(playerFrom, cost);
        if(!withdrawResp.transactionSuccess()) {
            log.info("Failed to withdraw");
            return false;
        }
        
        EconomyResponse depositResp = plugin.getEconManager().depositPlayer(playerTo, cost);
        if(!depositResp.transactionSuccess()) {
            log.info("Failed to deposit");
            // Return money to shop owner
            EconomyResponse returnResp = plugin.getEconManager().depositPlayer(playerFrom, cost);
            if(!returnResp.transactionSuccess()) {
                log.warning(String.format("[%s] ERROR:  Payment failed and could not return funds to original state!  %s may need %s!", plugin.pdfFile.getName(), playerName, plugin.getEconManager().format(cost)));
            }
            return false;
        }
        
        if (withdrawResp.transactionSuccess() && depositResp.transactionSuccess()) {
            plugin.getShopManager().logPayment(playerFrom, "payment", withdrawResp.amount, balanceFromResp.amount, withdrawResp.balance);
            plugin.getShopManager().logPayment(playerTo, "payment", depositResp.amount, balanceToResp.amount, depositResp.balance);
            return true;
        } else {
            return false;
        }
    }

    public double getBalance(String playerName) {
        EconomyResponse balanceResp = plugin.getEconManager().getBalance(playerName);
        return balanceResp.amount;
    }

    public boolean chargePlayer(String playerName, double chargeAmount) {
        EconomyResponse balanceResp = plugin.getEconManager().getBalance(playerName);
        if(!balanceResp.transactionSuccess()) {
            return false;
        }
        
        EconomyResponse withdrawResp = plugin.getEconManager().withdrawPlayer(playerName, chargeAmount);
        if(withdrawResp.transactionSuccess()) {
            plugin.getShopManager().logPayment(playerName, "payment", withdrawResp.amount, balanceResp.balance, withdrawResp.balance);
            return true;
        } else {
            return false;
        }
    }
    
    public UUID getCurrentShop() {
        if(shopList.size() == 1) {
            return shopList.get(0);
        } else {
            return null;
        }
    }

    public void setSelecting(boolean isSelecting) {
        this.isSelecting = isSelecting;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

}
