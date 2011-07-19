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

package net.milkbowl.localshops.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import net.milkbowl.localshops.LocalShops;
import net.milkbowl.vault.economy.EconomyResponse;

public class Econ {
	private static Logger log = Logger.getLogger("Minecraft");
	
    public static boolean depositPlayer(String playerName, double cost) {
        EconomyResponse depositResp = LocalShops.getEcon().depositPlayer(playerName, cost);
        if(depositResp.transactionSuccess()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean payPlayer(String playerFrom, String playerTo, double cost) {
    	LocalShops plugin = (LocalShops) Bukkit.getServer().getPluginManager().getPlugin("LocalShops");
        double balanceFrom = LocalShops.getEcon().getBalance(playerFrom);
        double balanceTo = LocalShops.getEcon().getBalance(playerTo);
        
        EconomyResponse withdrawResp = LocalShops.getEcon().withdrawPlayer(playerFrom, cost);
        if(!withdrawResp.transactionSuccess()) {
            return false;
        }
        
        EconomyResponse depositResp = LocalShops.getEcon().depositPlayer(playerTo, cost);
        if(!depositResp.transactionSuccess()) {
            log.info("Failed to deposit");
            // Return money to shop owner
            EconomyResponse returnResp = LocalShops.getEcon().depositPlayer(playerFrom, cost);
            if(!returnResp.transactionSuccess()) {
                log.warning(String.format("[%s] ERROR:  Payment failed and could not return funds to original state!  %s may need %s!", plugin.getDescription().getName(), playerFrom, LocalShops.getEcon().format(cost)));
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
        return LocalShops.getEcon().getBalance(playerName);
    }

    public static boolean chargePlayer(String playerName, double chargeAmount) {
    	LocalShops plugin = (LocalShops) Bukkit.getServer().getPluginManager().getPlugin("LocalShops");
        double balance = LocalShops.getEcon().getBalance(playerName);
        
        EconomyResponse withdrawResp = LocalShops.getEcon().withdrawPlayer(playerName, chargeAmount);
        
        if(withdrawResp.transactionSuccess()) {
            plugin.getShopManager().logPayment(playerName, "payment", withdrawResp.amount, balance, withdrawResp.balance);
            return true;
        } else {
            return false;
        }
    }
}
