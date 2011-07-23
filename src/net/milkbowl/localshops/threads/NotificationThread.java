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

package net.milkbowl.localshops.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import net.milkbowl.localshops.Config;
import net.milkbowl.localshops.LocalShops;
import net.milkbowl.localshops.objects.Shop;
import net.milkbowl.localshops.objects.Transaction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class NotificationThread extends Thread {
    
    private LocalShops plugin;
    private boolean run = true;
    protected final Logger log = Logger.getLogger("Minecraft");
    
    public NotificationThread(LocalShops plugin) {
        this.plugin = plugin;
    }
    
    public void setRun(boolean run) {
        this.run = run;
    }

    public void run() {
        log.info(String.format("[%s] Starting NotificationThread with Timer of %d seconds", plugin.getDescription().getName(), Config.getShopTransactionNoticeTimer()));
        
        while(true) {
            List<Shop> shops = plugin.getShopManager().getAllShops();
            for(final Shop shop : shops) {
                if(!shop.getNotification()) {
                    shop.clearTransactions();
                    continue;
                }
                
                Queue<Transaction> transactions = shop.getTransactions();
                if(transactions.size() == 0) {
                    continue;
                }
                
                final Player player = plugin.getServer().getPlayer(shop.getOwner());
                if (player == null || !player.isOnline()) {
                    continue;
                }
                
                final ArrayList<String> messages = new ArrayList<String>();
                
                if(transactions.size() <= 4) {
                    // List the last 4 transactions...
                    messages.add(String.format(ChatColor.WHITE + "%d " + ChatColor.DARK_AQUA + "transactions for " + ChatColor.WHITE + "%s", transactions.size(), shop.getName()));
                    for(Transaction trans : transactions) {
                        switch (trans.type) {
                        case Buy:
                            messages.add(String.format(ChatColor.WHITE + "   %s " + ChatColor.GOLD + "sold " + ChatColor.WHITE + "%d %s" + ChatColor.DARK_AQUA + " for " + ChatColor.WHITE + "%s", trans.playerName, trans.quantity, trans.itemName, LocalShops.getEcon().format(trans.cost)));
                            break;
                        case Sell:
                            messages.add(String.format(ChatColor.WHITE + "   %s " + ChatColor.GREEN + "purchased " + ChatColor.WHITE + "%d %s" + ChatColor.DARK_AQUA + " for " + ChatColor.WHITE + " %s", trans.playerName, trans.quantity, trans.itemName, LocalShops.getEcon().format(trans.cost)));
                            break;

                        default:
                            // ruh roh lets ignore it
                        }
                    }
                } else {
                    // Summarize the transactions
                    double buyCostTotal = 0;
                    HashMap<String, Double> itemBuyCost = new HashMap<String, Double>();
                    HashMap<String, Integer> itemBuyQuantity = new HashMap<String, Integer>();
                    
                    double sellCostTotal = 0;
                    HashMap<String, Double> itemSellCost = new HashMap<String, Double>();
                    HashMap<String, Integer> itemSellQuantity = new HashMap<String, Integer>();
                    
                    ArrayList<String> players = new ArrayList<String>();
                    
                    for(Transaction trans : transactions) {
                        if (trans.type == Transaction.Type.Sell) {
                            if (itemSellCost.containsKey(trans.itemName)) {
                                itemSellCost.put(trans.itemName, itemSellCost.get(trans.itemName) + trans.cost);
                            } else {
                                itemSellCost.put(trans.itemName, trans.cost);
                            }

                            if (itemSellQuantity.containsKey(trans.itemName)) {
                                itemSellQuantity.put(trans.itemName, itemSellQuantity.get(trans.itemName) + trans.quantity);
                            } else {
                                itemSellQuantity.put(trans.itemName, trans.quantity);
                            }

                            if (!players.contains(trans.playerName)) {
                                players.add(trans.playerName);
                            }
                            
                            buyCostTotal += trans.cost;
                            
                        } else if(trans.type == Transaction.Type.Buy) {
                            if (itemBuyCost.containsKey(trans.itemName)) {
                                itemBuyCost.put(trans.itemName, itemBuyCost.get(trans.itemName) + trans.cost);
                            } else {
                                itemBuyCost.put(trans.itemName, trans.cost);
                            }

                            if (itemBuyQuantity.containsKey(trans.itemName)) {
                                itemBuyQuantity.put(trans.itemName, itemBuyQuantity.get(trans.itemName) + trans.quantity);
                            } else {
                                itemBuyQuantity.put(trans.itemName, trans.quantity);
                            }

                            if (!players.contains(trans.playerName)) {
                                players.add(trans.playerName);
                            }
                            
                            sellCostTotal += trans.cost;
                        }
                    }
                    
                    // Create messages :D
                    messages.add(String.format(ChatColor.WHITE + "%d " + ChatColor.DARK_AQUA + "transactions for " + ChatColor.WHITE + "%s", transactions.size(), shop.getName()));
                    messages.add(String.format(ChatColor.WHITE + "Totals: " + ChatColor.GREEN + "Gained %s, " + ChatColor.GOLD + "Lost %s", LocalShops.getEcon().format(buyCostTotal), LocalShops.getEcon().format(sellCostTotal)));
                    StringBuffer g = null;
                    for(String item : itemSellCost.keySet()) {
                        if(g == null) {
                            g = new StringBuffer();
                            g.append(ChatColor.GREEN);
                            g.append(item);
                        } else {
                            g.append(" ");
                            g.append(item);
                        }
                    }
                    StringBuffer l = null;
                    for(String item : itemBuyCost.keySet()) {
                        if(l == null) {
                            l = new StringBuffer();
                            l.append(ChatColor.GOLD);
                            l.append(item);
                        } else {
                            l.append(" ");
                            l.append(item);
                        }
                    }
                    
                    messages.add(String.format(ChatColor.WHITE + "   Sold: %s", g.toString()));
                    messages.add(String.format(ChatColor.WHITE + "   Bought: %s", l.toString()));
                }
                
                // Register task to send messages ;)
                plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        log.info("test");
                        for (String message : messages) {
                            player.sendMessage(message);
                        }
                        
                        shop.clearTransactions();
                    }
                });
            }
            
            try {
                for(int i = 0; i < Config.getShopTransactionNoticeTimer(); i++) {
                    if(!Config.getShopTransactionNotice() || !run) {
                        break;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                // Ignore it...really its just not important
                return;
            }
            
            if(!Config.getShopTransactionNotice() || !run) {
                break;
            }
        }
    }
}
