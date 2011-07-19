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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item {

	protected final Material material;
	protected final short subTypeId;
	protected final String name;
	
	public Item(Material material, String name) {
	    this.material = material;
	    this.name = name;
	    this.subTypeId = 0;
	}

	public Item(Material material, short subTypeId, String name) {
		this.name = name;
		this.material = material;
		this.subTypeId = subTypeId;
	}

	public Material getType() {
		return material;
	}

	public short getSubTypeId() {
		return subTypeId;
	}

	public int getStackSize() {
		return material.getMaxStackSize();
	}

	public int getId() {
		return material.getId();
	}

	public String getName() {
		return name;
	}

	public int getMaxBundleSize() {
		return material.getMaxStackSize();
	}

	public boolean equals (Object obj) {
		if (obj instanceof Item)
			return (this.material == ((Item) obj).material && this.subTypeId == ((Item) obj).subTypeId);
		else
			return false;
	}

	public boolean isDurable() {
		return (material.getMaxDurability() > 0);
	}

	public ItemStack toStack() {
		return new ItemStack (this.material, 1, subTypeId);
	}

}
