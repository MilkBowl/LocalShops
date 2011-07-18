package net.milkbowl.localshops.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item {

	protected final Material material;
	protected final short subTypeId;
	protected final String name;

	public Item (Material material, short subTypeId, String name) {
		this.name = name;
		this.material = material;
		if (this.isDurable()) {
			this.subTypeId = 0;
		} else
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
