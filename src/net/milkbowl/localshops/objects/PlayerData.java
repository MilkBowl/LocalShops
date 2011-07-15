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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.milkbowl.localshops.LocalShops;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class PlayerData {
    // Objects
    private LocalShops plugin = null;

    // Attributes
    public List<UUID> shopList = Collections.synchronizedList(new ArrayList<UUID>());
    public String playerName = null;
    private boolean isSelecting = false;
    private Location xyzA = null;
    private Location xyzB = null;

    // Constructor
    public PlayerData(LocalShops plugin, String playerName) {
        this.plugin = plugin;
        this.playerName = playerName;
    }

    public Location getPositionA() {
        return xyzA;
    }

    public Location getPositionB() {
        return xyzB;
    }

    public void setPositionA(Location xyz) {
        xyzA = xyz.clone();
    }

    public void setPositionB(Location xyz) {
        xyzB = xyz.clone();
    }

    public boolean addPlayerToShop(Shop shop) {
        String playerWorld = plugin.getServer().getPlayer(playerName).getWorld().getName();

        if (shop instanceof LocalShop) {
            if (!playerIsInShop(shop) && ((LocalShop) shop).getWorld().equals(playerWorld)) {
                shopList.add(shop.getUuid());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean playerIsInShop(Shop shop) {
        String playerWorld = plugin.getServer().getPlayer(playerName).getWorld().getName();

        if (shop instanceof GlobalShop) {
            if ( ((GlobalShop) shop).containsWorld(playerWorld))
                ;
        } else if (shop instanceof LocalShop) {
            if (shopList.contains(shop.getUuid())) {
                if ( ((LocalShop) shop).getWorld().equals(playerWorld) ) {
                    return true;
                }
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
    
    public UUID getCurrentShop() {
        if(shopList.size() == 1) {
            return shopList.get(0);
        } else {
            return null;
        }
    }

    /*
     * Sets a players selection mode
     */
    public void setSelecting(boolean isSelecting) {
        this.isSelecting = isSelecting;
    }

    /*
     * Gets the players current selection mode
     */
    public boolean isSelecting() {
        return isSelecting;
    }
}
