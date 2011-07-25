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

    public boolean playerIsInShop(Shop shop) {
        if (shop.containsPoint(plugin.getServer().getPlayer(playerName).getLocation())) {
            return true;
        }
        return false;
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
