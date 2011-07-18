package net.milkbowl.localshops.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Item {

	protected final Material material;
	protected final short subTypeId;
	protected final String name;
	private boolean isDurable = false;

	public Item (Material material, short subTypeId, String name) {
		this.name = name;
		this.material = material;
		if (material == Material.CHAINMAIL_BOOTS ||
				material == Material.CHAINMAIL_CHESTPLATE ||
				material == Material.CHAINMAIL_HELMET ||
				material == Material.CHAINMAIL_LEGGINGS ||
				material == Material.WOOD_AXE ||
				material == Material.WOOD_HOE ||
				material == Material.WOOD_PICKAXE ||
				material == Material.WOOD_SPADE ||
				material == Material.WOOD_SWORD ||
				material == Material.STONE_AXE ||
				material == Material.STONE_HOE ||
				material == Material.STONE_PICKAXE ||
				material == Material.STONE_SPADE ||
				material == Material.STONE_SWORD ||
				material == Material.IRON_AXE ||
				material == Material.IRON_BOOTS ||
				material == Material.IRON_CHESTPLATE ||
				material == Material.IRON_HELMET ||
				material == Material.IRON_HOE ||
				material == Material.IRON_LEGGINGS ||
				material == Material.IRON_PICKAXE ||
				material == Material.IRON_SPADE ||
				material == Material.IRON_SWORD ||
				material == Material.GOLD_AXE ||
				material == Material.GOLD_BOOTS ||
				material == Material.GOLD_CHESTPLATE ||
				material == Material.GOLD_HELMET ||
				material == Material.GOLD_HOE ||
				material == Material.GOLD_LEGGINGS ||
				material == Material.GOLD_PICKAXE ||
				material == Material.GOLD_SPADE ||
				material == Material.GOLD_SWORD ||
				material == Material.DIAMOND_AXE ||
				material == Material.DIAMOND_BOOTS ||
				material == Material.DIAMOND_CHESTPLATE ||
				material == Material.DIAMOND_HELMET ||
				material == Material.DIAMOND_HOE ||
				material == Material.DIAMOND_LEGGINGS ||
				material == Material.DIAMOND_PICKAXE ||
				material == Material.DIAMOND_SPADE ||
				material == Material.DIAMOND_SWORD ||
				material == Material.SHEARS) {
			this.subTypeId = 0;
			isDurable = true;
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
		return isDurable;
	}

	public ItemStack toStack() {
		return new ItemStack (this.material, 1, subTypeId);
	}

}
