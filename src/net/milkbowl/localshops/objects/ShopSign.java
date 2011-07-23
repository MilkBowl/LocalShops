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

import net.milkbowl.localshops.exceptions.TypeNotFoundException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author sleaker
 *
 */
public class ShopSign {

    private int x;
    private int y;
    private int z;
    private World world = null;
    private String signWorld = null;
    private String item;
    private int amount = 1;
    private SignType type = null;

    public static enum SignType {

        INFO(0),
        BUY(1),
        SELL(2);
        private final int id;

        SignType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public ShopSign(String signWorld, int x, int y, int z, String itemName, int typeId, int amount) throws TypeNotFoundException {
        this.signWorld = signWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        if (amount > 1 && !(typeId == 1)) {
            this.amount = amount;
        }

        for (SignType t : SignType.values()) {
            if (t.getId() == typeId) {
                this.type = t;
                break;
            }
        }
        if (type == null) {
            // ruh roh!
            throw new TypeNotFoundException(String.format("Sign type %d not found.", typeId));
        }
    }

    public ShopSign(World world, int x, int y, int z, String itemName, int typeId, int amount) throws TypeNotFoundException {
        this.signWorld = world.getName();
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = itemName;
        if (amount > 1 && !(typeId == 0)) {
            this.amount = amount;
        }

        for (SignType t : SignType.values()) {
            if (t.getId() == typeId) {
                this.type = t;
                break;
            }
        }
        if (type == null) {
            // ruh roh!
            throw new TypeNotFoundException(String.format("Sign type %d not found.", typeId));
        }
    }

    public ShopSign(Block block, String itemName, int typeId, int amount) throws TypeNotFoundException {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ(), itemName, typeId, amount);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(signWorld);
        string.append(":");
        string.append(x);
        string.append(",");
        string.append(y);
        string.append(",");
        string.append(z);
        string.append(",");
        string.append(item);
        string.append(",");
        string.append(type.getId());
        string.append(",");
        string.append(amount);
        return string.toString();
    }

    public boolean isValid() {
        if (world == null) {
            return true;
        }

        world.loadChunk(world.getChunkAt(getLoc()));
        if (world.getBlockAt(getLoc()).getType() == Material.WALL_SIGN || world.getBlockAt(getLoc()).getType() == Material.SIGN_POST) {
            return true;
        } else {
            return false;
        }
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Location getLoc() {
        if (world == null) {
            world = Bukkit.getServer().getWorld(signWorld);
        }

        if (world == null) {
            return null;
        } else {
            return new Location(this.world, this.x, this.y, this.z);
        }
    }

    public String getItemName() {
        return this.item;
    }

    public String getWorldName() {
        return this.signWorld;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public SignType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShopSign) {
            ShopSign sign = (ShopSign) o;
            if (this.getWorldName().equals(sign.getWorldName()) && this.x == sign.x && this.y == sign.y && this.z == sign.z && this.item.equals(sign.item) && this.type.equals(sign.type)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.y;
        hash = 59 * hash + this.z;
        hash = 59 * hash + (this.signWorld != null ? this.signWorld.hashCode() : 0);
        hash = 59 * hash + (this.item != null ? this.item.hashCode() : 0);
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}
