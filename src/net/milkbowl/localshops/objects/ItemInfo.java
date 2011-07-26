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

import java.util.Arrays;

import org.bukkit.Material;

public class ItemInfo extends Item {

    public final String[][] search;

    public ItemInfo(String name, String[][] search, Material material, short subTypeId) {
        super(material, subTypeId, name);
        this.search = search.clone();
    }

    public ItemInfo(String name, String[][] search, Material material) {
        super(material, name);
        this.search = search.clone();
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d:%d", name, Arrays.deepToString(search), material.getId(), subTypeId);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
