package net.milkbowl.localshops.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.EconomyResponse;

public class Econ {
	private static Logger log = Logger.getLogger("Minecraft");
	
    public static boolean depositPlayer(String playerName, double cost) {
        EconomyResponse depositResp = Vault.getEconomy().depositPlayer(playerName, cost);
        if(depositResp.transactionSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean payPlayer(String playerFrom, String playerTo, double cost) {
    	LocalShops plugin = (LocalShops) Bukkit.getServer().getPluginManager().getPlugin("LocalShops");
        double balanceFrom = Vault.getEconomy().getBalance(playerFrom);
        double balanceTo = Vault.getEconomy().getBalance(playerTo);
        
        EconomyResponse withdrawResp = Vault.getEconomy().withdrawPlayer(playerFrom, cost);
        if(!withdrawResp.transactionSuccess()) {
            return false;
        }
        
        EconomyResponse depositResp = Vault.getEconomy().depositPlayer(playerTo, cost);
        if(!depositResp.transactionSuccess()) {
            log.info("Failed to deposit");
            // Return money to shop owner
            EconomyResponse returnResp = Vault.getEconomy().depositPlayer(playerFrom, cost);
            if(!returnResp.transactionSuccess()) {
                log.warning(String.format("[%s] ERROR:  Payment failed and could not return funds to original state!  %s may need %s!", plugin.getDescription().getName(), playerFrom, Vault.getEconomy().format(cost)));
            }
            return false;
        }
        
        if (withdrawResp.transactionSuccess() && depositResp.transactionSuccess()) {
            plugin.getShopManager().logPayment(playerFrom, "payment", withdrawResp.amount, balanceFrom, withdrawResp.balance);
            plugin.getShopManager().logPayment(playerTo, "payment", depositResp.amount, balanceTo, depositResp.balance);
            return true;
        } else {
            return false;
        }
    }

    public static double getBalance(String playerName) {
        return Vault.getEconomy().getBalance(playerName);
    }

    public static boolean chargePlayer(String playerName, double chargeAmount) {
    	LocalShops plugin = (LocalShops) Bukkit.getServer().getPluginManager().getPlugin("LocalShops");
        double balance = Vault.getEconomy().getBalance(playerName);
        
        EconomyResponse withdrawResp = Vault.getEconomy().withdrawPlayer(playerName, chargeAmount);
        
        if(withdrawResp.transactionSuccess()) {
            plugin.getShopManager().logPayment(playerName, "payment", withdrawResp.amount, balance, withdrawResp.balance);
            return true;
        } else {
            return false;
        }
    }
}
